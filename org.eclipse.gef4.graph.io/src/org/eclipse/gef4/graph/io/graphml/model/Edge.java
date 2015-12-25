package org.eclipse.gef4.graph.io.graphml.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

public class Edge {
	@XmlAttribute(name="id")
	private String eid;
	@XmlIDREF
	@XmlAttribute
	private Node source;
	@XmlIDREF
	@XmlAttribute
	private Node target;
	private List<Attribute> edata; 
	public Edge(String id, Node source, Node target) {
		this.eid = id;
		this.source = source;
		this.target = target;
	}	
	public void setData(List<Attribute> data) {
		this.edata = data;
	}
	public List<Attribute> getData() {
		return edata;
	}
}
