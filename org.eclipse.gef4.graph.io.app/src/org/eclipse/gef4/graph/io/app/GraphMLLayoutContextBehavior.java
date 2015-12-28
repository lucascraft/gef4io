package org.eclipse.gef4.graph.io.app;

import org.eclipse.gef4.zest.fx.behaviors.LayoutContextBehavior;
import org.eclipse.gef4.zest.fx.layout.GraphEdgeLayout;
import org.eclipse.gef4.zest.fx.layout.GraphNodeLayout;
import org.eclipse.gef4.zest.fx.models.HidingModel;
import org.eclipse.gef4.layout.*;
import org.eclipse.gef4.zest.fx.ZestProperties;

public class GraphMLLayoutContextBehavior extends LayoutContextBehavior {

	@Override
	public void activate() {
		super.activate();
		
		final HidingModel hidingModel = getHost().getRoot().getViewer().getAdapter(HidingModel.class);
		getGraphLayoutContext().addLayoutFilter(new ILayoutFilter() {
			@Override
			public boolean isLayoutIrrelevant(IConnectionLayout connectionLayout) {
				return ZestProperties.getLayoutIrrelevant(((GraphEdgeLayout) connectionLayout).getEdge(), true)
						|| isLayoutIrrelevant(connectionLayout.getSource())
						|| isLayoutIrrelevant(connectionLayout.getTarget());
			}

			@Override
			public boolean isLayoutIrrelevant(INodeLayout nodeLayout) {
				if (nodeLayout != null) {
					org.eclipse.gef4.graph.Node node = ((GraphNodeLayout) nodeLayout).getNode();
					return ZestProperties.getLayoutIrrelevant(node, true) || hidingModel.isHidden(node);
				}
				return true;
			}
		});
	}
}
