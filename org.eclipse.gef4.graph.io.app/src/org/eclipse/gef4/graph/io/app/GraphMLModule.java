package org.eclipse.gef4.graph.io.app;

import org.eclipse.gef4.common.adapt.AdapterKey;
import org.eclipse.gef4.common.inject.AdaptableScopes;
import org.eclipse.gef4.common.inject.AdapterMap;
import org.eclipse.gef4.common.inject.AdapterMaps;
import org.eclipse.gef4.mvc.fx.MvcFxModule;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultFeedbackPartFactory;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultHandlePartFactory;
import org.eclipse.gef4.mvc.fx.parts.FXRectangleSegmentHandlePart;
import org.eclipse.gef4.mvc.fx.policies.FXFocusAndSelectOnClickPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXHoverOnHoverPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXResizePolicy;
import org.eclipse.gef4.mvc.fx.policies.FXResizeTranslateOnHandleDragPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXRotateSelectedOnHandleDragPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXTransformPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXTranslateSelectedOnDragPolicy;
import org.eclipse.gef4.mvc.fx.providers.ChopBoxAnchorProvider;
import org.eclipse.gef4.mvc.fx.providers.GeometricOutlineProvider;
import org.eclipse.gef4.mvc.fx.providers.ShapeBoundsProvider;
import org.eclipse.gef4.mvc.fx.viewer.FXViewer;
import org.eclipse.gef4.mvc.parts.IContentPartFactory;
import org.eclipse.gef4.mvc.parts.IHandlePartFactory;
import org.eclipse.gef4.mvc.parts.IRootPart;
import org.eclipse.gef4.mvc.policies.DeletionPolicy;
import org.eclipse.gef4.zest.fx.behaviors.EdgeHidingBehavior;
import org.eclipse.gef4.zest.fx.behaviors.EdgeLabelHidingBehavior;
import org.eclipse.gef4.zest.fx.behaviors.EdgeLayoutBehavior;
import org.eclipse.gef4.zest.fx.behaviors.NodeHidingBehavior;
import org.eclipse.gef4.zest.fx.behaviors.NodeLayoutBehavior;
import org.eclipse.gef4.zest.fx.models.HidingModel;
import org.eclipse.gef4.zest.fx.models.NavigationModel;
import org.eclipse.gef4.zest.fx.parts.ContentPartFactory;
import org.eclipse.gef4.zest.fx.parts.EdgeContentPart;
import org.eclipse.gef4.zest.fx.parts.EdgeLabelPart;
import org.eclipse.gef4.zest.fx.parts.GraphContentPart;
import org.eclipse.gef4.zest.fx.parts.GraphRootPart;
import org.eclipse.gef4.zest.fx.parts.HandlePartFactory;
import org.eclipse.gef4.zest.fx.parts.HideHoverHandlePart;
import org.eclipse.gef4.zest.fx.parts.NodeContentPart;
import org.eclipse.gef4.zest.fx.parts.ShowHiddenNeighborsHoverHandlePart;
import org.eclipse.gef4.zest.fx.policies.HideFirstAnchorageOnClickPolicy;
import org.eclipse.gef4.zest.fx.policies.HideOnTypePolicy;
import org.eclipse.gef4.zest.fx.policies.HidePolicy;
import org.eclipse.gef4.zest.fx.policies.OffsetEdgeLabelOnDragPolicy;
import org.eclipse.gef4.zest.fx.policies.OpenNestedGraphOnDoubleClickPolicy;
import org.eclipse.gef4.zest.fx.policies.OpenParentGraphOnDoubleClickPolicy;
import org.eclipse.gef4.zest.fx.policies.SemanticZoomPolicy;
import org.eclipse.gef4.zest.fx.policies.ShowHiddenNeighborsOfFirstAnchorageOnClickPolicy;
import org.eclipse.gef4.zest.fx.policies.ShowHiddenNeighborsOnTypePolicy;
import org.eclipse.gef4.zest.fx.policies.ShowHiddenNeighborsPolicy;

import com.google.common.reflect.TypeToken;
import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

import javafx.scene.Node;

public class GraphMLModule extends MvcFxModule {
	public GraphMLModule() {
	}
	
	protected void bindGraphContentPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(GraphMLGraphLayoutContext.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(GraphMLLayoutContextBehavior.class);
	}

	/**
	 * Binds {@link IContentPartFactory} to {@link ContentPartFactory}.
	 */
	protected void bindIContentPartFactory() {
		binder().bind(new TypeLiteral<IContentPartFactory<Node>>() {
		}).to(GraphMLContentPartFactory.class).in(AdaptableScopes.typed(FXViewer.class));
	}

	@Override
	protected void bindAbstractContentPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindAbstractContentPartAdapters(adapterMapBinder);
		// register (default) interaction policies (which are based on viewer
		// models and do not depend on transaction policies)
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FXFocusAndSelectOnClickPolicy.class);
		// geometry provider for hover handles
		adapterMapBinder.addBinding(AdapterKey.role(FXDefaultHandlePartFactory.HOVER_HANDLES_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
	}

	@Override
	protected void bindAbstractViewerAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindAbstractViewerAdapters(adapterMapBinder);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(NavigationModel.class);
	}

	/**
	 * Adds (default) {@link AdapterMap} bindings for {@link EdgeContentPart}
	 * and all sub-classes. May be overwritten by sub-classes to change the
	 * default bindings.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link EdgeContentPart} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindEdgeContentPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// layout
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(EdgeLayoutBehavior.class);
		// hiding
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(EdgeHidingBehavior.class);

		// feedback and handles
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FXHoverOnHoverPolicy.class);
		adapterMapBinder
				.addBinding(AdapterKey.role(FXDefaultFeedbackPartFactory.SELECTION_LINK_FEEDBACK_GEOMETRY_PROVIDER))
				.to(GeometricOutlineProvider.class);
		// geometry provider for selection feedback
		adapterMapBinder.addBinding(AdapterKey.role(FXDefaultFeedbackPartFactory.SELECTION_FEEDBACK_GEOMETRY_PROVIDER))
				.to(GeometricOutlineProvider.class);
		// geometry provider for hover feedback
		adapterMapBinder.addBinding(AdapterKey.role(FXDefaultFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER))
				.to(GeometricOutlineProvider.class);
	}

	/**
	 * Adds (default) {@link AdapterMap} bindings for {@link EdgeLabelPart} and
	 * all sub-classes. May be overwritten by sub-classes to change the default
	 * bindings.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link EdgeLabelPart} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindEdgeLabelPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// hiding
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(EdgeLabelHidingBehavior.class);
		// offset on drag
		adapterMapBinder.addBinding(AdapterKey.role("OffsetOnDrag")).to(OffsetEdgeLabelOnDragPolicy.class);
		// hover feedback
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FXHoverOnHoverPolicy.class);
		// selection link feedback
		adapterMapBinder
				.addBinding(AdapterKey.role(FXDefaultFeedbackPartFactory.SELECTION_LINK_FEEDBACK_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
		// geometry provider for selection feedback
		adapterMapBinder.addBinding(AdapterKey.role(FXDefaultFeedbackPartFactory.SELECTION_FEEDBACK_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
		// geometry provider for hover feedback
		adapterMapBinder.addBinding(AdapterKey.role(FXDefaultFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
	}

	@Override
	protected void bindFXChangeViewportPolicyAsFXRootPartAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(SemanticZoomPolicy.class);
	}

	/**
	 * Bind resize and rotate behavior to {@link FXRectangleSegmentHandlePart}.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link FXRectangleSegmentHandlePart} as a key.
	 */
	protected void bindFXRectangleSegmentHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role("ResizeRelocateOnHandleDrag"))
				.to(FXResizeTranslateOnHandleDragPolicy.class);
		// rotate on drag + control
		adapterMapBinder.addBinding(AdapterKey.role("rotate")).to(FXRotateSelectedOnHandleDragPolicy.class);
	}

	@Override
	protected void bindFXRootPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindFXRootPartAdapters(adapterMapBinder);
		adapterMapBinder.addBinding(AdapterKey.role("OpenParentGraphOnDoubleClick"))
				.to(OpenParentGraphOnDoubleClickPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.get(new TypeToken<DeletionPolicy<Node>>(){})).to(DeletionPolicy.class);
	}

	@Override
	protected void bindFXViewerAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindFXViewerAdapters(adapterMapBinder);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HidingModel.class);
	}

	/**
	 * Adds (default) {@link AdapterMap} bindings for
	 * {@link HideHoverHandlePart} and all sub-classes. May be overwritten by
	 * sub-classes to change the default bindings.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link HideHoverHandlePart} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindHidingHoverHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role("hide")).to(HideFirstAnchorageOnClickPolicy.class);
	}

	@Override
	protected void bindIHandlePartFactory() {
		binder().bind(new TypeLiteral<IHandlePartFactory<Node>>() {
		}).to(HandlePartFactory.class).in(AdaptableScopes.typed(FXViewer.class));
	}

	@Override
	protected void bindIRootPart() {
		binder().bind(new TypeLiteral<IRootPart<Node, ? extends Node>>() {
		}).to(GraphRootPart.class).in(AdaptableScopes.typed(FXViewer.class));
	}

	/**
	 * Adds (default) {@link AdapterMap} bindings for {@link NodeContentPart}
	 * and all sub-classes. May be overwritten by sub-classes to change the
	 * default bindings.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link NodeContentPart} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindNodeContentPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// layout
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(NodeLayoutBehavior.class);
		// pruning
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HidePolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ShowHiddenNeighborsPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(NodeHidingBehavior.class);
		// interaction
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FXTranslateSelectedOnDragPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.role("showHiddenNeighbors")).to(ShowHiddenNeighborsOnTypePolicy.class);
		adapterMapBinder.addBinding(AdapterKey.role("hide")).to(HideOnTypePolicy.class);
		adapterMapBinder.addBinding(AdapterKey.role("OpenNestedGraphOnDoubleClick"))
				.to(OpenNestedGraphOnDoubleClickPolicy.class);
		// transform policy for relocation
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FXTransformPolicy.class);
		// resize policy to resize nesting nodes
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FXResizePolicy.class);
		// cursor provider
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ChopBoxAnchorProvider.class);
		// feedback and handles
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FXHoverOnHoverPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.role(FXDefaultHandlePartFactory.SELECTION_HANDLES_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
		// geometry provider for selection feedback
		adapterMapBinder.addBinding(AdapterKey.role(FXDefaultFeedbackPartFactory.SELECTION_FEEDBACK_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
		// geometry provider for hover feedback
		adapterMapBinder.addBinding(AdapterKey.role(FXDefaultFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
	}

	/**
	 * Adds (default) {@link AdapterMap} bindings for
	 * {@link ShowHiddenNeighborsHoverHandlePart} and all sub-classes. May be
	 * overwritten by sub-classes to change the default bindings.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link ShowHiddenNeighborsHoverHandlePart} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindShowHiddenNeighborsHoverHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role("showHiddenNeighbors"))
				.to(ShowHiddenNeighborsOfFirstAnchorageOnClickPolicy.class);
	}

	@Override
	protected void configure() {
		super.configure();

		bindIContentPartFactory();

		bindGraphContentPartAdapters(AdapterMaps.getAdapterMapBinder(binder(), GraphContentPart.class));
		bindNodeContentPartAdapters(AdapterMaps.getAdapterMapBinder(binder(), NodeContentPart.class));
		bindEdgeContentPartAdapters(AdapterMaps.getAdapterMapBinder(binder(), EdgeContentPart.class));
		bindEdgeLabelPartAdapters(AdapterMaps.getAdapterMapBinder(binder(), EdgeLabelPart.class));

		bindFXRectangleSegmentHandlePartAdapters(
				AdapterMaps.getAdapterMapBinder(binder(), FXRectangleSegmentHandlePart.class));
		bindHidingHoverHandlePartAdapters(AdapterMaps.getAdapterMapBinder(binder(), HideHoverHandlePart.class));
		bindShowHiddenNeighborsHoverHandlePartAdapters(
				AdapterMaps.getAdapterMapBinder(binder(), ShowHiddenNeighborsHoverHandlePart.class));
	}


}
