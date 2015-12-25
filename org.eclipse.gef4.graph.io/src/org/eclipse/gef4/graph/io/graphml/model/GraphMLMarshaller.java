package org.eclipse.gef4.graph.io.graphml.model;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.gef4.graph.Graph;

public class GraphMLMarshaller {

	public static void marshall(Graph gef4Graph, File file) {
		GraphMLAdapter graphMLAdapter = new GraphMLAdapter();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(GraphML.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd");
			
			GraphML graphML = graphMLAdapter.adaptGraphRoot(gef4Graph);
			
			m.marshal(graphML, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
