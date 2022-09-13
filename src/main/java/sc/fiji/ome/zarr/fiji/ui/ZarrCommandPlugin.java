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

@Plugin(type = Command.class, menuPath = "File > Import > OME.Zarr...")
public class ZarrCommandPlugin implements Command {
	@Parameter(style = FileWidget.DIRECTORY_STYLE,
			label = "Folder with OME.Zarr data:",
			description = "Please, point only on the top level folder of the OME.Zarr container.")
	File zarrFolder;

	@Parameter(required = false,
			label = "Open as virtual stack:",
			description = "This opens the standard Fiji image viewing window.")
	Boolean openInIJ = true;

	@Parameter(required = false,
			label = "Open in BigDataViewer:",
			description = "This opens the standard Fiji image viewing window.")
	Boolean openInBDV = true;

	@Parameter(required = false,
			label = "What to do next time:",
			choices = {
					"Show this dialog again",
					"Remember the checkboxes, open directly next time, show this dialog in a week again.",
					"Remember the checkboxes, open directly next time, never show this dialog again (Edit->Options)"})
	String futureDialogAction = "Show";

	@Parameter
	LogService logService;

	@Override
	public void run() {
		if ( !isZarrFolder( zarrFolder.toPath() ) ) {
			logService.error("The folder " + zarrFolder.getAbsolutePath()
					+ " is not an OME.Zarr container.");
			return;
		}

		final boolean topLevelFolder = !isZarrFolder( zarrFolder.getParentFile().toPath() );
		logService.info("Accessing " + zarrFolder.getAbsolutePath());
		logService.info("It is a " + (topLevelFolder ? "top-level" : "nested-level") + " folder");
		//
		if (!topLevelFolder) {
			logService.error("I can't do yet. Please, point me on top-level folder next time.");
			return;
		}

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

	public static void main(String[] args) {
		final String path = "/home/ulman/M_TRIF_testSmall/testNew.zarr";
		openZarr(new Context(), path, true, true);
	}
}