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

import bdv.util.BdvFunctions;
import net.imagej.Dataset;

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
		if ( isZarrFolder( zarrFolder.toPath() ) ) {
			openZarr(logService.context(), zarrFolder.getAbsolutePath(), openInIJ, openInBDV);
		}
	}


	/**
	 * Checks if under the given folder there exists any of
	 * the files: .zgroup, .zarray or zarr.json.
	 * @param zarrFolder Supposedly the top-level Zarr folder.
	 * @return True if some of the three files is found.
	 */
	public static boolean isZarrFolder(final Path zarrFolder) {
		return ( Files.exists( zarrFolder.resolve( ".zgroup" ) ) || //Zarr v2
				  Files.exists( zarrFolder.resolve( ".zarray" ) ) || //Zarr v2
				  Files.exists( zarrFolder.resolve("zarr.json") ) ); //Zarr v3
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

		final Dataset dataset = null; //TODO!

		if (showInFiji) {
			UIService ui = ctx.getService(UIService.class);
			if (ui != null) ui.show( dataset );
			else System.out.println("Not opening in Fiji, failed to find an available scijava UIService.");
		}

		if (showInBDV) {
			BdvFunctions.show( dataset, topFolderPath );
		}
	}


	public static void main(String[] args) {
		final String path = "/home/ulman/M_TRIF_testSmall/testNew_v4.zarr";
		openZarr(new Context(), path, true, true);
	}
}