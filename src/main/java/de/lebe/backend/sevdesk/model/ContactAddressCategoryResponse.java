package de.lebe.backend.sevdesk.model;

import java.time.ZonedDateTime;
import java.util.List;

public record ContactAddressCategoryResponse(List<Category> objects) {
    public record Category(
        int id,
        String objectName,
        String additionalInformation,
        ZonedDateTime create,
        ZonedDateTime update,
        String name,
        String objectType,
        String priority,
        String code,
        String color,
        String postingAccount,
        String type,
        String translationCode
    ) {}
}

