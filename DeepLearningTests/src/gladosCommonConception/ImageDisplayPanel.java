package gladosCommonConception;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JPanel;

/**
 * A JPanel which automatically displays one of the MNIST images, depending on
 * its constructor, either by finding the image associated with an image number
 * or by creating the image from a byte array.
 * 
 * @author Laty
 *
 */
public class ImageDisplayPanel extends JPanel {

	private int[][] imageMatrix = new int[28][28];

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageDisplayPanel(int imageNumber) {
		super();
		this.setPreferredSize(new Dimension(250, 250));
		Path trainImages = FileSystems.getDefault().getPath("src/filesMNIST", "train-images.idx3-ubyte");
		byte[] imagesArray;
		try {
			imagesArray = Files.readAllBytes(trainImages);

			int c = imageNumber;
			int start = 16 + c * (28 * 28);
			for (int i = start; i < (28 * 28) + start; i++) {
				// System.out.println(imagesArray[i]);
				imageMatrix[(i - start) % 28][(i - start) / 28] = (int) imagesArray[i];
			}
			// System.out.println(labelsArray[8+c]);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ImageDisplayPanel(byte[] show) {
		super();

		for (int i = 0; i < (28 * 28); i++) {
			// System.out.println(imagesArray[i]);
			imageMatrix[i % 28][i / 28] = (int) show[i];
		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int value;
		int bias = 50;
		for (int i = 0; i < imageMatrix.length; i++) {
			for (int j = 0; j < imageMatrix[i].length; j++) {
				if (imageMatrix[i][j] != 0) {
					value = 127 - imageMatrix[i][j];
				} else {
					value = 255;
				}

				g.setColor(new Color(value, value, value));
				g.fillRect(bias + 5 * i, bias + 5 * j, 5, 5);
			}
		}
	}
}
