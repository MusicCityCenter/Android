/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.nav;


import java.util.HashSet;
import java.util.Set;

public class Floorplan {

	private Set<FloorplanLocation> locations = new HashSet<FloorplanLocation>();

	private Set<FloorplanEdge> edges = new HashSet<FloorplanEdge>();

	public Set<FloorplanLocation> getLocations() {
		return locations;
	}

	public void setLocations(Set<FloorplanLocation> locations) {
		this.locations = locations;
	}

	public Set<FloorplanEdge> getEdges() {
		return edges;
	}

	public void setEdges(Set<FloorplanEdge> edges) {
		this.edges = edges;
	}

}
