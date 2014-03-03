/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.nav;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains a mapping of FloorplanLocations to specific x,y coordinates
 * on the image stored in imageUrl. This class can be used to create a simple map
 * display to render a navigation path on top of the provided image.
 * 
 * @author jules
 *
 */
public class FloorplanImageMapping {

	private String imageUrl;
	private Map<String, Coord> mapping = new HashMap<String, Coord>();
	
	public FloorplanImageMapping(String url, Map<String, Coord> map) {
		imageUrl = url;
		mapping = map;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public Coord getImageLocation(FloorplanLocation loc){
		return mapping.get(loc.getId());
	}

	public Map<String, Coord> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, Coord> mapping) {
		this.mapping = mapping;
	}

}
