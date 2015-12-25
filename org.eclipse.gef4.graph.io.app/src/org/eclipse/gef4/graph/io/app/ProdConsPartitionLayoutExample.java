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
 * Note: Parts of this class have been transferred from org.eclipse.gef4.zest.examples.layout.CustomLayoutExample
 *
 *******************************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.gef4.graph.Edge;
import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.Node;
import org.eclipse.gef4.layout.ILayoutAlgorithm;
import org.eclipse.gef4.layout.ILayoutContext;
import org.eclipse.gef4.layout.INodeLayout;
import org.eclipse.gef4.layout.LayoutProperties;
import org.eclipse.gef4.zest.examples.AbstractZestExample;
import org.eclipse.gef4.zest.fx.ZestProperties;
import org.eclipse.gef4.zest.fx.layout.GraphNodeLayout;

import javafx.application.Application;

/**
 * This snippet shows how to create a custom layout. All the work is done in the
 * applyLayoutInternal Method.
 *
 * @author irbull
 * @authoer anyssen
 *
 */
public class ProdConsPartitionLayoutExample extends AbstractZestExample {

	public class Channel {
		private String name;
		private Node node;
		private Map<String, Node> producers;
		private Map<String, Node> consumers;

		public Channel() {
			producers = new HashMap<String, Node>();
			consumers = new HashMap<String, Node>();
		}

		public Channel(Node node) {
			this();
			this.node = node;
		}

		public Map<String, Node> getConsumers() {
			return consumers;
		}

		public String getName() {
			return name;
		}

		public Node getNode() {
			return node;
		}

		public Map<String, Node> getProducers() {
			return producers;
		}

		public void setConsumers(Map<String, Node> consumers) {
			this.consumers = consumers;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setNode(Node node) {
			this.node = node;
		}

		public void setProducers(Map<String, Node> producers) {
			this.producers = producers;
		}
	}

	private static Map<String, Channel> channelMap = new HashMap<String, Channel>();

	public static String[] natures = new String[] { "producer", "consumer",
			"channel" };

	public static void main(String[] args) {
		Application.launch(args);
	}

	public ProdConsPartitionLayoutExample() {
		super("GEF4 Layouts - Producer/Consumer Layout Example");
	}

	@Override
	protected Graph createGraph() {
		Random random = new Random();
		// create nodes
		List<Edge> edges = new ArrayList<Edge>();
		List<Node> nodes = new ArrayList<Node>();

		for (int i = 1; i < 25; i++) {
			String channelID = "" + i;
			org.eclipse.gef4.graph.Node node = new org.eclipse.gef4.graph.Node[] {
					n(LABEL, channelID, "KIND", natures[2]) }[0];

			channelMap.put(channelID, new Channel(node));
			nodes.add(node);

			int nbProd = random.nextInt(10);
			int nbCons = random.nextInt(10);
			for (int prod = 1; prod <= nbProd; prod++) {
				String prodID = channelID + "_P" + prod;
				org.eclipse.gef4.graph.Node nodeProd = new org.eclipse.gef4.graph.Node[] {
						n(LABEL, prodID, "KIND", natures[0]) }[0];
				nodes.add(nodeProd);
				// create edges
				edges.add(new org.eclipse.gef4.graph.Edge[] {
						e(nodeProd, node) }[0]);

				channelMap.get(channelID).getProducers().put(prodID, nodeProd);
			}
			for (int cons = 1; cons <= nbCons; cons++) {
				String consID = channelID + "_C" + cons;
				org.eclipse.gef4.graph.Node nodeCons = new org.eclipse.gef4.graph.Node[] {
						n(LABEL, consID, "KIND", natures[1]) }[0];
				nodes.add(nodeCons);

				// create edges
				edges.add(new org.eclipse.gef4.graph.Edge[] {
						e(node, nodeCons) }[0]);

				channelMap.get(channelID).getProducers().put(consID, nodeCons);
			}
		}

		System.out.println("eges : " + edges.size());
		System.out.println("nodes : " + nodes.size());

		return new Graph.Builder().nodes(nodes.toArray(new Node[0]))
				.edges(edges.toArray(new Edge[0]))
				.attr(ZestProperties.GRAPH_LAYOUT_ALGORITHM,
						createLayoutAlgorithm())
				.build();
	}

	private ILayoutAlgorithm createLayoutAlgorithm() {
		ILayoutAlgorithm layoutAlgorithm = new ILayoutAlgorithm() {
			private ILayoutContext context;

			@Override
			public void applyLayout(boolean clean) {
				INodeLayout[] entitiesToLayout = context.getNodes();
				int totalSteps = entitiesToLayout.length;
				double distance = LayoutProperties.getBounds(context).getWidth()
						/ totalSteps;

				int xLocation = 250;
				int yLocation = 100;

				for (INodeLayout node : context.getNodes()) {
					if (node instanceof GraphNodeLayout) {
						String label = (String) ((GraphNodeLayout) node)
								.getNode().getAttrs().get("label");
						String kind = (String) ((GraphNodeLayout) node)
								.getNode().getAttrs().get("KIND");
						System.out.println(kind + " : " + label);

						int startY = yLocation;
						for (String channelID : channelMap.keySet()) {
							if (channelID.equals(label)) {
								Channel channel = channelMap.get(channelID);
								LayoutProperties.setLocation(node, xLocation,
										yLocation);
								int nbP = 0;
								int yBottomProd = yLocation;
								int yBottomCons = yLocation;
								for (INodeLayout prod : node
										.getPredecessingNodes()) {
									yBottomProd = yLocation + nbP * 15;
									LayoutProperties.setLocation(prod,
											xLocation - 120, yBottomProd);
									nbP++;
								}

								int nbC = 0;
								for (INodeLayout cons : node
										.getSuccessingNodes()) {
									yBottomCons = yLocation + nbC * 15;
									LayoutProperties.setLocation(cons,
											xLocation + 120, yBottomCons);
									nbC++;
								}
								yLocation = Math.max(yBottomCons, yBottomProd);

								LayoutProperties.setLocation(node, xLocation,
										startY + (yLocation - startY) / 2
												- LayoutProperties
														.getSize(node).height
														/ 2
								/*
								 * LayoutProperties.getLocation( layoutEntity).y
								 */);
								yLocation += 50;
							}
						}
					}
				}
				// for (int currentStep = 0; currentStep <
				// entitiesToLayout.length; currentStep++) {
				// INodeLayout layoutEntity = entitiesToLayout[currentStep];
				// LayoutProperties.setLocation(layoutEntity, xLocation,
				// /*
				// * LayoutProperties.getLocation( layoutEntity).y
				// */0);
				// xLocation += distance;
				// }
			}

			@Override
			public ILayoutContext getLayoutContext() {
				return context;
			}

			@Override
			public void setLayoutContext(ILayoutContext context) {
				this.context = context;
			}
		};
		return layoutAlgorithm;
	}

}
