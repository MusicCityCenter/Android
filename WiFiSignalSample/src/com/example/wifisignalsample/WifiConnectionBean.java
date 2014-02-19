package com.example.wifisignalsample;

public class WifiConnectionBean{
	private String name;
	private String description;
	private int SignalStrength;
	private String BSSID;
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
	public int getSignalStrength() {
		return SignalStrength;
	}
	public void setSignalStrength(int signalStrength) {
		SignalStrength = signalStrength;
	}
	public String getBSSID() {
		return BSSID;
	}
	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}
	
	
}