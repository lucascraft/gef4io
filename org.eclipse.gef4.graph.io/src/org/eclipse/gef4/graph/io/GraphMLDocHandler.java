package org.eclipse.gef4.graph.io;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.runtime.Platform;
import org.eclipse.gef4.graph.Edge;
import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GraphMLDocHandler  extends DefaultHandler  {
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
	private String fileLocation = "";


	public GraphMLDocHandler(String fileLocation) {
		nodeCtx 		= new Stack<Node>();
		graphCtx 		= new Stack<Graph>();
		edgeCtx 		= new Stack<Edge>();
		keyCtx 			= new Stack<Key>();
		atttributeCtx	= new Stack<Attribute>();
		edgeMap 		= new HashMap<String, Edge>();
		nodeMap 		= new HashMap<String, Node>();
		
		globalAttrMap 	= new HashMap<String, Key>();
		attrTypesMap 	= new HashMap<String, Class>();
		
		this.fileLocation = fileLocation;
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
	public void startDocument() throws SAXException {
		super.startDocument();
		
		System.out.println("----------------------------------------------");
		System.out.println(fileLocation);
		System.out.println("----------------------------------------------");
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	    String cdata = new String(ch, start, length);
	    if (!keyCtx.empty())
	    {
	    	cdata = cdata.trim();
	    	if (cdata != null && !"".equals(cdata) && cdata != "\\\n"){
	    		Object defaultValue = computeAttrValue(keyCtx.peek().getType(), cdata);
	    		keyCtx.peek().setDefaultValue(defaultValue);
	    	}
	    }
	    if (!atttributeCtx.empty())
	    {
	    	cdata = cdata.trim();
	    	if (cdata != null && !"".equals(cdata) && cdata != "\\\n")
	    	{
		    	for (Key kGlobal : globalAttrMap.values())
		    	{
		    		if (kGlobal.getId().equals(atttributeCtx.peek().getKeyId()))
		    		{
		    			Key keyData = globalAttrMap.get(kGlobal.getId());
	
		    			if (!nodeCtx.isEmpty() && keyData.getForType().equals("node"))
		    			{
		    				nodeCtx.peek().getAttrs().put(keyData.getId(), cdata);
		    			}
		    			if (!edgeCtx.isEmpty() && keyData.getForType().equals("edge"))
		    			{
		    				edgeCtx.peek().getAttrs().put(keyData.getId(), cdata);
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
			case GraphMLTags.GRAPHML_NODEDATA:
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
					Key key = new Key();
					key.setDefaultValue(null);
					key.setForType((String)forAttr);
					key.setId((String)keyId);
					key.setType((String)typeAttr);
					key.setName((String)nameAttr);
					globalAttrMap.put((String)keyId, key);
					keyCtx.push(key);
				}
				
				break;
			case GraphMLTags.GRAPHML_NODE:
				String nid = attributes.getValue("id");
				
				Node node = new Node();
				node.getAttrs().put("id", nid);
				node.setGraph(graphCtx.peek());
				
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
				
				if (!nodeCtx.isEmpty())
				{
					nodeCtx.peek().setNestedGraph(graph);
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
			case GraphMLTags.GRAPHML_ATTRIBUTE:
				atttributeCtx.pop();
				break;
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
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		if (Platform.inDebugMode())
		{
			System.out.println("<graphml>");
			for (Key k : globalAttrMap.values())
			{
				if (k.getDefaultValue() == null)
				{
					System.out.println("    <key id='" + k.getId() +"' for='" + k.getForType() +"' attr.name='" + k.getName() +"' attr.type='" + k.getType() + "' />");
				}
				else
				{
					System.out.println("    <key id='" + k.getId() +"' for='" + k.getForType() +"' attr.name='" + k.getName() +"' attr.type='" + k.getType() + "'>");
					System.out.println("        <default>" + k.getDefaultValue() + "</default>");
					System.out.println("    </key>");
				}
			}
			dumpGraphAsGraphMLText(rootGraph, 1);
			System.out.println("</graphml>");
		}
	}
	
	private void dumpGraphAsGraphMLText(Graph graph, int lvl)
	{
		String indent = "";
		for (int l=0; l<lvl;l++)
		{
			indent += "    ";
		}
		System.out.println(indent + "<graph id='" + graph.getAttrs().get("id") + "'>");
		for (Node node : graph.getNodes())
		{
			boolean nested = node.getNestedGraph() != null;
			String data = "";
			if (nested)
			{
				System.out.println(indent + "    <node id='"+node.getAttrs().get("id") + "'>");
				dumpGraphAsGraphMLText(node.getNestedGraph(), lvl+2);
			}
			else 
			{
				for (String k : node.getAttrs().keySet())
				{
					if (!"id".equals(k))
					{
						data += indent + "        <data key='" + k + "'>" + node.getAttrs().get(k) + "</data>";
					}
				}

				System.out.println(indent + "    <node id='"+node.getAttrs().get("id") + "'"+ ((nested || !"".equals(data))?"":"/") + ">");
				
				if (!"".equals(data))
				{
					System.out.println(data);
				}
			}
			if (nested || !"".equals(data))
			{
				System.out.println(indent + "    </node>");
			}
		}
		for (Edge edge: graph.getEdges())
		{
			String sourceAttr = "source='"+ edge.getSource().getAttrs().get("id") +"'";
			String targetAttr = "target='"+ edge.getTarget().getAttrs().get("id") +"'";
			String idAttr = "";
			if (edge.getAttrs().get("id") != null)
			{
				idAttr = "id='"+ edge.getAttrs().get("id") +"'";
			}
			
			System.out.println(indent + "    <edge " + idAttr+ " " + sourceAttr + " " + targetAttr + ">");
			for (String k : edge.getAttrs().keySet())
			{
				if (!"id".equals(k))
				{
					System.out.println(indent + "        <data key='" + k + "'>" + edge.getAttrs().get(k) + "</data>" );
				}
			}
			System.out.println(indent + "    </edge>");
			
		}
		System.out.println(indent + "</graph>");
	}
}
