package org.eclipse.gef4.graph.io.graphml.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;


public class Attribute {
	private String id;
	
	@XmlValue
	private String value;

	public Attribute() {
		value = "";
	}
	
	public Attribute(String key, String val) {
		this.id = key;
		this.value = val;
	}
	
	@XmlAttribute(name="key")
	public String getKeyId() {
		return id;
	}
	public void setKeyId(String keyId) {
		this.id = keyId;
	}
}
