package org.eclipse.gef4.graph.io.graphml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
/**
 * <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
     http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
 * @author LBI
 *
 */
@XmlRootElement(name="graphml")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlType(propOrder={"keys", "root"})
public class GraphML {
	@XmlElement(name="graph")
	private Graph root;
	private List<Key> keys;
	
	public GraphML() {
		keys = new ArrayList<Key>();
	}
	public GraphML(Graph root) {
		this.root = root;
	}
	public void setKeys(List<Key> keys) {
		this.keys = keys;
	}
	@XmlElement(name="key")
	public List<Key> getKeys() {
		return keys;
	}
}
