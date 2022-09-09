package sc.fiji.ome.zarr.fiji.ui;

import org.scijava.ItemIO;
import org.scijava.ItemVisibility;
import org.scijava.command.Command;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.Parameter;
import org.scijava.widget.FileWidget;
import org.scijava.log.LogService;
import net.imagej.Dataset;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(type = Command.class, menuPath = "File > Import > OME.Zarr...")
public class ZarrCommandPlugin implements Command {
	@Parameter(style = FileWidget.DIRECTORY_STYLE, label = "Folder with OME.Zarr data:")
	File zarrFolder;

	@Parameter(required = false,
			label = "Read directly the first item:",
			description = "When opening a multi-item OME.Zarr, show directly the first item rather than a dialog.")
	Boolean openFirstItemNow = true;

	@Parameter(required = false,
			label = "Or, Read directly this item:",
			description = "When opening a multi-item OME.Zarr, show directly the given item rather than a dialog.")
	String openThisItemNow = "";

	@Parameter(required = false,
			label = "Or, by not filling anything open the dialog...",
			visibility = ItemVisibility.MESSAGE)
	final String dialogInfoMsg = null;

	@Parameter(type = ItemIO.OUTPUT)
	Dataset dataset;

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

		if (topLevelFolder) {
			if (!openFirstItemNow && openThisItemNow.equals("")) {
				//open the dialog to determine which item to open
				DialogAroundZarr dg = new DialogAroundZarr(zarrFolder.toPath());

				//must be obtained from the dialog:
				if (openThisItemNow.equals("")) {
					logService.error("Requested top-level OME.Zarr folder "
							+ zarrFolder.getAbsolutePath() + " but haven't chosen any"
							+ " particular item to open.");
					return;
				}

				dataset = open(zarrFolder, openThisItemNow);
			}
			else {
				dataset = openFirstItemNow ? openFirst(zarrFolder) : open(zarrFolder, openThisItemNow);
			}
		} else {
			//not top-level folder:

			// find top-level and request 'openThisItemNow',
			// or open directly the 'zarrFolder' if Tischi's solution permits that
			dataset = null;
		}
	}

	Dataset open(final File zarrFolder, final String itemName) {
		logService.info("opening "+itemName);
		//TBA: waits for Tischi's IO API
		return null;
	}

	Dataset openFirst(final File zarrFolder) {
		logService.info("opening the first item");
		//TBA: waits for Tischi's IO API
		return null;
	}

	public static boolean isZarrFolder(final Path zarrFolder) {
		return ( Files.exists( zarrFolder.resolve( ".zgroup" ) ) ||
                Files.exists( zarrFolder.resolve( ".zarray" ) ) );
	}
}