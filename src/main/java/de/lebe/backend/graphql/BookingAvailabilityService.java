package de.lebe.backend.graphql;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import de.lebe.backend.domain.TimeSlot;

public class BookingAvailabilityService {

    private final MicrosoftBookingClient bookingClient;

    public BookingAvailabilityService(MicrosoftBookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    public List<LocalDate> getAvailableDays() {
        // Erstelle eine Liste für verfügbare Tage
        List<LocalDate> availableDays = new ArrayList<>();

        // Erhalte den aktuellen Monat
        YearMonth currentMonth = YearMonth.now();

        // Iteriere durch alle Tage des Monats
        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            LocalDate currentDate = currentMonth.atDay(day);

            // Überprüfe, ob es für diesen Tag verfügbare Slots gibt
            if (isDayAvailable(currentDate)) {
                availableDays.add(currentDate);
            }
        }

        return availableDays;
    }

    private boolean isDayAvailable(LocalDate date) {
        // Abrufen aller Mitarbeiter-Kalender
        List<String> employeeCalendars = bookingClient.getEmployeeCalendars();

        for (String calendarId : employeeCalendars) {
            // Für jeden Mitarbeiter-Kalender verfügbare Slots abrufen
            List<TimeSlot> availableSlots = bookingClient.getAvailableSlots(calendarId, date);

            // Wenn mindestens ein Slot verfügbar ist, ist der Tag verfügbar
            if (!availableSlots.isEmpty()) {
                return true;
            }
        }

        return false;
    }
}