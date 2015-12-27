package org.eclipse.gef4.graph.io.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.mvc.fx.domain.FXDomain;
import org.eclipse.gef4.mvc.fx.ui.parts.AbstractFXEditor;
import org.eclipse.gef4.mvc.fx.viewer.FXViewer;
import org.eclipse.gef4.mvc.models.ContentModel;
import org.eclipse.gef4.zest.examples.graph.ZestGraphExample;
import org.eclipse.gef4.zest.fx.ZestFxModule;
import org.eclipse.gef4.zest.fx.ui.ZestFxUiModule;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

import javafx.application.Platform;

public class GrapMLEditor extends AbstractFXEditor {

	public GrapMLEditor() {
		super(Guice.createInjector(Modules.override(new ZestFxModule()).with(new ZestFxUiModule())));
		
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site, input);
		
	}
	public void dispose() {
		super.dispose();
	}
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createPartControl(parent);
		setGraph(ZestGraphExample.createDefaultGraph());
	}
	
	public void setGraph(Graph graph) {
		// check we have a content viewer
		FXViewer contentViewer = getViewer();
		if (contentViewer == null) {
			throw new IllegalStateException("Invalid configuration: Content viewer could not be retrieved.");
		}
		// check we have a content model
		ContentModel contentModel = getViewer().getAdapter(ContentModel.class);
		if (contentModel == null) {
			throw new IllegalStateException("Invalid configuration: Content model could not be retrieved.");
		}
		// set contents (will wrap graph into contents list)
		List<Object> contents = new ArrayList<>(1);
		if (graph != null) {
			contents.add(graph);
		}
		contentModel.setContents(contents);
	}

}
