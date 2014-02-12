package com.example.wifisignalsample;

public class WifiConnectionBean{
	private String name;
	private String description;
	private String id;
	private String level;
	private String SignalStrength;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getSignalStrength() {
		return SignalStrength;
	}
	public void setSignalStrength(String signalStrength) {
		SignalStrength = signalStrength;
	}
	
}