package sc.fiji.ome.zarr.fiji.ui;

import java.nio.file.Path;
import java.util.concurrent.Executors;

import org.janelia.saalfeldlab.n5.bdv.N5ViewerTreeCellRenderer;
import org.janelia.saalfeldlab.n5.ij.N5Importer.N5BasePathFun;
import org.janelia.saalfeldlab.n5.ij.N5Importer.N5ViewerReaderFun;
import org.janelia.saalfeldlab.n5.metadata.N5GenericSingleScaleMetadataParser;
import org.janelia.saalfeldlab.n5.metadata.N5MetadataParser;
import org.janelia.saalfeldlab.n5.metadata.N5ViewerMultiscaleMetadataParser;
import org.janelia.saalfeldlab.n5.ui.DatasetSelectorDialog;

import java.nio.file.Files;

public class DialogAroundZarr {

	/*
	 * Will need metadata parsers for ome-zarr
	 * see
	 * https://github.com/saalfeldlab/n5-imglib2/issues/32
	 */
	public static final N5MetadataParser<?>[] PARSERS = new N5MetadataParser[] {
			new N5GenericSingleScaleMetadataParser() };

	public static final N5MetadataParser<?>[] GROUP_PARSERS = new N5MetadataParser[] {
			new N5ViewerMultiscaleMetadataParser() };

	public DialogAroundZarr(final Path zarrFolder) {
		if (!Files.isDirectory(zarrFolder))
			throw new IllegalArgumentException("Cannot access "
					+zarrFolder.toAbsolutePath()+" or it is not a folder."
					+" I need a top-level _folder_ to open.");

		if (!zarrFolder.toString().endsWith(".ome.zarr"))
			throw new IllegalArgumentException("The folder "
					+zarrFolder.toAbsolutePath()+" is likely not a .ome.zarr");

		// build the dialog
		DatasetSelectorDialog selectionDialog = new DatasetSelectorDialog(
				new N5ViewerReaderFun(),
				new N5BasePathFun(),
				zarrFolder.toAbsolutePath().toString(),
				GROUP_PARSERS, // no group parsers
				PARSERS );

		// some details on the setup
		selectionDialog.setTreeRenderer(new N5ViewerTreeCellRenderer(false));
		selectionDialog.setLoaderExecutor( Executors.newFixedThreadPool( 1 ));
		selectionDialog.setContainerPathUpdateCallback( x -> {});

		// lambda defines behavior when we press ok
		selectionDialog.run( selection -> {
			System.out.println( "opening dataset: " + selection.metadata );
		});

		// parse the tree
		selectionDialog.detectDatasets();
	}
}
