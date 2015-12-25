package org.eclipse.gef4.graph.io.tests;

import java.io.File;

import org.eclipse.gef4.graph.io.graphml.GraphMLParser;

public class TestLoadGraphML {
	public static void main(String[] args) {
		GraphMLParser simpleGraphMLParser = new GraphMLParser(new File("samples\\simple.xml"));
		simpleGraphMLParser.load();
		
		GraphMLParser hyperGraphMLParser = new GraphMLParser(new File("samples\\hyper.xml"));
		hyperGraphMLParser.load();
		
		GraphMLParser portGraphMLParser = new GraphMLParser(new File("samples\\port.xml"));
		portGraphMLParser.load();
		
		GraphMLParser nestedGraphMLParser = new GraphMLParser(new File("samples\\nested.xml"));
		nestedGraphMLParser.load();
		
		GraphMLParser attributesGraphMLParser = new GraphMLParser(new File("samples\\attributes.xml"));
		attributesGraphMLParser.load();
	}
}
