/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.events;

import java.util.UUID;


public class Event {

	private String id;	
	private String name;
	private String description;
	private String day;
	private String month;
	private String year;
	private String startTime;

	private String endTime;

	private String floorplanId;

	private String floorplanLocationId;
	
	private String conference;

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

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFloorplanId() {
		return floorplanId;
	}

	public void setFloorplanId(String floorplanId) {
		this.floorplanId = floorplanId;
	}

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}
}
