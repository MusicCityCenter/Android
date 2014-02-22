/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.nav;

public class FloorplanEdge {

	private FloorplanLocation start;
	private FloorplanLocation end;
	private double length;
	private double angle;

	public FloorplanEdge() {
	}

	public FloorplanLocation getStart() {
		return start;
	}

	public void setStart(FloorplanLocation start) {
		this.start = start;
	}

	public FloorplanLocation getEnd() {
		return end;
	}

	public void setEnd(FloorplanLocation end) {
		this.end = end;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

}
