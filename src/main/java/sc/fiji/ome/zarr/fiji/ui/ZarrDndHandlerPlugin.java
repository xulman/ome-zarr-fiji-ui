package sc.fiji.ome.zarr.fiji.ui;

import org.janelia.saalfeldlab.n5.ij.N5Importer;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.log.LogService;

import org.scijava.io.IOPlugin;
import org.scijava.io.AbstractIOPlugin;
import org.scijava.io.location.FileLocation;
import org.scijava.io.location.Location;
import sc.fiji.ome.zarr.fiji.ui.util.ZarrOnFSutils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Plugin(type = IOPlugin.class, attrs = @Attr(name = "eager"))
public class ZarrDndHandlerPlugin extends AbstractIOPlugin<Object> {

	@Parameter
	private LogService logService;

	@Override
	public boolean supportsOpen(Location source) {
		final String sourcePath = source.getURI().getPath();
		logService.info(this.getClass().getName()+" was questioned: "+sourcePath);

		if (!(source instanceof FileLocation)) return false;
		if ( !ZarrOnFSutils.isZarrFolder( Paths.get(source.getURI()) ) ) return false;
		return true;
	}

	@Override
	public Object open(Location source) throws IOException {
		logService.info(this.getClass().getName()+" was asked to open: "+source.getURI().getPath());
		final FileLocation fsource = source instanceof FileLocation ? (FileLocation)source : null;

		//debugging the DnD a bit.... as both tests should never fail
		if (fsource == null) return null;
		if (!ZarrOnFSutils.isZarrFolder(fsource.getFile().toPath())) return null;

		final Path droppedPath = fsource.getFile().toPath();
		final Path zarrRootPath = ZarrOnFSutils.findRootFolder(droppedPath);
		//NB: shouldn't be null as fsource is already a valid OME Zarr path (see above)

		final String zarrRootStrPath = zarrRootPath.toAbsolutePath().toString();
		logService.info(this.getClass().getName()+" is going to open: "+zarrRootStrPath);

		N5Importer importer = new N5Importer();
		importer.runWithDialog(zarrRootStrPath, ZarrOnFSutils.listPathDifferences(droppedPath,zarrRootPath));

		logService.info(this.getClass().getName()+" opened.");
		return FAKE_INPUT;
	}

	//the "innocent" product of the (hypothetical) file reading... which Fiji will not display
	private static final Object FAKE_INPUT = new ArrayList<>(0);

	@Override
	public Class<Object> getDataType() {
		return Object.class;
	}
}