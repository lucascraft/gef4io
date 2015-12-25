package org.eclipse.gef4.graph.io.graphml.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="node")
public class Node {
	@XmlAttribute(name="id")
	@XmlID
	private String nid;
	@XmlElement(name="graph")
	private Graph nestedGraph;	
	private List<Attribute> ndata; 
	public void setData(List<Attribute> data) {
		this.ndata = data;
	}
	public List<Attribute> getData() {
		return ndata;
	}
	public Node(String id) {
		nid = id;
	}	
	public Node(String id, Graph graph) {
		nid = id;
		nestedGraph = graph;
	}	
	public Node() {
	}
}
