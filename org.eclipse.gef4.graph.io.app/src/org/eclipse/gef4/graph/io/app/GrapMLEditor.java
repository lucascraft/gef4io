package org.eclipse.gef4.graph.io.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef4.common.adapt.AdapterKey;
import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.io.graphml.GraphMLParser;
import org.eclipse.gef4.graph.io.graphml.model.GraphML;
import org.eclipse.gef4.graph.io.graphml.model.GraphMLAdapter;
import org.eclipse.gef4.graph.io.graphml.model.GraphMLMarshaller;
import org.eclipse.gef4.graph.io.graphml.model.Key;
import org.eclipse.gef4.mvc.fx.ui.parts.AbstractFXEditor;
import org.eclipse.gef4.mvc.fx.viewer.FXViewer;
import org.eclipse.gef4.mvc.models.ContentModel;
import org.eclipse.gef4.mvc.policies.DeletionPolicy;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

import com.google.common.reflect.TypeToken;
import com.google.inject.Guice;
import com.google.inject.util.Modules;

import javafx.scene.Node;

public class GrapMLEditor extends AbstractFXEditor {
	private Graph graph;
	private GraphMLParser graphMLParser;

	public GrapMLEditor() {
		super(Guice.createInjector(Modules.override(new GraphMLModule()).with(new GraphMLUiModule())));
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site, input);
		if (input instanceof FileEditorInput)
		{
			File file = ((FileEditorInput)input).getFile().getRawLocation().toFile();
			
			graphMLParser = new GraphMLParser(file);
			graph = graphMLParser.load();

			setGraph(graph);
		}
	}
	@Override
	public boolean isDirty() {
		return super.isDirty();
	}
	
	public void dispose() {
		super.dispose();
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		persist();
	}
	
	@Override
	public void doSaveAs() {
		persist();
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	private void persist()
	{
		GraphMLAdapter graphMLAdapter = new GraphMLAdapter();
		GraphML graphML = graphMLAdapter.adaptGraphRoot(graph);
		List<Key> keys = graphMLParser.getKeys();
		graphML.setKeys(keys);

		GraphMLMarshaller.marshall(graphML, ((FileEditorInput)getEditorInput()).getFile().getRawLocation().toFile());
		
		setDirty(false);
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
