package de.lebe.backend.sevdesk.model;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public abstract class BaseModel {

	private Integer id;
	private ZonedDateTime created;
	private ZonedDateTime update;
}
