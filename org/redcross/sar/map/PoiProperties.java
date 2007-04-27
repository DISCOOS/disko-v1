package org.redcross.sar.map;

import org.redcross.sar.mso.data.IPOIIf.POIType;

public class PoiProperties {
	
	private String name = null;
	private String desrciption = null;
	private POIType type = null;
	
	public String getDesrciption() {
		return desrciption;
	}
	public void setDesrciption(String desrciption) {
		this.desrciption = desrciption;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public POIType getType() {
		return type;
	}
	public void setType(POIType type) {
		this.type = type;
	}
}
