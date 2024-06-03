package de.lebe.backend.bing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import de.lebe.backend.bing.BingMapsApiResponse.ResourceSet.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AddressBingService {

	@Value("${solar.order.maps.client.url}")
	private String bingApiUrl;

	@Value("${solar.order.maps.client.key}")
	private String bingApiKey;

	/**
	 * Retrieve a formatted address based on BING API. In case there is no match the
	 * image is null and the input is parameters are used.
	 * 
	 * @param streetWithHnr street with hnr.
	 * @param city          city
	 * @param postalCode    postal code
	 * @return {@link MAddressFormatted}.
	 */
	public MAddressFormatted formatAddress(String streetWithHnr, String city, String postalCode) {

		var match = findFirstByAddress(streetWithHnr, city, postalCode);

		byte[] image = null;

		if (match != null) {

			image = retrieveAerial(match.point().coordinates().get(0), match.point().coordinates().get(1));

			return MAddressFormatted.builder().addressLine(match.address().addressLine())
					.adminDistrict(match.address().adminDistrict()).adminDistrict2(match.address().adminDistrict2())
					.countryRegion(match.address().countryRegion()).formattedAddress(match.address().formattedAddress())
					.locality(match.address().locality()).postalCode(match.address().postalCode()).image(image)
					.qualified(true).build();
		}

		return MAddressFormatted.builder().formattedAddress(streetWithHnr + ", " + postalCode + " " + city)
				.locality(city).postalCode(postalCode).addressLine(streetWithHnr).qualified(false).build();
	}

	/**
	 * Get the image of the point from bing maps.
	 * 
	 * @param latitude  of the house
	 * @param longitude of the house
	 * @return image as blob
	 */
	public byte[] retrieveAerial(double latitude, double longitude) {
		// Zoom-Level
		int zoomLevel = 20;

		// Größe der Karte
		int mapWidth = 350;
		int mapHeight = 350;

		// URL für die Anfrage zusammenstellen
		String apiUrl = String.format(bingApiUrl + "/Imagery/Map/Aerial/%s,%s/%d?mapSize=%d,%d&format=jpeg&key=%s",
				latitude, longitude, zoomLevel, mapWidth, mapHeight, bingApiKey);

		// RestTemplate erstellen
		RestTemplate restTemplate = new RestTemplate();

		// GET-Anfrage durchführen
		ResponseEntity<byte[]> response = restTemplate.getForEntity(apiUrl, byte[].class);

		// Überprüfen, ob die Anfrage erfolgreich war (Statuscode 200)
		if (response.getStatusCode().is2xxSuccessful()) {
			// Die Antwort als Byte-Array erhalten
			return response.getBody();

			// Hier können Sie mit dem Bild machen, was Sie möchten (speichern, anzeigen,
			// usw.)
			// Beispiel: Speichern des Bilds als Datei
			// saveImageToFile(imageBytes, "mapImage.jpg");
		} else {
			log.error("Fehler bei der Anfrage: " + response.getStatusCode());
		}

		return null;
	}

	/**
	 * Resolve the address to an bing api match.
	 * 
	 * @param streetWithHnr
	 * @param city
	 * @param postalCode
	 * @return
	 */
	public Resource findFirstByAddress(String streetWithHnr, String city, String postalCode) {
		try {
			// URL für die Anfrage zusammenstellen
			String apiUrl = bingApiUrl + "/Locations/DE/" + postalCode + "/" + city + "/"
					+ encodeUriComponent(streetWithHnr) + "?key=" + bingApiKey;

			// RestTemplate erstellen
			RestTemplate restTemplate = new RestTemplate();

			// GET-Anfrage durchführen
			ResponseEntity<BingMapsApiResponse> response = restTemplate.getForEntity(apiUrl, BingMapsApiResponse.class);

			// Überprüfen, ob die Anfrage erfolgreich war (Statuscode 200)
			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				// Die Antwort verarbeiten
				BingMapsApiResponse responseBody = response.getBody();

				var result = responseBody.resourceSets().stream().findAny();

				if (!result.isEmpty() && result.get().estimatedTotal() > 0) {
					return result.get().resources().get(0);
				}

			} else {
				log.error("Fehler bei der Anfrage: " + response.getStatusCode());

			}
		} catch (Exception ex) {
			log.error("Fehler bei der Anfrage: " + ex.getMessage());
		}

		return null;
	}

	private static String encodeUriComponent(String component) {
		return component.replaceAll("[^a-zA-Z0-9 ]", "");
	}
}
