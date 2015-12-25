package org.eclipse.gef4.graph.io.graphml.model;

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
	public Edge(String id, Node source, Node target) {
		this.eid = id;
		this.source = source;
		this.target = target;
	}	
	
}
