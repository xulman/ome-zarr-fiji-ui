package sc.fiji.ome.zarr.fiji.ui;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath = "Plugins > DatasetDisplay")
public class DatasetDisplay implements Command {
	@Parameter
	Dataset ds;

	@Override
	public void run() {
		System.out.println("got dataset: "+ds);
	}

	public static void main(String[] args) {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();
	}
}
