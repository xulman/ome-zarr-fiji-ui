package sc.fiji.ome.zarr.fiji.ui;

import org.scijava.command.Command;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.Parameter;
import org.scijava.widget.FileWidget;
import sc.fiji.ome.zarr.fiji.ui.util.ZarrOnFSutils;

import java.io.File;

@Plugin(type = Command.class, menuPath = "File > OME Zarr... > Open in BDV")
public class ZarrOpenInBdvPlugin implements Command {
	@Parameter(style = FileWidget.DIRECTORY_STYLE,
			label = "Folder with OME Zarr data:",
			description = "Please, point only on the top level folder of the OME Zarr container.")
	File zarrFolder;

	@Override
	public void run() {
		if (!ZarrOnFSutils.isZarrFolder(zarrFolder.toPath())) return;

		//BdvFunctions.show(singleMI.asDataset(), topLevelPath);
		System.out.println("HELLO FROM BDV OPENER");
	}
}