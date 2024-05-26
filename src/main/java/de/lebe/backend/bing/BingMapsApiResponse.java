package de.lebe.backend.bing;

import java.util.List;

public record BingMapsApiResponse(
        String authenticationResultCode,
        String brandLogoUri,
        String copyright,
        List<ResourceSet> resourceSets,
        int statusCode,
        String statusDescription,
        String traceId
) {
    public record ResourceSet(
            int estimatedTotal,
            List<Resource> resources
    ) {
        public record Resource(
                String type,
                List<Double> bbox,
                String name,
                Point point,
                Address address,
                String confidence,
                String entityType,
                List<GeocodePoint> geocodePoints,
                List<String> matchCodes
        ) {
        }

        public record Point(
                String type,
                List<Double> coordinates
        ) {
        }

        public record Address(
                String addressLine,
                String adminDistrict,
                String adminDistrict2,
                String countryRegion,
                String formattedAddress,
                String locality,
                String postalCode
        ) {
        }

        public record GeocodePoint(
                String type,
                List<Double> coordinates,
                String calculationMethod,
                List<String> usageTypes
        ) {
        }
    }
}
