package gladosPackage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

/**
 * This window is executable and allows the training of a new
 * <code>NeuralNetwork</code> from scratch and following a number of parameters.
 * The NeuralNetwork always has one and only one hidden layer because this has
 * been found to be the quickest and most reliable type of NeuralNetwork.
 * 
 * @author Laty
 *
 */
public class LearningWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	/**
	 * If true, will only create a simple NeuronNetwork to test out logic
	 * functions (AND by default). Allows the testing of the backpropagation
	 * algorithm.
	 */
	public static final boolean SIMPLE = false;
	/**
	 * If true, enables a variable learning rate, following the method given by
	 * the Popescu article.
	 */
	public static final boolean VAR_LEARNING_RATE = false;
	/**
	 * If true, shows the error per epoch of the NeuralNetwork in the console.
	 */
	public static final boolean SHOW_ERROR_PER_EPOCH = true;
	/**
	 * If true, creates a NeuralNetwork which treats preprocessed images
	 * following the method given by the Leon and Sandu article. If false,
	 * simply creates a NeuralNetwork with as much input neurons as pixels.
	 */
	public static final boolean PREPROCESSING = true;
	/**
	 * If true, will show the output of the last test of an epoch in the
	 * console.
	 */
	public static final boolean SHOW_OUTPUT = false;
	/**
	 * If true, will show the image of the last test of an epoch in a window.
	 */
	public static final boolean SHOW_IMAGE = false;
	/**
	 * If false, will replace the standard desired output with intermediate
	 * values to avoid probabilistic conversions (not very useful).
	 */
	public static final boolean STANDARD_OUTPUT = true;
	/**
	 * If false will increment weights after each test rather than after each
	 * epoch.
	 */
	public static final boolean INCREMENT_PER_EPOCH = false;

	/**
	 * The size of the input when <code>PREPROCESSING</code> is true.
	 */
	public static final int INPUT_LENGTH = 100;
	/**
	 * The default learning rate for this learning algorithm.
	 */
	public static final double LEARNING_RATE = 0.0005;
	// private static final double MAX_LR = 2.;
	/**
	 * The first parameter in case of variable learning rate.
	 */
	public static final double DECREASE_LR = 0.8;
	/**
	 * The second parameter in case of variable learning rate.
	 */
	public static final double INCREASE_LR = 1.2;
	/**
	 * The momentum rate of the algorithm which is applied following the method
	 * described in the Popescu article. Can be set to 0. to disable this
	 * modification.
	 */
	public static final double MOMENTUM_RATE = 0.3;
	/**
	 * The size of epochs for the current learning algorithm.
	 */
	public static final int EPOCH_SIZE = 1000;
	
	public static final double  TARGET_ERROR_PER_EPOCH = 35.;
	// private VisualPanel contentPane;
	// private static double[] expectedResult = new double[]{0.8,0.4,0.6};

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// LearningWindow frame = new LearningWindow();
		Path trainImages = FileSystems.getDefault().getPath("src/filesMNIST", "train-images.idx3-ubyte");
		Path trainLabels = FileSystems.getDefault().getPath("src/filesMNIST", "train-labels.idx1-ubyte");
		Preprocessing processedFile;
		if (PREPROCESSING) {
			processedFile = new Preprocessing();
		}
		byte[] rawImagesArray = null;
		byte[] labelsArray = null;
		try {
			rawImagesArray = Files.readAllBytes(trainImages);
			labelsArray = Files.readAllBytes(trainLabels);
			// System.out.println(new String(imagesArray,"ISO-8859-1"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		double errorPerEpoch = Double.MAX_VALUE;
		double lastEpochError = 1.;
		double learningRate = LEARNING_RATE;
		double[] expectedOutput = null;
		double[] input;
		SourceImage currentImage;
		byte[] show = null;

		if (!SIMPLE) {
			JFrame mainwindow = new JFrame();
			if (SHOW_IMAGE && !PREPROCESSING) {

				mainwindow.setSize(300, 200);
				mainwindow.setLocationRelativeTo(null);
				mainwindow.setTitle("Image Preview");
				mainwindow.setVisible(true);
			}
			NeuralNetwork learningNN;
			List<SourceImage> cleanInput;
			if (PREPROCESSING) {
				learningNN = new NeuralNetwork(new int[] { 100, 50, 10 }, learningRate);
				cleanInput = recreateCleanInput(processedFile.getFeatures(), processedFile.getExpectedOutputs());
			} else {
				learningNN = new NeuralNetwork(new int[] { 28 * 28, 100, 40, 10 }, learningRate);
				cleanInput = createCleanInput(rawImagesArray, labelsArray);
			}
			while(errorPerEpoch>TARGET_ERROR_PER_EPOCH){
				for (int i = 0; i < cleanInput.size() / EPOCH_SIZE; i++) {
					errorPerEpoch = 0.;
					for (int j = 0; j < EPOCH_SIZE; j++) {
						currentImage = cleanInput.get(i * EPOCH_SIZE + j);
						show = currentImage.getCleanRawImage();
						if (PREPROCESSING) {
							input = currentImage.getRelevantFeatures();
						} else {
							input = currentImage.getCleanRawDoubleImage();
						}
						learningNN.setInputs(input);
						learningNN.fire();
						expectedOutput = currentImage.getExpectedOutput();
						learningNN.calculateNeuronDiffs(expectedOutput);
						learningNN.incrementWeightDiffs();
						if (!INCREMENT_PER_EPOCH) {
							learningNN.incrementWeights();
							learningNN.resetWeightDiffsMomentum(MOMENTUM_RATE);
						}
						errorPerEpoch += currentError(expectedOutput, learningNN.getOutputs());
					}
					if (SHOW_IMAGE && !PREPROCESSING) {
						mainwindow.setContentPane(new ImageDisplayPanel(show));
						mainwindow.repaint();
	
						mainwindow.revalidate();
					}
					if (SHOW_OUTPUT) {
						System.out.println(learningNN.getOutputs());
					}
	
					learningNN.incrementWeights();
					learningNN.resetWeightDiffsMomentum(MOMENTUM_RATE);
					if (SHOW_ERROR_PER_EPOCH) {
						System.out.println(errorPerEpoch);
					}
					if (VAR_LEARNING_RATE) {
						if (errorPerEpoch > lastEpochError) {
							learningNN.resetLR();
						} else {
							learningNN.varyLR();
						}
					}
					lastEpochError = errorPerEpoch;
					
				}
			}
			try {
				FileOutputStream fileOut;
				if (PREPROCESSING) {
					fileOut = new FileOutputStream("src/resultingNN/latest.pre");
				} else {
					fileOut = new FileOutputStream("src/resultingNN/latest.rw");
				}
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(learningNN);
				out.close();
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		else {
			NeuralNetwork learningNN = new NeuralNetwork(new int[] { 2, 3, 4, 1 }, learningRate);
			for (int i = 0; i < 10000 / EPOCH_SIZE; i++) {
				for (int j = 0; j < EPOCH_SIZE; j++) {
					double inputA = Math.floor(Math.random() * 2);
					double inputB = Math.floor(Math.random() * 2);
					learningNN.setInputs(new double[] { inputA, inputB });
					learningNN.fire();
					if (inputA == inputB) {
						expectedOutput = new double[] { inputA };
					} else {
						expectedOutput = new double[] { inputA + inputB };
					}
					learningNN.calculateNeuronDiffs(expectedOutput);
					learningNN.incrementWeightDiffs();

					// System.out.println("Got "+ learningNN.getOutputs() + "
					// instead of "+ expectedOutput[0]);
					learningNN.incrementWeights();
					learningNN.resetWeightDiffsMomentum(MOMENTUM_RATE);
					errorPerEpoch += currentError(expectedOutput, learningNN.getOutputs());
				}
				// learningNN.incrementWeights();
				// //learningNN.resetWeightDiffs();
				// learningNN.resetWeightDiffsMomentum(MOMENTUM_RATE);
				if (SHOW_ERROR_PER_EPOCH) {
					System.out.println(errorPerEpoch);
				}
				errorPerEpoch = 0.;

			}
		}
	}

	private static double currentError(double[] expectedOutput, List<Double> outputs) {
		double res = 0;
		for (int i = 0; i < expectedOutput.length; i++) {
			res += (expectedOutput[i] - outputs.get(i)) * (expectedOutput[i] - outputs.get(i));
		}
		res = res / 2.;
		return res;
	}

	public LearningWindow() {

		setTitle("Visualisation");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setUndecorated(true);
		// contentPane.setBackground(Color.ORANGE);
		// setContentPane(contentPane);
		// setVisible(true);

	}

	public static double[] createInput(int imageNumber, byte[] imagesArray) {
		double[] res = new double[784];
		;
		for (int i = 0; i < 28 * 28; i++) {
			res[i] = (imagesArray[16 + imageNumber * (28 * 28) + i]) / 128.;
		}
		return res;
	}

	/**
	 * Creates an <code>ArrayList</code> of <code>SourceImage</code>s following
	 * two byte arrays.
	 * 
	 * @param rawImagesArray
	 * @param labelsArray
	 * @return
	 */
	public static List<SourceImage> createCleanInput(byte[] rawImagesArray, byte[] labelsArray) {
		List<SourceImage> res = new ArrayList<SourceImage>();
		int imageSize = 28;
		int init = 16;
		int c = 0;

		while (init + (imageSize * imageSize) * c < rawImagesArray.length) {
			byte[] temp = Arrays.copyOfRange(rawImagesArray, init + (imageSize * imageSize * c),
					init + imageSize * imageSize * (c + 1));

			res.add(new SourceImage(temp, imageSize, labelsArray[8 + c]));
			c++;
		}
		return res;
	}

	/**
	 * Recreates an <code>ArrayList</code> of <code>SourceImage</code>s
	 * following the processed data from features and expectedOutputs. As such
	 * the SourceImages do not actually have the image code, only the extracted
	 * features.
	 * 
	 * @param features
	 * @param expectedOutputs
	 * @return
	 */
	private static List<SourceImage> recreateCleanInput(double[][] features, double[][] expectedOutputs) {

		List<SourceImage> res = new ArrayList<SourceImage>();
		for (int i = 0; i < features.length; i++) {
			res.add(new SourceImage(features[i], expectedOutputs[i]));
		}

		return res;
	}

}
