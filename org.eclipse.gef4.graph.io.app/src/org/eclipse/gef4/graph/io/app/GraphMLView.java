package org.eclipse.gef4.graph.io.app;

import java.io.File;

import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.io.graphml.GraphMLParser;
import org.eclipse.gef4.layout.algorithms.SpringLayoutAlgorithm;
import org.eclipse.gef4.zest.fx.ZestProperties;
import org.eclipse.gef4.zest.fx.ui.parts.ZestFxUiView;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

public class GraphMLView extends ZestFxUiView {
	public GraphMLView() {
		GraphMLParser nestedGraphMLParser = new GraphMLParser(new File("samples\\simple.xml"));
		Graph graph = nestedGraphMLParser.load();
		graph.getAttrs().put(ZestProperties.GRAPH_TYPE, ZestProperties.GRAPH_TYPE_DIRECTED);
		graph.getAttrs().put(ZestProperties.GRAPH_LAYOUT_ALGORITHM, new SpringLayoutAlgorithm());
		setGraph(graph);	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		getViewSite().getActionBars().getToolBarManager().add(new Action("Select GraphML") {
			@Override
			public void run() {
				super.run();
				selectFile();
			}
		});
	}
	
	private void selectFile()
	{
		FileDialog fsdlg = new FileDialog(Display.getDefault().getActiveShell());
		String str = fsdlg.open();
		GraphMLParser nestedGraphMLParser = new GraphMLParser(new File(str));
		Graph graph = nestedGraphMLParser.load();
		graph.getAttrs().put(ZestProperties.GRAPH_TYPE, ZestProperties.GRAPH_TYPE_DIRECTED);
		graph.getAttrs().put(ZestProperties.GRAPH_LAYOUT_ALGORITHM, new SpringLayoutAlgorithm());
		setGraph(graph);
	}
}
