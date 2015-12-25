package org.eclipse.gef4.graph.io;

public class Key {
	private String id;
	private String forType;
	private String name;
	private String type;
	private Object defaultValue;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getForType() {
		return forType;
	}
	public void setForType(String forType) {
		this.forType = forType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
}
