package org.eclipse.gef4.graph.io.graphml.model;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlType(name="key")
public class Key {
	private String keyId;
	private String fType;
	private String aName;
	private String aType;
	
	private String value;

	public String getDefault() {
		return value;
	}
	public void setDefault(String defaultValue) {
		this.value = defaultValue;
	}
	public Key() {
	}
	public Key(String id, String forType, String attrName, String attrType) {
		this.keyId = id;
		this.fType = forType;
		this.aName = attrName;
		this.aType = attrType;
	}
	public String getForType() {
		return fType;
	}
	@XmlAttribute(name="id")
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	@XmlAttribute(name="for")
	public void setForType(String forType) {
		this.fType = forType;
	}
	@XmlAttribute(name="attr.name")
	public String getAttrName() {
		return aName;
	}
	public void setAttrName(String attrName) {
		this.aName = attrName;
	}
	@XmlAttribute(name="attr.type")
	public String getAttrType() {
		return aType;
	}
	public void setAttrType(String attrType) {
		this.aType = attrType;
	}
}
