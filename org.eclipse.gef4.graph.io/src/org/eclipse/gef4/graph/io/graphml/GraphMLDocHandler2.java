package org.eclipse.gef4.graph.io.graphml;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.gef4.graph.Edge;
import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.Graph.Builder;
import org.eclipse.gef4.graph.Node;
import org.eclipse.gef4.graph.io.graphml.model.Attribute;
import org.eclipse.gef4.graph.io.graphml.model.Key;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GraphMLDocHandler2  extends DefaultHandler  {
	private final static String ID = "id";
	
	private Map<String, Key> globalAttrMap;
	private Map<String, Class> attrTypesMap;
	private Stack<Node> nodeCtx;
	private Stack<Graph.Builder> graphCtx;
	private Stack<Edge> edgeCtx;
	private Stack<Key> keyCtx;
	private Stack<Attribute> atttributeCtx;
	private Map<String, Node> nodesMap;
	
	private Graph rootGraph;
	private String fileLocation = "";
		
	private Builder graphBuilder;
	
	public GraphMLDocHandler2(String fileLocation) {
		nodeCtx 		= new Stack<Node>();
		graphCtx 		= new Stack<Graph.Builder>();
		edgeCtx 		= new Stack<Edge>();
		keyCtx 			= new Stack<Key>();
		atttributeCtx	= new Stack<Attribute>();
		nodesMap		= new HashMap<String, Node>();
		globalAttrMap 	= new HashMap<String, Key>();
		attrTypesMap 	= new HashMap<String, Class>();
		
		graphBuilder = new Graph.Builder();
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
		// TODO Auto-generated method stub
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
	
		    			if (!nodeCtx.isEmpty() && keyData.getForType().equals("node"))
		    			{
		    				nodeCtx.peek().getAttrs().put(key, cdata);
		    			}
		    			if (!edgeCtx.isEmpty() && keyData.getForType().equals("edge"))
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
		// TODO Auto-generated method stub
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
				//System.out.println("<node>");
				String nid = attributes.getValue("id");
				Node node = graphCtx.peek().node(nid).attr(ID, nid).buildNode();
				nodesMap.put(nid,  node);
				graphCtx.peek().build().getNodes().add(node);
				nodeCtx.push(node);
				break;
			case GraphMLTags.GRAPHML_EDGE:
				//System.out.println("<edge>");
				String eid = attributes.getValue("id");
				String sourceId = attributes.getValue("source");
				String targetId = attributes.getValue("target");
				Edge edge = graphCtx.peek().edge(nodesMap.get(sourceId), nodesMap.get(targetId)).attr(ID, eid).buildEdge();
				graphCtx.peek().build().getEdges().add(edge);
				edgeCtx.push(edge);
				break;
			case GraphMLTags.GRAPHML_GRAPH:
				//System.out.println("<graph>");
				String gid = attributes.getValue("id");
				String edgedefault = attributes.getValue("edgedefault");
				graphCtx.push(new Graph.Builder().attr("edgedefault", edgedefault).attr(ID, gid));
				Graph curGraph = graphCtx.peek().build();
				
				if (!nodeCtx.isEmpty())
				{
					nodeCtx.peek().setNestedGraph(curGraph);
				}
			break;
			case GraphMLTags.GRAPHML_GRAPHML:
				//System.out.println("<graphml>");
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
				//System.out.println("</node>");
				nodeCtx.pop();
				break;
			case GraphMLTags.GRAPHML_EDGE:
				//System.out.println("</edge>");
				edgeCtx.pop();
				break;
			case GraphMLTags.GRAPHML_GRAPH:
				//System.out.println("</graph>");
				
				Graph graph = graphCtx.peek().build();
				if (graphCtx.size()==1)
				{
					rootGraph = graph;
				}
				graphCtx.pop();
				break;
			case GraphMLTags.GRAPHML_GRAPHML:
				//System.out.println("</graphml>");
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
}
