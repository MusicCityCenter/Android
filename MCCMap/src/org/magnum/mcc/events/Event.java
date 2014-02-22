/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.events;

public class Event {

	private String id;
	private String name;
	private String description;
	private String floorplanLocationId;

	// I should be changed to some specific type
	// ... probably from the JodaTime library.
	private String time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFloorplanLocationId() {
		return floorplanLocationId;
	}

	public void setFloorplanLocationId(String floorplanLocationId) {
		this.floorplanLocationId = floorplanLocationId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
