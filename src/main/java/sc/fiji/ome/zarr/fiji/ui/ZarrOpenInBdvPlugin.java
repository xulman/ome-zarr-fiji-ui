package sc.fiji.ome.zarr.fiji.ui;

import org.scijava.command.Command;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.Parameter;
import org.scijava.widget.FileWidget;
import org.scijava.log.LogService;
import java.io.File;
import bdv.util.BdvFunctions;

@Plugin(type = Command.class, menuPath = "File > Import > OME.Zarr... > Open Zarr in BDV")
public class ZarrOpenInBdvPlugin implements Command {
	@Parameter(style = FileWidget.DIRECTORY_STYLE,
			label = "Folder with OME.Zarr data:",
			description = "Please, point only on the top level folder of the OME.Zarr container.")
	File zarrFolder;

	@Parameter
	LogService logService;

	@Override
	public void run() {
		if (!ZarrOpenDialogPlugin.canOpenThisZarrFolder(zarrFolder, logService))
			return;

		//BdvFunctions.show(singleMI.asDataset(), topLevelPath);
		System.out.println("HELLO FROM BDV OPENER");
	}
}