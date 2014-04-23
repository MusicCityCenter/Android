/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.nav;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class FloorplanNavigationData {

	private Floorplan floorplan;
	private FloorplanImageMapping mapping;
	
	public FloorplanNavigationData(Floorplan fp, FloorplanImageMapping map) {
		floorplan = fp;
		mapping = map;
	}

	public Floorplan getFloorplan() {
		return floorplan;
	}

	public void setFloorplan(Floorplan floorplan) {
		this.floorplan = floorplan;
	}

	public FloorplanImageMapping getMapping() {
		return mapping;
	}

	public void setMapping(FloorplanImageMapping mapping) {
		this.mapping = mapping;
	}

}
