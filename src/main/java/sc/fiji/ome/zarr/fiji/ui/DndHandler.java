package sc.fiji.ome.zarr.fiji.ui;

import net.imagej.legacy.ui.LegacyUI;

import org.scijava.Context;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import org.scijava.io.IOPlugin;
import org.scijava.io.AbstractIOPlugin;
import org.scijava.io.location.FileLocation;
import org.scijava.io.location.Location;
import org.scijava.ui.UIService;

import java.io.IOException;
import java.util.ArrayList;

@Plugin(type = IOPlugin.class)
public class DndHandler extends AbstractIOPlugin<Object> {

	@Parameter
	private LogService logService;

	@Override
	public boolean supportsOpen(Location source) {
		logService.info("DnDHandler was questioned: "+source.getURI().getPath());
		if (!(source instanceof FileLocation)) return false;
		if (!(source.getName().endsWith(".zarr"))) return false;
		return true;
	}

	@Override
	public Object open(Location source) throws IOException {
		logService.info("DnDHandler was asked to open: "+source.getURI().getPath());
		final FileLocation fsource = source instanceof FileLocation ? (FileLocation)source : null;
		if (fsource == null) return null; //NB: shouldn't happen... (in theory)

		logService.info("DnDHandler is going to open: "+fsource.getFile().getAbsolutePath());
		//
		//we've been given a .zarr _file_, a special "tag" file inside .zarr,
		//its parent folder is the true "handle" of this .zarr
		new DialogAroundZarr(fsource.getFile().getParentFile().toPath());
		return FAKE_INPUT;
	}

	//the "innocent" product of the (hypothetical) file reading...
	private static final Object FAKE_INPUT = new ArrayList<>(0);

	@Override
	public Class<Object> getDataType() {
		return Object.class;
	}


	//--------------------------------------------------------
	public static void main(String[] args) {
		final Context context = new Context();
		context.service( UIService.class ).showUI( LegacyUI.NAME );
	}
}