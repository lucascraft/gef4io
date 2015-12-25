package org.eclipse.gef4.graph.io.graphml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.gef4.graph.Graph;
import org.xml.sax.SAXException;

public class GraphMLParser {
	private GraphMLDocHandler _optHandler;
	private File _file;
	
	public GraphMLParser(File file) {
		_file = file;
	}

	public Graph load()
	{
		try
		{
			_optHandler = new GraphMLDocHandler();
			javax.xml.parsers.SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new FileInputStream(_file), _optHandler);
			return _optHandler.getGraph();
		}
		catch (IllegalArgumentException  | ParserConfigurationException
			| IOException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}

		return null;
	}

}
