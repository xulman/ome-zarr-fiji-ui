package sc.fiji.ome.zarr.fiji.ui;

import org.scijava.io.AbstractIOPlugin;
import org.scijava.io.IOPlugin;
import org.scijava.io.location.Location;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Plugin;
import org.scijava.ui.SelfShowableContent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Plugin(type = IOPlugin.class, attrs = @Attr(name = "eager"))
public class MinimalFolderOpeningExample extends AbstractIOPlugin<Object> {
	@Override
	public boolean supportsOpen(Location source) {
		// I only work with folders.. and steal them away
		// from native ImageJ opener (aka Image Sequence reader)
		return Files.isDirectory( Paths.get(source.getURI()) );
	}

	@Override
	public Object open(Location source) throws IOException {
		final String message = this.getClass().getName() + " is opening the folder "+source;
		System.out.println("Only a proof I was here...");
		return new SelfShowableContent<>(message, m -> System.out.println("message was: "+m));
	}

	@Override
	public Class<Object> getDataType() {
		return Object.class;
	}
}
