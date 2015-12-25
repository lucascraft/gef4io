package org.eclipse.gef4.graph.io.graphml.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
     http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
 * @author LBI
 *
 */
@XmlRootElement(name="graphml" )
public class GraphML {
	@XmlElement(name="graph")
	private Graph root;
	public GraphML() {
	}
	public GraphML(Graph root) {
		this.root = root;
	}

}
