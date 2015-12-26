package org.eclipse.gef4.graph.io.app;
/*******************************************************************************
 * Copyright (c) 2014, 2015 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API & implementation
 *
 * Note: Parts of this class have been transferred from org.eclipse.gef4.zest.examples.layout.RadialLayoutExample
 *
 *******************************************************************************/

import java.io.File;

import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.io.graphml.GraphMLParser;
import org.eclipse.gef4.graph.io.graphml.model.GraphMLMarshaller;
import org.eclipse.gef4.layout.algorithms.SpringLayoutAlgorithm;
import org.eclipse.gef4.zest.examples.AbstractZestExample;
import org.eclipse.gef4.zest.fx.ZestProperties;

import javafx.application.Application;

public class GraphMLSpringLayoutExample extends AbstractZestExample {

	private Graph graph;
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	public GraphMLSpringLayoutExample() {
		super("GEF4 Layouts - Spring Layout Example");
	}

	@Override
	protected Graph createGraph() {

		GraphMLParser nestedGraphMLParser = new GraphMLParser(new File("samples\\nested.xml"));
		graph = nestedGraphMLParser.load();

		graph.getAttrs().put(ZestProperties.GRAPH_TYPE, ZestProperties.GRAPH_TYPE_DIRECTED);
		graph.getAttrs().put(ZestProperties.GRAPH_LAYOUT_ALGORITHM, new SpringLayoutAlgorithm());

		return graph;

	}

	@Override
	public void stop() throws Exception {
		GraphMLMarshaller.marshall(graph, new File("samples\\nested.xml"));
		super.stop();
	}
}
