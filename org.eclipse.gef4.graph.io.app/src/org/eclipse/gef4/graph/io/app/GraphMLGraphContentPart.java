package org.eclipse.gef4.graph.io.app;

import org.eclipse.gef4.graph.io.graphml.model.Edge;
import org.eclipse.gef4.zest.fx.parts.GraphContentPart;

public class GraphMLGraphContentPart extends GraphContentPart {

	@Override
	protected void doRemoveContentChild(Object contentChild) {
		String ELEMENT_LAYOUT_IRRELEVANT = "layoutIrrelevant";
		Boolean ELEMENT_LAYOUT_IRRELEVANT_DEFAULT = true;
		if (contentChild instanceof Edge) {
			((org.eclipse.gef4.graph.Edge) contentChild).getAttrs().put(ELEMENT_LAYOUT_IRRELEVANT, ELEMENT_LAYOUT_IRRELEVANT_DEFAULT);
			getContent().getEdges().remove(contentChild);
		} else if (contentChild instanceof org.eclipse.gef4.graph.Node) {
			((org.eclipse.gef4.graph.Node) contentChild).getAttrs().put(ELEMENT_LAYOUT_IRRELEVANT,
					ELEMENT_LAYOUT_IRRELEVANT_DEFAULT);
			for (org.eclipse.gef4.graph.Edge e : ((org.eclipse.gef4.graph.Node) contentChild).getAllIncomingEdges()) {
				doRemoveContentChild(e);
			}
			for (org.eclipse.gef4.graph.Edge e : ((org.eclipse.gef4.graph.Node) contentChild).getAllOutgoingEdges()) {
				doRemoveContentChild(e);
			}
			getContent().getNodes().remove(contentChild);
		}

	}

}
