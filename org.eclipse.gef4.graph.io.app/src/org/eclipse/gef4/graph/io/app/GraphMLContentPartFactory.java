package org.eclipse.gef4.graph.io.app;

import java.util.Map;

import org.eclipse.gef4.graph.Edge;
import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.mvc.behaviors.IBehavior;
import org.eclipse.gef4.mvc.parts.IContentPart;
import org.eclipse.gef4.mvc.parts.IContentPartFactory;
import org.eclipse.gef4.zest.fx.parts.EdgeContentPart;
import org.eclipse.gef4.zest.fx.parts.EdgeLabelPart;
import org.eclipse.gef4.zest.fx.parts.GraphContentPart;
import org.eclipse.gef4.zest.fx.parts.NodeContentPart;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javafx.scene.Node;
import javafx.util.Pair;

public class GraphMLContentPartFactory implements IContentPartFactory<Node> {

	@Inject
	private Injector injector;

	@SuppressWarnings("rawtypes")
	@Override
	public IContentPart<Node, ? extends Node> createContentPart(Object content, IBehavior<Node> contextBehavior,
			Map<Object, Object> contextMap) {
		IContentPart<Node, ? extends Node> part = null;
		if (content instanceof Graph) {
			part = new GraphMLGraphContentPart();
		} else if (content instanceof org.eclipse.gef4.graph.Node) {
			part = new GraphMLNodeContentPart();
		} else if (content instanceof Edge) {
			part = new GraphMLEdgeContentPart();
		} else if (content instanceof Pair && ((Pair) content).getKey() instanceof Edge) {
			part = new GraphMLEdgeLabelPart();
		}
		if (part != null) {
			injector.injectMembers(part);
		}
		return part;
	}


}
