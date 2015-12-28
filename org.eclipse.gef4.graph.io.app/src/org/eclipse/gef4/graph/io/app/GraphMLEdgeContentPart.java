package org.eclipse.gef4.graph.io.app;

import org.eclipse.gef4.mvc.parts.IVisualPart;
import org.eclipse.gef4.zest.fx.parts.EdgeContentPart;

import com.google.common.collect.SetMultimap;

import javafx.scene.Node;

public class GraphMLEdgeContentPart extends EdgeContentPart {
		@Override
		protected void doDetachFromContentAnchorage(Object contentAnchorage, String role) {
			// getContentAnchorages().remove(contentAnchorage, role);
		}
		
		@Override
		public SetMultimap<Object, String> getContentAnchorages() {
			return super.getContentAnchorages();
		}
		
		@Override
		public SetMultimap<IVisualPart<Node, ? extends Node>, String> getAnchorages() {
			return super.getAnchorages();
		}
}
