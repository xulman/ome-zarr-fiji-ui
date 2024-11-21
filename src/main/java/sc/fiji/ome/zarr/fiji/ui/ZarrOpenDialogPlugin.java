package sc.fiji.ome.zarr.fiji.ui;

import net.imagej.ImageJ;
import net.imglib2.cache.img.CachedCellImg;
import net.imglib2.img.Img;
import org.janelia.saalfeldlab.n5.DatasetAttributes;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;
import org.janelia.saalfeldlab.n5.universe.N5Factory;
import org.scijava.Context;
import org.scijava.command.Command;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.Parameter;
import org.scijava.ui.UIService;
import org.scijava.widget.FileWidget;
import org.scijava.log.LogService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import bdv.util.BdvFunctions;
import net.imagej.Dataset;
import sc.fiji.ome.zarr.fiji.ui.util.ZarrOnFSutils;

@Plugin(type = Command.class, menuPath = "File > OME Zarr... > Open Dialog")
public class ZarrOpenDialogPlugin implements Command {
	@Parameter(style = FileWidget.DIRECTORY_STYLE,
			label = "Folder with OME Zarr:",
			description = "Please, point only on the top level _folder_ of the OME Zarr container.")
	File zarrFolder;

	@Parameter(label = "Open as virtual stack:",
			description = "This opens the standard Fiji image viewing window.")
	boolean openInIJ = true;

	@Parameter(label = "Open in BigDataViewer:",
			description = "This opens the standard Fiji image viewing window.")
	boolean openInBDV = true;

	public static final String DIAG_ALWAYS_ASK = "Always open this dialog and ask.";
	public static final String DIAG_OPEN_DIRECTLY = "Remember the checkboxes, directly open accordingly.";

	@Parameter(required = false,
			label = "Action after drag-and-drop:",
			choices = { DIAG_ALWAYS_ASK, DIAG_OPEN_DIRECTLY })
	String futureDialogAction = DIAG_ALWAYS_ASK;

	@Parameter
	LogService logService;

	@Override
	public void run() {
		Path rootZarrPath = ZarrOnFSutils.findRootFolder( zarrFolder.toPath() );
		if ( rootZarrPath != null) {
			openZarr(logService.context(), rootZarrPath.toAbsolutePath().toString(), openInIJ, openInBDV);
		}
	}


	/**
	 * TBA
	 * @param ctx
	 * @param topFolderPath
	 * @param showInFiji
	 * @param showInBDV
	 */
	public static void openZarr(final Context ctx, final String topFolderPath,
	                            final boolean showInFiji,
	                            final boolean showInBDV) {

		int sepPos = topFolderPath.lastIndexOf('/');
		String rootPath = topFolderPath.substring(0,sepPos);
		String group = topFolderPath.substring(sepPos+1)+"/s0";
		System.out.println(rootPath+" -- "+group);

		N5Reader reader = new N5Factory().openReader(rootPath);

		System.out.println("test1 = "+reader.datasetExists("djksalfdjs"));
		System.out.println("test2 = "+reader.datasetExists("CTC_trainTrif02_TP35"));
		System.out.println("test3 = "+reader.datasetExists("CTC_trainTrif02_TP35/s2"));

		System.out.println("-------");
		for (String ds : reader.deepListDatasets("")) System.out.println(ds);
		System.out.println("-------");
		for (String ds : reader.list("")) System.out.println(ds);
		System.out.println("-------");


		DatasetAttributes attrs = reader.getDatasetAttributes(group);
		printLongArray(attrs.getDimensions());

		Img<?> i = N5Utils.open(reader, group);
		printLongArray(i.dimensionsAsLongArray());

/*
		final Dataset dataset = null; //TODO!

		if (showInFiji) {
			UIService ui = ctx.getService(UIService.class);
			if (ui != null) ui.show( dataset );
			else System.out.println("Not opening in Fiji, failed to find an available scijava UIService.");
		}

		if (showInBDV) {
			BdvFunctions.show( dataset, topFolderPath );
		}
*/
	}

	public static void printLongArray(long[] a) {
		System.out.print("[");
		for (long val : a) {
			System.out.print(val+",");
		}
		System.out.print("]");
	}


	public static void main(String[] args) {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();

		//final String path = "/home/ulman/M_TRIF_testSmall/testNew_v4.zarr";
		final String path = "/temp/Zurich.hackathon.testData/maybe_top_level.zarr/CTC_trainTrif02_TP35";
		openZarr(new Context(), path, true, true);
	}
}