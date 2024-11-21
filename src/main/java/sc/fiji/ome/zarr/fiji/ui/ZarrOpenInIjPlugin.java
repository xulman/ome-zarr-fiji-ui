package sc.fiji.ome.zarr.fiji.ui;

import org.scijava.command.Command;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.Parameter;
import org.scijava.ui.UIService;
import org.scijava.widget.FileWidget;
import sc.fiji.ome.zarr.fiji.ui.util.ZarrOnFSutils;

import java.io.File;

@Plugin(type = Command.class, menuPath = "File > OME Zarr... > Open as Virtual Stack")
public class ZarrOpenInIjPlugin implements Command {
	@Parameter(style = FileWidget.DIRECTORY_STYLE,
			label = "Folder with OME Zarr data:",
			description = "Please, point only on the top level folder of the OME Zarr container.")
	File zarrFolder;

	@Parameter
	UIService uiService;

	@Override
	public void run() {
		if (!ZarrOnFSutils.isZarrFolder(zarrFolder.toPath())) return;

		uiService.show("HELLO FROM IJ OPENER");
		System.out.println("HELLO FROM IJ OPENER"); //just in case...
	}
}