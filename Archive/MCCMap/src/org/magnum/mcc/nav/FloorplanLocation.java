/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.nav;

public class FloorplanLocation {
	private String id;
	private String type;

	public FloorplanLocation() {
	}
	
	public FloorplanLocation(String locId, String locType) {
		id = locId;
		type = locType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
