package sc.fiji.ome.zarr.fiji.ui;

import net.imagej.Dataset;
import net.imagej.DefaultDataset;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.GenericByteType;
import net.imglib2.view.Views;
import org.scijava.io.AbstractIOPlugin;
import org.scijava.io.IOPlugin;
import org.scijava.io.location.Location;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Plugin(type = IOPlugin.class, attrs = @Attr(name = "eager"))
public class MinimalFolderOpeningExample extends AbstractIOPlugin<Dataset> {
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
	public Dataset open(Location source) throws IOException {
		final String message = this.getClass().getName() + " is opening the file "+source;

		//assuming one can "extract" an image out of the folder:
		final Img<ByteType> anImg = ArrayImgs.bytes(110, 90);
		Views.interval(anImg,new long[] {25,20}, new long[] {30,25}).forEach(GenericByteType::setOne);
		Views.interval(anImg,new long[] {80,20}, new long[] {85,25}).forEach(GenericByteType::setOne);
		Views.interval(anImg,new long[] {20,53}, new long[] {40,59}).forEach(GenericByteType::setOne);
		Views.interval(anImg,new long[] {41,60}, new long[] {69,70}).forEach(GenericByteType::setOne);
		Views.interval(anImg,new long[] {70,53}, new long[] {90,59}).forEach(GenericByteType::setOne);

		return new DefaultDataset(
				this.getContext(),
				new ImgPlus<>(anImg, "image from the folder " + source.toString())
		);
	}

	@Override
	public Class<Dataset> getDataType() {
		return Dataset.class;
	}

	public static void main(String[] args) {
		final ImageJ ij = new ImageJ();
		ij.ui().showUI();
	}
}
