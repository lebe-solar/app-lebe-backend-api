package de.lebe.backend.graphql;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.graph.beta.models.BookingBusiness;
import com.microsoft.graph.beta.models.BookingService;
import com.microsoft.graph.beta.models.BookingStaffMember;
import com.microsoft.graph.beta.models.Event;
import com.microsoft.graph.beta.serviceclient.GraphServiceClient;

@Service
public class MicrosoftBookingClient {

	@Autowired
    private final GraphServiceClient graphClient;
    private final String bookingBusinessId = "LeBeMontageTeam1@lebe-solarenergie.de";


    /**
     * Retrieves all available days within the business hours and with free slots for the given service duration.
     *
     * @param serviceName Name of the service to check availability for (e.g., "Montage").
     * @return List of available days with free slots.
     */
    public List<LocalDate> getAvailableDays(String serviceName) {
        // Step 1: Fetch business hours and services
        BookingBusiness business = graphClient
        		.bookingBusinesses()
        		.byBookingBusinessId("LeBeMontageTeam1@lebe-solarenergie.de")
        		.get();

        BookingService service = getServiceByName(serviceName, business.getServices());

        if (service == null) {
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }

        // Step 2: Define the time range (e.g., current month)
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        // Step 3: Retrieve business hours and staff schedules
        var staffMembers = getStaffForService(service.getId());
        List<LocalDate> businessDays = getBusinessDays(startOfMonth, endOfMonth, business);

        // Step 4: Filter days with available slots
        return businessDays.stream()
                .filter(day -> hasAvailableSlot(day, staffMembers, service.getDefaultDuration()))
                .collect(Collectors.toList());
    }

    /**
     * Fetches the service by name from the list of services.
     */
    private BookingService getServiceByName(String serviceName, List<BookingService> services) {
        return services.stream()
                .filter(service -> service.displayName.equalsIgnoreCase(serviceName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the staff members assigned to a specific service.
     */
    private List<BookingStaffMember> getStaffForService(String serviceId) {
        return graphClient.bookingBusinesses()
        		.byBookingBusinessId("LeBeMontageTeam1@lebe-solarenergie.de")
                .staffMembers()
                .get().getValue();
    }

    /**
     * Retrieves the business days (working days) within the specified date range.
     */
    private List<LocalDate> getBusinessDays(LocalDate startDate, LocalDate endDate, BookingBusiness business) {
        // Business hours as LocalTime ranges (assumes single time zone)
        Map<DayOfWeek, LocalTime[]> businessHours = business.businessHours.stream()
                .collect(Collectors.toMap(
                        hour -> DayOfWeek.valueOf(hour.day),
                        hour -> new LocalTime[]{
                                LocalTime.parse(hour.start, DateTimeFormatter.ISO_LOCAL_TIME),
                                LocalTime.parse(hour.end, DateTimeFormatter.ISO_LOCAL_TIME)
                        }
                ));

        // Generate a list of business days
        return startDate.datesUntil(endDate.plusDays(1))
                .filter(date -> businessHours.containsKey(date.getDayOfWeek()))
                .collect(Collectors.toList());
    }

    /**
     * Checks if any of the staff members has a free slot of the required duration on the given day.
     */
    private boolean hasAvailableSlot(LocalDate day, List<BookingStaffMember> staffMembers, Duration duration) {
        for (BookingStaffMemberBase staff : staffMembers) {
            List<Event> events = graphClient
                    .bookingBusinesses(bookingBusinessId)
                    .staffMembers(staff.id)
                    .calendarView()
                    .buildRequest()
                    .filter(String.format("start/dateTime ge '%s' and end/dateTime le '%s'",
                            formatDate(day.atStartOfDay()), formatDate(day.plusDays(1).atStartOfDay())))
                    .get()
                    .getCurrentPage();

            if (hasSlot(events, duration, day)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if there is a slot of the required duration available for the given day.
     */
    private boolean hasSlot(List<Event> events, Duration requiredDuration, LocalDate day) {
        // Generate business hours for the day
        List<LocalTime> businessHours = List.of(
                LocalTime.of(9, 0), // Example: Start at 9 AM
                LocalTime.of(17, 0) // Example: End at 5 PM
        );

        LocalTime availableStart = businessHours.get(0);
        for (Event event : events) {
            LocalTime eventStart = LocalTime.parse(event.start.dateTime, DateTimeFormatter.ISO_LOCAL_TIME);
            LocalTime eventEnd = LocalTime.parse(event.end.dateTime, DateTimeFormatter.ISO_LOCAL_TIME);

            if (Duration.between(availableStart, eventStart).compareTo(requiredDuration) >= 0) {
                return true;
            }

            availableStart = eventEnd;
        }

        // Check if there is a slot after the last event
        return Duration.between(availableStart, businessHours.get(1)).compareTo(requiredDuration) >= 0;
    }

    /**
     * Formats a LocalDateTime into the required format for the Graph API filter.
     */
    private String formatDate(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
