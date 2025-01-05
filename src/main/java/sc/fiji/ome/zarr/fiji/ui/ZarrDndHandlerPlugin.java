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

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Plugin(type = IOPlugin.class, attrs = @Attr(name = "eager"))
public class ZarrDndHandlerPlugin extends AbstractIOPlugin<Object> implements Runnable {

	// ========================= logging stuff =========================
	@Parameter
	private LogService logService;

	//NB: Change _only_ here to the wanted log level: info() vs. debug()
	private final Reporter log = (msg) -> logService.info(this.getClass().getName()+" "+msg);
	private interface Reporter { void message(String msg); }

	// ========================= IOPlugin stuff =========================
	@Override
	public boolean supportsOpen(Location source) {
		final String sourcePath = source.getURI().getPath();
		log.message("was questioned: "+sourcePath);

		if (!(source instanceof FileLocation)) return false;
		if (!ZarrOnFSutils.isZarrFolder( Paths.get(source.getURI()) )) return false;
		return true;
	}

	@Override
	public Object open(Location source) throws IOException {
		log.message("was asked to open: "+source.getURI().getPath());
		final FileLocation fsource = source instanceof FileLocation ? (FileLocation)source : null;

		//debugging the DnD a bit.... but both tests should never fail
		if (fsource == null) return null;
		if (!ZarrOnFSutils.isZarrFolder(fsource.getFile().toPath())) return null;

		this.droppedInPath = fsource.getFile().toPath();
		//NB: shouldn't be null as fsource is already a valid OME Zarr path (see above)

		//not going to display anything now, we instead start a thread that delays itself a bit
		//and only opens after a waiting period; the waiting period is used to detect whether
		//the ALT key has been released (that is, if it had been pressed during the drag-and-drop operation)
		new Thread(this).start();
		return FAKE_INPUT;
	}

	//the "innocent" product of the (hypothetical) file reading... which Fiji will not display
	private static final Object FAKE_INPUT = new ArrayList<>(0);

	@Override
	public Class<Object> getDataType() {
		return Object.class;
	}

	// ========================= the actual opening of the dropped-in path =========================
	private Path droppedInPath = null;

	private void openRecentlyDroppedPath() {
		//do anything only when the argument is valid
		if (droppedInPath != null) {
			final Path zarrRootPath = ZarrOnFSutils.findRootFolder(droppedInPath);
			final String zarrRootPathAsStr = zarrRootPath.toAbsolutePath().toString();
			log.message("is opening now: " + zarrRootPathAsStr);
		}

		//flag that this argument is processed
		droppedInPath = null;
	}

	private static boolean wasAltKeyDown = false;
	private static final long PERIOD_FOR_DETECTING_ALT_KEY = 2000; //millis

	// ========================= stuff to detect if ALT was pressed during the drag-and-drop =========================
	// ------------------------- keyboard monitor -------------------------
	private static boolean isAlreadyRegisteredKeyHandler = false;

	public ZarrDndHandlerPlugin() {
		super();

		//install a keyboard events monitor, but only once!
		if (!isAlreadyRegisteredKeyHandler) {
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
				if (e.getKeyCode() == KeyEvent.VK_ALT) {
					//...monitor only if the ALT key did some action
					wasAltKeyDown = e.getID() == KeyEvent.KEY_RELEASED;
				}
				return false;
			});
			isAlreadyRegisteredKeyHandler = true;
		}
	}

	// ========================= stuff to detect if ALT was pressed during the drag-and-drop =========================
	// ------------------------- separate thread that fires GUI to keep application's focus to
	//                           allow its keyboard monitor to read-out anything while waiting a bit -------------------------
	@Override
	public void run() {
		wasAltKeyDown = false;
		//NB: this waiting period below is here only to give keyboard events
		//    a chance to notify us that the ALT key has been released
		try { Thread.sleep(PERIOD_FOR_DETECTING_ALT_KEY); } catch (InterruptedException e) { /* empty */ }
		openRecentlyDroppedPath();
	}

}