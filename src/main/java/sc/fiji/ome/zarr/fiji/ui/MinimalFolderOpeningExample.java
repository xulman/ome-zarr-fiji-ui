package sc.fiji.ome.zarr.fiji.ui;

import net.imagej.ImageJ;
import org.scijava.io.AbstractIOPlugin;
import org.scijava.io.IOPlugin;
import org.scijava.io.location.Location;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Plugin;
import org.scijava.ui.SelfShowableContent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Plugin(type = IOPlugin.class, attrs = @Attr(name = "eager"))
public class MinimalFolderOpeningExample extends AbstractIOPlugin<Object> {
	@Override
	public boolean supportsOpen(Location source) {
		System.out.println(this.getClass().getName() + " is considering the file "+source);
		//
		final Path filePath = Paths.get(source.getURI());
		boolean canOpen = Files.isDirectory(filePath) && filePath.toString().endsWith(".xyz");
		System.out.println(this.getClass().getName() + " says canOpen = " + canOpen);
		return canOpen;
	}

	@Override
	public Object open(Location source) throws IOException {
		final String message = this.getClass().getName() + " is opening the file "+source;

		return new SelfShowableContent<>(
				source.toString(),
				m -> System.out.println("I just took care of the .xyz folder: "+m)
		);
	}

	@Override
	public Class<Object> getDataType() {
		return Object.class;
	}

	public static void main(String[] args) {
		final ImageJ ij = new ImageJ();
		ij.ui().showUI();
	}
}
