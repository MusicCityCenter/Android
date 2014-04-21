package org.magnum.mcc.events;

import java.util.Comparator;


public class EventCompare implements Comparator<Event> {
	@Override
	public int compare(Event e1, Event e2) {
		return Integer.valueOf(e1.getStartTime()) - Integer.valueOf(e2.getStartTime());
	}
}
