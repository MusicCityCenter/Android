/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.nav;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Floorplan {

	/*
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
	}*/
	
	private Map<String, FloorplanLocation> locations = new HashMap<String, FloorplanLocation>();

	private List<FloorplanEdge> edges = new ArrayList<FloorplanEdge>();
	
	public Floorplan(Map<String, FloorplanLocation> map, List<FloorplanEdge> list) {
		locations = map;
		edges = list;
	}

	public Map<String, FloorplanLocation> getLocations() {
		return locations;
	}

	public void setLocations(Map<String, FloorplanLocation> locations) {
		this.locations = locations;
	}

	public List<FloorplanEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<FloorplanEdge> edges) {
		this.edges = edges;
	}


}
