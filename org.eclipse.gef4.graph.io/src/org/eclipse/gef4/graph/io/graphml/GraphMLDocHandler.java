package org.eclipse.gef4.graph.io.graphml;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.gef4.graph.Edge;
import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.Node;
import org.eclipse.gef4.graph.io.graphml.model.Attribute;
import org.eclipse.gef4.graph.io.graphml.model.Key;
import org.eclipse.gef4.layout.algorithms.SpringLayoutAlgorithm;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GraphMLDocHandler  extends DefaultHandler  {
	public static final String EDGE_LABEL_CSS_STYLE = "edge-label-css-style";
	public static final String NODE_LABEL_CSS_STYLE = "node-label-css-style";
	public static final String ELEMENT_CSS_ID = "css-id";
	public static final String GRAPH_TYPE = "type";
	public static final String GRAPH_TYPE_DIRECTED = "directed";
	public static final String GRAPH_TYPE_UNDIRECTED = "undirected";
	public static final String GRAPH_LAYOUT_ALGORITHM = "layout";
	public static final String ELEMENT_LAYOUT_IRRELEVANT = "layoutIrrelevant";
	public static final Boolean ELEMENT_LAYOUT_IRRELEVANT_DEFAULT = false;

	private Map<String, Key> globalAttrMap;
	private Map<String, Class> attrTypesMap;
	private Stack<Node> nodeCtx;
	private Stack<Graph> graphCtx;
	private Stack<Edge> edgeCtx;
	private Stack<Key> keyCtx;
	private Stack<Attribute> atttributeCtx;
	
	private Graph rootGraph;
	private Map<String, Edge> edgeMap;
	private Map<String, Node> nodeMap;


	public GraphMLDocHandler() {
		nodeCtx 		= new Stack<Node>();
		graphCtx 		= new Stack<Graph>();
		edgeCtx 		= new Stack<Edge>();
		keyCtx 			= new Stack<Key>();
		atttributeCtx	= new Stack<Attribute>();
		edgeMap 		= new HashMap<String, Edge>();
		nodeMap 		= new HashMap<String, Node>();
		
		globalAttrMap 	= new HashMap<String, Key>();
		attrTypesMap 	= new HashMap<String, Class>();
		
		init();
	}
	
	private void init()
	{
		attrTypesMap.put("string", 	String.class);
		attrTypesMap.put("boolean", Boolean.class);
		attrTypesMap.put("bool", 	Boolean.class);
		attrTypesMap.put("integer", Integer.class);
		attrTypesMap.put("int", 	Integer.class);
		attrTypesMap.put("long", 	Long.class);
		attrTypesMap.put("float", 	Float.class);
		attrTypesMap.put("double", 	Double.class);
	}
	
	private Class getAttrClassFromName(String type2find)
	{
		return attrTypesMap.get(type2find);
	}
	
	private Object initAttr(String typeName)
	{
		Class class2Compute = getAttrClassFromName(typeName);
		switch (class2Compute.getName())
		{
			case "java.lang.Boolean":
				return new Boolean(true);
			case "java.lang.Integer":
				return new Integer(0);
			case "java.lang.String":
				return "";
			case "java.lang.Float":
				return new Float(0);
			case "java.lang.Double":
				return new Double(0);
			case "java.lang.Long":
				return new Long(0);
			default:
				// Error
				break;
		}
		return null;
	}
	
	private Object computeAttrValue(String typeName, String value)
	{
		Class class2Compute = getAttrClassFromName(typeName);
		switch (class2Compute.getName())
		{
			case "java.lang.Boolean":
				return Boolean.valueOf(value);
			case "java.lang.Integer":
				return Integer.valueOf(value);
			case "java.lang.String":
				return value;
			case "java.lang.Float":
				return Float.valueOf(value);
			case "java.lang.Double":
				return Double.valueOf(value);
			case "java.lang.Long":
				return Long.valueOf(value);
			default:
				// Error
				break;
		}
		return null;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
	    String cdata = new String(ch, start, length);
	    if (!keyCtx.empty())
	    {
	    	cdata = cdata.trim();
	    	if (cdata != null && !"".equals(cdata) && cdata != "\\\n"){
	    		Object defaultValue = computeAttrValue(keyCtx.peek().getAttrType(), cdata);
	    		keyCtx.peek().setDefault(defaultValue.toString());
	    	}
	    }
	    if (!atttributeCtx.empty())
	    {
	    	cdata = cdata.trim();
	    	if (cdata != null && !"".equals(cdata) && cdata != "\\\n")
	    	{
		    	for (String key : globalAttrMap.keySet())
		    	{
		    		if (key.equals(atttributeCtx.peek().getKeyId()))
		    		{
		    			Key keyData = globalAttrMap.get(key);
	
		    			if (!nodeCtx.isEmpty() && keyData.getForType().equals(GraphMLTags.GRAPHML_NODE))
		    			{
		    				nodeCtx.peek().getAttrs().put(key, cdata);
		    			}
		    			if (!edgeCtx.isEmpty() && keyData.getForType().equals(GraphMLTags.GRAPHML_EDGE))
		    			{
		    				edgeCtx.peek().getAttrs().put(key, cdata);
		    			}
		    		}
		    	}
		    }
	    }
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		switch (qName)
		{
			case GraphMLTags.GRAPHML_NODE_DATA:
				String keyDataId = (String) attributes.getValue("key");
				Attribute attr = new Attribute();
				attr.setKeyId(keyDataId);
				atttributeCtx.push(attr);
				break;
			case GraphMLTags.GRAPHML_KEY:
				Object keyId = attributes.getValue("id");
				Object forAttr = attributes.getValue("for");
				Object nameAttr = attributes.getValue("attr.name");
				Object typeAttr = attributes.getValue("attr.type");
				
				if (nameAttr instanceof String && typeAttr instanceof String)
				{
					Key key = new Key((String)keyId, (String)forAttr, (String)nameAttr, (String)typeAttr);
					key.setDefault(null);
					globalAttrMap.put((String)keyId, key);
					keyCtx.push(key);
				}
				
				break;
			case GraphMLTags.GRAPHML_NODE:
				String nid = attributes.getValue("id");
				
				Node node = new Node();
				node.getAttrs().put("id", nid);
				node.getAttrs().put("label", nid);
				node.getAttrs().put(NODE_LABEL_CSS_STYLE, nid);
				node.getAttrs().put(ELEMENT_CSS_ID, nid);
				node.setGraph(graphCtx.peek());
				node.getAttrs().put(ELEMENT_LAYOUT_IRRELEVANT,  ELEMENT_LAYOUT_IRRELEVANT_DEFAULT);

				nodeMap.put(nid, node);
				graphCtx.peek().getNodes().add(node);
				nodeCtx.push(node);
				break;
			case GraphMLTags.GRAPHML_EDGE:
				String eid = attributes.getValue("id");
				
				String sourceId = attributes.getValue("source");
				Node sourceNode = nodeMap.get(sourceId);
				sourceNode.getAttrs().put("id", sourceId);

				String targetId = attributes.getValue("target");
				Node targetNode = nodeMap.get(targetId);
				targetNode.getAttrs().put("id", targetId);
				
				Edge edge = new Edge(sourceNode, targetNode);
				graphCtx.peek().getEdges().add(edge);
				edge.setGraph(graphCtx.peek());
				edge.getAttrs().put("label", sourceId + " - " + targetId);
				edge.getAttrs().put(EDGE_LABEL_CSS_STYLE, sourceId + " - " + targetId);
				edge.getAttrs().put(ELEMENT_CSS_ID, sourceId + " - " + targetId);
				edge.getAttrs().put(ELEMENT_LAYOUT_IRRELEVANT,  ELEMENT_LAYOUT_IRRELEVANT_DEFAULT);
				
				if (eid != null)
				{
					edgeMap.put(eid, edge);
					edge.getAttrs().put("id", eid);
				}

				edgeCtx.push(edge);
				break;
			case GraphMLTags.GRAPHML_GRAPH:
				String gid = attributes.getValue("id");
				String edgedefault = attributes.getValue("edgedefault");
				
				Graph graph = new Graph();
				graph.getAttrs().put("edgedefault", edgedefault);
				graph.getAttrs().put("id", gid);
				graph.getAttrs().put("label", gid);
				graph.getAttrs().put(GRAPH_TYPE, GRAPH_TYPE_DIRECTED);
				graph.getAttrs().put(GRAPH_LAYOUT_ALGORITHM, new SpringLayoutAlgorithm());

				if (!nodeCtx.isEmpty())
				{
					nodeCtx.peek().setNestedGraph(graph);
					graph.setNestingNode(nodeCtx.peek());
				}
				graphCtx.push(graph);
				break;
			case GraphMLTags.GRAPHML_GRAPHML:
				break;
			default:
				break;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName)
		{
			case GraphMLTags.GRAPHML_KEY:
				keyCtx.pop();
				break;
			case GraphMLTags.GRAPHML_NODE:
				nodeCtx.pop();
				break;
			case GraphMLTags.GRAPHML_EDGE:
				edgeCtx.pop();
				break;
			case GraphMLTags.GRAPHML_GRAPH:
				if (graphCtx.size()==1)
				{
					rootGraph = graphCtx.peek();
				}
				graphCtx.pop();
				break;
			case GraphMLTags.GRAPHML_GRAPHML:
				break;
			default:
				break;
		}
		super.endElement(uri, localName, qName);
	}
	public Graph getGraph()
	{
		return rootGraph;
	}
	public Map<String, Key> getGlobalAttrMap() {
		return globalAttrMap;
	}
}
