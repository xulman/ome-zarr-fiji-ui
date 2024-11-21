package sc.fiji.ome.zarr.fiji.ui.util;

import java.nio.file.Files;
import java.nio.file.Path;

public class ZarrOnFSutils {
	/**
	 * Checks if under the given folder there exists any of
	 * the files: .zgroup, .zarray or zarr.json.
	 *
	 * @param zarrFolder Supposedly the top-level Zarr folder.
	 * @return True if some of the three files is found.
	 */
	public static boolean isZarrFolder(final Path zarrFolder) {
		return ( Files.exists( zarrFolder.resolve( ".zgroup" ) ) || //Zarr v2
				  Files.exists( zarrFolder.resolve( ".zarray" ) ) || //Zarr v2
				  Files.exists( zarrFolder.resolve("zarr.json") ) ); //Zarr v3
	}

	/**
	 * Traverses up the folders tree as long as {@link ZarrOnFSutils#isZarrFolder(Path)}
	 * says we are inside a Zarr dataset. The last such folder is returned, which is
	 * supposed to be the top-level/root folder of the pointed at dataset.
	 *
	 * @param somewhereInZarrFolder Pointer (folder) to somewhere inside an OME Zarr.
	 * @return Root of that OME Zarr, or NULL if the provided path is NOT within an OME Zarr.
	 */
	public static Path findRootFolder(final Path somewhereInZarrFolder) {
		Path parentFolder = somewhereInZarrFolder;
		Path lastValidFolder = null;

		while (isZarrFolder(parentFolder)) {
			lastValidFolder = parentFolder;
			parentFolder = parentFolder.getParent();
		}

		return lastValidFolder;
	}
}
