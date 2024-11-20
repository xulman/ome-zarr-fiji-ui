package sc.fiji.ome.zarr.fiji.ui;

import org.scijava.command.Command;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.Parameter;
import org.scijava.ui.UIService;
import org.scijava.widget.FileWidget;
import org.scijava.log.LogService;

import java.io.File;

@Plugin(type = Command.class, menuPath = "File > Import > OME.Zarr... > Open Zarr as Virtual Stack")
public class ZarrOpenInIjPlugin implements Command {
	@Parameter(style = FileWidget.DIRECTORY_STYLE,
			label = "Folder with OME.Zarr data:",
			description = "Please, point only on the top level folder of the OME.Zarr container.")
	File zarrFolder;

	@Parameter
	LogService logService;

	@Parameter
	UIService uiService;

	@Override
	public void run() {
		if (!ZarrOpenDialogPlugin.canOpenThisZarrFolder(zarrFolder, logService))
			return;

		uiService.show("HELLO FROM IJ OPENER");
		System.out.println("HELLO FROM IJ OPENER"); //just in case...
	}
}