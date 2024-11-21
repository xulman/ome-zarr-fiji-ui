package sc.fiji.ome.zarr.fiji.ui;

import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.log.LogService;
import org.scijava.command.CommandService;
import org.scijava.prefs.PrefService;

import org.scijava.io.IOPlugin;
import org.scijava.io.AbstractIOPlugin;
import org.scijava.io.location.FileLocation;
import org.scijava.io.location.Location;
import sc.fiji.ome.zarr.fiji.ui.util.ZarrOnFSutils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

@Plugin(type = IOPlugin.class, attrs = @Attr(name = "eager"))
public class ZarrDndHandlerPlugin extends AbstractIOPlugin<Object> {

	@Parameter
	private LogService logService;

	@Parameter
	private CommandService cmdService;

	@Parameter
	private PrefService prefService;

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

		logService.info(this.getClass().getName()+" is going to open: "+fsource.getFile().getAbsolutePath());
		final String futureDialogAction =
				prefService.get(ZarrOpenDialogPlugin.class, "futureDialogAction", ZarrOpenDialogPlugin.DIAG_ALWAYS_ASK);
		if (futureDialogAction.equals(ZarrOpenDialogPlugin.DIAG_ALWAYS_ASK)) {
			cmdService.run(ZarrOpenDialogPlugin.class,true, "zarrFolder", fsource.getFile() );
		} else {
			boolean showInIJ  = prefService.getBoolean(ZarrOpenDialogPlugin.class, "openInIJ", true);
			boolean showInBDV = prefService.getBoolean(ZarrOpenDialogPlugin.class, "openInBDV", true);
			ZarrOpenDialogPlugin.openZarr(prefService.context(), fsource.getFile().getAbsolutePath(), showInIJ, showInBDV);
		}

		return FAKE_INPUT;
	}

	//the "innocent" product of the (hypothetical) file reading... which Fiji will not display
	private static final Object FAKE_INPUT = new ArrayList<>(0);

	@Override
	public Class<Object> getDataType() {
		return Object.class;
	}
}