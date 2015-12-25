package org.eclipse.gef4.graph.io;

public class Attribute {
	private String keyId;
	private Key keyValue;
	public String getKeyId() {
		return keyId;
	}
	public Key getKeyValue() {
		return keyValue;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	public void setKeyValue(Key keyValue) {
		this.keyValue = keyValue;
	}
}
