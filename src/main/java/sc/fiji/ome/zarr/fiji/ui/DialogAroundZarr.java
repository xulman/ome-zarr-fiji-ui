package sc.fiji.ome.zarr.fiji.ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class DialogAroundZarr {
	public DialogAroundZarr(final Path zarrFolder) {
		if (!Files.isDirectory(zarrFolder))
			throw new IllegalArgumentException("Cannot access "
					+zarrFolder.toAbsolutePath()+" or it is not a folder."
					+" I need a top-level _folder_ to open.");

		if (!zarrFolder.toString().endsWith(".ome.zarr"))
			throw new IllegalArgumentException("The folder "
					+zarrFolder.toAbsolutePath()+" is likely not a .ome.zarr");

		System.out.println("OPENING ZARR FOLDER:"+zarrFolder.toAbsolutePath());
		//TBA! :-)
	}


	//--------------------------------------------------------
	public static void main(String[] args) {
		new DialogAroundZarr(Paths.get("/home/ulman/M_TRIF_testSmall/orig/trif022.ome.zarr"));
	}
}
