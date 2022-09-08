package sc.fiji.ome.zarr.fiji.ui;

import org.scijava.display.Display;
import org.scijava.plugin.Plugin;
import org.scijava.ui.dnd.AbstractDragAndDropHandler;
import org.scijava.ui.dnd.DragAndDropHandler;

@Plugin(type = DragAndDropHandler.class) //possible option: priority = Priority.NORMAL
public class DndHandler2 extends AbstractDragAndDropHandler<String>
{
	@Override
	public boolean supports(final String file) {
		if (file == null) return false;

		//refuse to handle if outside my responsibility scope
		if (!file.endsWith(".vu")) return false;
		System.out.println("I'm supporting this");

		return true;
	}

	@Override
	public boolean drop(final String file, final Display<?> display) {
		//process the 'file' and signal if it went well
		System.out.println("I'm processing this");
		return true;
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}
}