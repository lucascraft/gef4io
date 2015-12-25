package org.eclipse.gef4.graph.io.tests;

import java.io.File;
import java.util.List;

import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.io.graphml.GraphMLParser;
import org.eclipse.gef4.graph.io.graphml.model.GraphML;
import org.eclipse.gef4.graph.io.graphml.model.GraphMLAdapter;
import org.eclipse.gef4.graph.io.graphml.model.GraphMLMarshaller;
import org.eclipse.gef4.graph.io.graphml.model.Key;
import org.junit.Assert;
import org.junit.Test;


public class GraphMLMarshallerTests {

	@SuppressWarnings("unchecked")
	@Test
	public void testLoadAndMarshallSimple() {
		GraphMLParser simpleGraphMLParser = new GraphMLParser(new File("samples\\simple.xml"));
		Graph simpleGraph = simpleGraphMLParser.load();
		
		Assert.assertNotNull("Simple graph loaded from samples/simple.xml shouldn't have been null", simpleGraph);
		
		
		Assert.assertTrue("node 0 id should have been 'n0'", "n0".equals(simpleGraph.getNodes().get(0).getAttrs().get("id")));
		Assert.assertTrue("node 1 id should have been 'n1'", "n1".equals(simpleGraph.getNodes().get(1).getAttrs().get("id")));
		Assert.assertTrue("node 2 id should have been 'n2'", "n2".equals(simpleGraph.getNodes().get(2).getAttrs().get("id")));
		Assert.assertTrue("node 3 id should have been 'n3'", "n3".equals(simpleGraph.getNodes().get(3).getAttrs().get("id")));
		Assert.assertTrue("node 4 id should have been 'n4'", "n4".equals(simpleGraph.getNodes().get(4).getAttrs().get("id")));
		Assert.assertTrue("node 5 id should have been 'n5'", "n5".equals(simpleGraph.getNodes().get(5).getAttrs().get("id")));
		Assert.assertTrue("node 6 id should have been 'n6'", "n6".equals(simpleGraph.getNodes().get(6).getAttrs().get("id")));
		Assert.assertTrue("node 7 id should have been 'n7'", "n7".equals(simpleGraph.getNodes().get(7).getAttrs().get("id")));
		Assert.assertTrue("node 8 id should have been 'n8'", "n8".equals(simpleGraph.getNodes().get(8).getAttrs().get("id")));
		Assert.assertTrue("node 9 id should have been 'n9'", "n9".equals(simpleGraph.getNodes().get(9).getAttrs().get("id")));
		Assert.assertTrue("node 10 id should have been 'n10'", "n10".equals(simpleGraph.getNodes().get(10).getAttrs().get("id")));

		
		GraphMLMarshaller.marshall(simpleGraph, new File("samples\\gen_simple.xml"));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadAndMarshallHyperGraph() {
		GraphMLParser hyperGraphMLParser = new GraphMLParser(new File("samples\\hyper.xml"));
		Graph hyperGraph = hyperGraphMLParser.load();
		
		Assert.assertNotNull("Hyper graph loaded from samples/hyper.xml shouldn't have been null", hyperGraph);

		GraphMLMarshaller.marshall(hyperGraph, new File("samples\\gen_hyper.xml"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadAndMarshallPortGraph() {
		GraphMLParser portGraphMLParser = new GraphMLParser(new File("samples\\port.xml"));
		Graph portGraph = portGraphMLParser.load();

		
		Assert.assertNotNull("Port graph loaded from samples/port.xml shouldn't have been null", portGraph);

		GraphMLMarshaller.marshall(portGraph, new File("samples\\gen_port.xml"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadAndMarshallNestedGraph() {
		GraphMLParser nestedGraphMLParser = new GraphMLParser(new File("samples\\nested.xml"));
		Graph nestedGraph = nestedGraphMLParser.load();

		
		Assert.assertNotNull("Nested graph loaded from samples/port.xml shouldn't have been null", nestedGraph);

		GraphMLMarshaller.marshall(nestedGraph, new File("samples\\gen_nested.xml"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadAndMarshallAttributeGraph() {
		GraphMLParser attributesGraphMLParser = new GraphMLParser(new File("samples\\attributes.xml"));
		Graph attributesGraph = attributesGraphMLParser.load();
		
		Assert.assertNotNull("Attribute graph loaded from samples/attribute.xml shouldn't have been null", attributesGraph);

		GraphMLAdapter graphMLAdapter = new GraphMLAdapter();
		GraphML graphML = graphMLAdapter.adaptGraphRoot(attributesGraph);
		List<Key> keys = attributesGraphMLParser.getKeys();
		graphML.setKeys(keys);

		GraphMLMarshaller.marshall(graphML, new File("samples\\gen_attributes.xml"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadAndMarshallSocialNetGraph() {
		GraphMLParser attributesGraphMLParser = new GraphMLParser(new File("samples\\socialnet.xml"));
		Graph attributesGraph = attributesGraphMLParser.load();
		
		Assert.assertNotNull("Attribute graph loaded from samples/attribute.xml shouldn't have been null", attributesGraph);

		GraphMLAdapter graphMLAdapter = new GraphMLAdapter();
		GraphML graphML = graphMLAdapter.adaptGraphRoot(attributesGraph);
		List<Key> keys = attributesGraphMLParser.getKeys();
		graphML.setKeys(keys);

		GraphMLMarshaller.marshall(graphML, new File("samples\\gen_socialnet.xml"));
	}
	
	
	
}
