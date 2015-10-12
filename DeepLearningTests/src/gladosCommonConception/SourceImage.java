package gladosCommonConception;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class is used to extract relevant features from the byte array given to
 * the constructor
 * 
 * @author Laty
 *
 */
public class SourceImage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5897815992815817720L;
	private byte[] rawImage;
	private byte[][] matrixImage;
	private double[][] matrixTransformed;
	private double[] relevantFeatures;
	private int imageSize;
	private byte label;

	/**
	 * Creates the source image from <code>rawImage</code> but does not extract
	 * relevant information which makes the construction much less costly
	 * 
	 * @param rawImage
	 * @param imageSize
	 */
	public SourceImage(byte[] rawImage, int imageSize, byte label) {
		this.label = label;
		if (rawImage.length != imageSize * imageSize) {
			System.out.println("Wrong imageSize");
		}
		this.rawImage = rawImage;
		correctRawImage();
		this.imageSize = imageSize;
		buildMatrix();
	}

	public SourceImage(double[] relevantFeatures, double[] expectedOutput) {
		this.relevantFeatures = relevantFeatures;
		for (int i = 0; i < expectedOutput.length; i++) {
			if (expectedOutput[i] != 0) {
				label = (byte) i;
			}
		}
	}

	private void correctRawImage() {
		for (int i = 0; i < rawImage.length; i++) {
			if (rawImage[i] != 0) {
				rawImage[i] = (byte) (rawImage[i] + 128);
			}
		}
	}

	public byte[] getCleanRawImage() {
		return rawImage;
	}

	public double[] getCleanRawDoubleImage() {
		double[] res = new double[rawImage.length];
		for (int i = 0; i < rawImage.length; i++) {
			res[i] = rawImage[i];
		}
		return res;
	}

	/**
	 * Returns a normalized version of the rawImage
	 * 
	 * @return
	 */
	public double[] getNormalizedRaw() {
		double[] res = new double[rawImage.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = rawImage[i] / 128.;
		}
		return res;
	}

	/**
	 * Used in the matrix transformation
	 * 
	 * @param x
	 * @return
	 */
	private double cFunc(double x) {
		if (x == 0) {
			return 1. / (Math.sqrt(2));
		}
		return 1.;
	}

	/**
	 * Called by the constructor, builds the matrix for the array
	 */
	private void buildMatrix() {
		matrixImage = new byte[imageSize][imageSize];
		for (int i = 0; i < imageSize; i++) {
			for (int j = 0; j < imageSize; j++) {
				matrixImage[i][j] = rawImage[i * imageSize + j];
			}
		}
	}

	/**
	 * Used by <code>transformMatrix()</code>.
	 * 
	 * @param u
	 * @param v
	 * @return
	 */
	private double sumFactor(int u, int v) {
		double res = 0.;
		for (int y = 0; y < imageSize; y++) {
			for (int x = 0; x < imageSize; x++) {
				res += matrixImage[x][y] * Math.cos((2 * x + 1) * u * Math.PI / (2 * imageSize))
						* Math.cos((2 * y + 1) * v * Math.PI / (2 * imageSize));
			}
		}
		return res;
	}

	/**
	 * Transforms the <code>matrixImage</code>. Very costly, called by
	 * <code>getRelevantFeatures()</code>
	 */
	private void transformMatrix() {
		matrixTransformed = new double[imageSize][imageSize];
		for (int u = 0; u < imageSize; u++) {
			for (int v = 0; v < imageSize; v++) {
				matrixTransformed[u][v] = 2 * cFunc(u) * cFunc(v) * sumFactor(u, v) / imageSize;
			}
		}
	}

	/**
	 * Extracts the relevant features from the SourceImage in an array of
	 * <code>length</code> length and stores it.
	 * 
	 * @param length
	 */
	public void buildRelevantFeatures(int length) {
		transformMatrix();
		double[] temp = new double[imageSize * imageSize];
		int i = 1;
		int j = 1;
		for (int element = 0; element < imageSize * imageSize; element++) {
			temp[element] = matrixTransformed[i - 1][j - 1];
			if ((i + j) % 2 == 0) {
				// Even stripes
				if (j < imageSize)
					j++;
				else
					i += 2;
				if (i > 1)
					i--;
			} else {
				// Odd stripes
				if (i < imageSize)
					i++;
				else
					j += 2;
				if (j > 1)
					j--;
			}
		}
		relevantFeatures = Arrays.copyOfRange(temp, 0, length);
	}

	/**
	 * Returns the relevant features of the <code>SourceImage</code>
	 * 
	 * @return
	 */
	public double[] getRelevantFeatures() {
		return relevantFeatures;
	}

	/**
	 * Returns the corresponding correct label for this image.
	 * 
	 * @return
	 */
	public byte getLabel() {
		return label;
	}

	public double[] getExpectedOutput() {
		double[] res;
		res = new double[10];
		res[label] = 1.;
		return res;
	}
}
