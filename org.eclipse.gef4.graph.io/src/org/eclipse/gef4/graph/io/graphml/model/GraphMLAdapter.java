package org.eclipse.gef4.graph.io.graphml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef4.graph.Edge;
import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.Node;

public class GraphMLAdapter {

	private Map<String, org.eclipse.gef4.graph.io.graphml.model.Node> nodesMap;
	
	public GraphMLAdapter() {
		nodesMap = new HashMap<String, org.eclipse.gef4.graph.io.graphml.model.Node>();
	}
	
	public GraphML adaptGraphRoot(Graph gef4Graph)
	{
		return new GraphML(adaptGraph(gef4Graph));
	}
	
	public org.eclipse.gef4.graph.io.graphml.model.Graph adaptGraph(Graph gef4Graph)
	{
		org.eclipse.gef4.graph.io.graphml.model.Graph graph = new org.eclipse.gef4.graph.io.graphml.model.Graph();
		graph.setId((String) gef4Graph.getAttrs().get("id"));
		String edgedefault = (String) gef4Graph.getAttrs().get("edgedefault");
		graph.setEdgedefault(edgedefault);
		
		for (Node gef4Node : gef4Graph.getNodes())
		{	
			org.eclipse.gef4.graph.io.graphml.model.Node graphmlNode = null;
			String nodeId = (String)gef4Node.getAttrs().get("id");
			if (gef4Node.getNestedGraph() != null)
			{
				graphmlNode = new org.eclipse.gef4.graph.io.graphml.model.Node(nodeId, adaptGraph(gef4Node.getNestedGraph()));
			}
			else
			{
				graphmlNode = new org.eclipse.gef4.graph.io.graphml.model.Node(nodeId);
			}
			graphmlNode.setData(computeAttributes(gef4Node.getAttrs(), Node.class));
			nodesMap.put(nodeId, graphmlNode);
			graph.getNode().add(graphmlNode);
		}
		
		for (org.eclipse.gef4.graph.Edge gef4Edge : gef4Graph.getEdges())
		{	
			String sourceId = (String)gef4Edge.getSource().getAttrs().get("id");
			String targetId = (String)gef4Edge.getTarget().getAttrs().get("id");
			String edgeId = (String)gef4Edge.getAttrs().get("id");
			org.eclipse.gef4.graph.io.graphml.model.Edge graphmlEdge = new org.eclipse.gef4.graph.io.graphml.model.Edge(edgeId, nodesMap.get(sourceId), nodesMap.get(targetId));
			graphmlEdge.setData(computeAttributes(gef4Edge.getAttrs(), Edge.class));
			graph.getEdge().add(graphmlEdge);
		}
		
		return graph;
	}
	
	private List<Attribute> computeAttributes(Map<String, Object> attrMap, Class type)
	{
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (String k : attrMap.keySet())
		{
			if (!"id".equals(k))
			{
				Attribute attr = new Attribute(k, attrMap.get(k).toString());
				
				attributes.add(attr);
			}
		}
		return attributes;
	}
//	public static org.eclipse.gef4.graph.io.graphml.model.Node adaptNode(Node gef4node)
//	{
//		gef4node.ge
//	}
}
