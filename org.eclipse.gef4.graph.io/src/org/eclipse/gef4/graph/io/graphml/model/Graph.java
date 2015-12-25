package org.eclipse.gef4.graph.io.graphml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="graph")
public class Graph {
	@XmlAttribute(name="id")
	private String gid;
	@XmlAttribute
	private String edgedefault;
	private List<Node> node;
	private List<Edge> edge;
	public Graph() {
		node = new ArrayList<Node>();
		edge = new ArrayList<Edge>();
	}
	public String getId() {
		return gid;
	}
	public void setId(String id) {
		this.gid = id;
	}
	public String getEdgedefault() {
		return edgedefault;
	}
	public void setEdgedefault(String edgedefault) {
		this.edgedefault = edgedefault;
	}
	public List<Edge> getEdge() {
		return edge;
	}
	public List<Node> getNode() {
		return node;
	}
	public void setEdge(List<Edge> edges) {
		this.edge = edges;
	}
	public void setNode(List<Node> nodes) {
		this.node = nodes;
	}
}
