package sc.fiji.ome.zarr.fiji.ui;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.axis.CalibratedAxis;
import org.scijava.command.Command;
import org.scijava.display.DisplayService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

@Plugin(type = Command.class, menuPath = "Plugins > DatasetDisplay")
public class DatasetDisplay implements Command {
	@Parameter
	Dataset dataset;

	@Parameter
	UIService uiService;

	@Override
	public void run() {
		// reporting
		System.out.println("got dataset >>" + dataset + "<< with pixel type >>" + dataset.getTypeLabelShort() + "<<");
		System.out.println("width: " + dataset.getWidth());
		System.out.println("height: " + dataset.getHeight());
		System.out.println("depth: " + dataset.getDepth());
		System.out.println("frames: " + dataset.getFrames());
		System.out.println("channels: " + dataset.getChannels());
		for (int i = 0; i < dataset.numDimensions(); ++i) {
			CalibratedAxis a = dataset.axis(i);
			System.out.println("dimension " + i + ": " + dataset.dimension(i) + " pixels; 1 px = "
				+ a.calibratedValue(1.0) + " " + a.unit());
		}
		System.out.println("isDirty = " + dataset.isDirty());

		//showing:
		uiService.show(dataset);
	}

	public static void main(String[] args) {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();
	}
}
