package sc.fiji.ome.zarr.fiji.ui;

import net.imagej.ImageJ;

public class DndHandler {

	public static void main(String[] args) {
		ImageJ ij = new ImageJ();
		ij.ui().showUI();
		ij.ui().show("try drag-and-dropping a .zarr file....");
	}
}