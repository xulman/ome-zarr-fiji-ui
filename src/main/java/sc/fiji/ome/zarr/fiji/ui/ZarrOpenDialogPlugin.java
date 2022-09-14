package sc.fiji.ome.zarr.fiji.ui;

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

import org.embl.mobie.io.ome.zarr.hackathon.DefaultPyramidal5DImageData;
import org.embl.mobie.io.ome.zarr.hackathon.MultiscaleImage;
import bdv.util.BdvFunctions;
import net.imagej.Dataset;

@Plugin(type = Command.class, menuPath = "File > Import > OME.Zarr... > Open Zarr Dialog")
public class ZarrOpenDialogPlugin implements Command {
	@Parameter(style = FileWidget.DIRECTORY_STYLE,
			label = "Folder with OME.Zarr data:",
			description = "Please, point only on the top level folder of the OME.Zarr container.")
	File zarrFolder;

	@Parameter(label = "Open as virtual stack:",
			description = "This opens the standard Fiji image viewing window.")
	Boolean openInIJ = true;

	@Parameter(label = "Open in BigDataViewer:",
			description = "This opens the standard Fiji image viewing window.")
	Boolean openInBDV = true;

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
		if ( !canOpenThisZarrFolder(zarrFolder, logService) )
			return;
		openZarr(logService.context(), zarrFolder.getAbsolutePath(), openInIJ, openInBDV);
	}


	public static void openZarr(final Context ctx, final String topLevelPath,
	                            final boolean showInFiji,
	                            final boolean showInBDV) {

		final MultiscaleImage<?,?> mi = new MultiscaleImage<>(topLevelPath, null);
		final DefaultPyramidal5DImageData<?,?> singleMI = new DefaultPyramidal5DImageData<>(ctx, topLevelPath, mi);
		final Dataset dataset = singleMI.asDataset();

		if (showInFiji) {
			UIService ui = ctx.getService(UIService.class);
			if (ui != null) ui.show( dataset );
			else System.out.println("Not opening in Fiji, failed to find an available scijava UIService.");
		}

		if (showInBDV) {
			BdvFunctions.show( dataset, topLevelPath );
		}
	}


	public static boolean isZarrFolder(final Path zarrFolder) {
		return ( Files.exists( zarrFolder.resolve( ".zgroup" ) ) ||
                Files.exists( zarrFolder.resolve( ".zarray" ) ) );
	}

	public static boolean canOpenThisZarrFolder(final File zarrFolder, final LogService logService) {
		logService.debug("Considering Zarr folder: " + zarrFolder.getAbsolutePath());

		if ( !isZarrFolder( zarrFolder.toPath() ) ) {
			logService.error("The folder " + zarrFolder.getAbsolutePath()
					+ " is not an OME.Zarr container.");
			return false;
		}

		final boolean topLevelFolder = !isZarrFolder( zarrFolder.getParentFile().toPath() );
		logService.debug("It is a " + (topLevelFolder ? "top-level" : "nested-level") + " folder");

		if (!topLevelFolder)
			logService.error("I can't do yet. Please, point me on top-level folder next time.");

		return topLevelFolder;
	}

	public static boolean canOpenThisZarrFolder(final File zarrFolder) {
		if ( !isZarrFolder( zarrFolder.toPath() ) )
			return false;
		//
		return !isZarrFolder( zarrFolder.getParentFile().toPath() );
	}


	public static void main(String[] args) {
		final String path = "/home/ulman/M_TRIF_testSmall/testNew_v4.zarr";
		openZarr(new Context(), path, true, true);
	}
}