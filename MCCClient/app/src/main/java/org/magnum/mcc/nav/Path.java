/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.nav;

import java.util.List;

/**
 * This class represents a simple navigation path as a series
 * of graph edges. This class can be combined with FloorplanImageMapping
 * to render a navigation path on a floor plan image.
 * 
 * @author jules
 *
 */
public class Path {

	private List<FloorplanEdge> edges_;
	
	public Path(List<FloorplanEdge> edges) {
		edges_ = edges;
	}

	public List<FloorplanEdge> getEdges() {
		return edges_;
	}

	public void setEdges(List<FloorplanEdge> edges) {
		edges_ = edges;
	}

}
