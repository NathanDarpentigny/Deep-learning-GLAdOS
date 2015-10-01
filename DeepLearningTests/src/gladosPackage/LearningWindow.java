package gladosPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import javax.swing.DefaultComboBoxModel;

/**
 * This window is executable and allows the training of a new
 * <code>NeuralNetwork</code> from scratch and following a number of parameters.
 * The NeuralNetwork always has one and only one hidden layer because this has
 * been found to be the quickest and most reliable type of NeuralNetwork.
 * 
 * @author Laty
 *
 */
public class LearningWindow extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	/**
	 * If true, will only create a simple NeuronNetwork to test out logic
	 * functions (AND by default). Allows the testing of the backpropagation
	 * algorithm.
	 */
	private static final boolean SIMPLE = false;
	/**
	 * If true, enables a variable learning rate, following the method given by
	 * the Popescu article.
	 */
	private static final boolean VAR_LEARNING_RATE = true;
	/**
	 * If true, shows the error per epoch of the NeuralNetwork in the console.
	 */
	private static final boolean SHOW_ERROR_PER_EPOCH = true;
	/**
	 * If true, creates a NeuralNetwork which treats preprocessed images
	 * following the method given by the Leon and Sandu article. If false,
	 * simply creates a NeuralNetwork with as much input neurons as pixels.
	 */
	private static final boolean PREPROCESSING = false;
	/**
	 * If true, will show the output of the last test of an epoch in the
	 * console.
	 */
	private static final boolean SHOW_OUTPUT = false;
	/**
	 * If true, will show the image of the last test of an epoch in a window.
	 */
	private static final boolean SHOW_IMAGE = false;
	/**
	 * If false, will replace the standard desired output with intermediate
	 * values to avoid probabilistic conversions (not very useful).
	 */
	private static final boolean STANDARD_OUTPUT = true;
	/**
	 * If false will increment weights after each test rather than after each
	 * epoch.
	 */
	private static final boolean INCREMENT_PER_EPOCH = false;

	/**
	 * The size of the input when <code>PREPROCESSING</code> is true.
	 */
	private static final int INPUT_LENGTH = 100;
	/**
	 * The default learning rate for this learning algorithm.
	 */
	private static final double LEARNING_RATE = 0.00001;
	// private static final double MAX_LR = 2.;
	/**
	 * The first parameter in case of variable learning rate.
	 */
	private static final double DECREASE_LR = 0.8;
	/**
	 * The second parameter in case of variable learning rate.
	 */
	private static final double INCREASE_LR = 1.5;
	/**
	 * The momentum rate of the algorithm which is applied following the method
	 * described in the Popescu article. Can be set to 0. to disable this
	 * modification.
	 */
	private static final double MOMENTUM_RATE = 0.3;
	/**
	 * The size of epochs for the current learning algorithm.
	 */
	private static final int EPOCH_SIZE = 1000;

	private static final double TARGET_ERROR_PER_EPOCH = 80.;
	private JTextField txtDefaultLearningRate;
	private JTextField txtEpochSize;
	private JTextField txtTargetErrorPer;
	private JTextField txtIncreaseAndDecrease;
	private JTextField txtMomentumFactor;
	private XYSeriesCollection learningData;
	private XYSeries series = new XYSeries("");
	private ChartPanel graphPanel;
	// private VisualPanel contentPane;
	// private static double[] expectedResult = new double[]{0.8,0.4,0.6};

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		LearningWindow frame = new LearningWindow("Test");
		// frame.learningAlg(SIMPLE, VAR_LEARNING_RATE, PREPROCESSING,
		// INCREMENT_PER_EPOCH, LEARNING_RATE, DECREASE_LR, INCREASE_LR,
		// MOMENTUM_RATE, EPOCH_SIZE,TARGET_ERROR_PER_EPOCH);
	}

	@SuppressWarnings("unused")
	public void learningAlg(boolean simple, boolean variableLR, boolean preprocessing, boolean incrementPerEpoch,
			double defaultLR, double decreaseLR_factor, double increaseLR_factor, double momentumRate, int epochSize,
			double targetError) {
		Path trainImages = FileSystems.getDefault().getPath("src/filesMNIST", "train-images.idx3-ubyte");
		Path trainLabels = FileSystems.getDefault().getPath("src/filesMNIST", "train-labels.idx1-ubyte");
		Preprocessing processedFile = null;
		if (preprocessing) {
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
		double learningRate = defaultLR;
		double[] expectedOutput = null;
		double[] input;
		SourceImage currentImage;
		byte[] show = null;
		int epochNumber = 0;
		
		if (!simple) {
			JFrame mainwindow = new JFrame();
			if (SHOW_IMAGE && !preprocessing) {

				mainwindow.setSize(300, 200);
				mainwindow.setLocationRelativeTo(null);
				mainwindow.setTitle("Image Preview");
				mainwindow.setVisible(true);
			}
			NeuralNetwork learningNN;
			List<SourceImage> cleanInput;
			if (preprocessing) {
				learningNN = new NeuralNetwork(new int[] { 100, 50, 10 }, learningRate);
				cleanInput = recreateCleanInput(processedFile.getFeatures(), processedFile.getExpectedOutputs());
			} else {
				learningNN = new NeuralNetwork(new int[] { 28 * 28, 100, 40, 10 }, learningRate);
				cleanInput = createCleanInput(rawImagesArray, labelsArray);
			}
			while (errorPerEpoch > targetError) {
				for (int i = 0; i < cleanInput.size() / epochSize; i++) {
					errorPerEpoch = 0.;
					for (int j = 0; j < epochSize; j++) {
						currentImage = cleanInput.get(i * epochSize + j);
						show = currentImage.getCleanRawImage();
						if (preprocessing) {
							input = currentImage.getRelevantFeatures();
						} else {
							input = currentImage.getCleanRawDoubleImage();
						}
						learningNN.setInputs(input);
						learningNN.fire();
						expectedOutput = currentImage.getExpectedOutput();
						learningNN.calculateNeuronDiffs(expectedOutput);
						learningNN.incrementWeightDiffs();
						if (!incrementPerEpoch) {
							learningNN.incrementWeights();
							learningNN.resetWeightDiffsMomentum(momentumRate);
						}
						errorPerEpoch += currentError(expectedOutput, learningNN.getOutputs());
					}
					if (SHOW_IMAGE && !preprocessing) {
						mainwindow.setContentPane(new ImageDisplayPanel(show));
						mainwindow.repaint();

						mainwindow.revalidate();
					}
					if (SHOW_OUTPUT) {
						//System.out.println(learningNN.getOutputs());
					}
					
					epochNumber++;
					//System.out.println(errorNumber);
					series.add((double)epochNumber,errorPerEpoch);
					
					learningData = new XYSeriesCollection(series);
					JFreeChart chart = ChartFactory.createXYLineChart("Quadratic error per epoch", "Epoch number", "Error per epoch", learningData);
					graphPanel.setChart(chart);
					update(getGraphics());
					revalidate();
					
			
					//graphPanel.revalidate();
					
					learningNN.incrementWeights();
					learningNN.resetWeightDiffsMomentum(momentumRate);
					
					if (variableLR) {
						if (errorPerEpoch > lastEpochError) {
							learningNN.resetLR();
						} else {
							learningNN.varyLR(decreaseLR_factor, increaseLR_factor);
						}
					}
					lastEpochError = errorPerEpoch;
					
				}
				
			}
		
			update(getGraphics());
			revalidate();
			try {
				FileOutputStream fileOut;
				if (preprocessing) {
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
			for (int i = 0; i < 10000 / epochSize; i++) {
				for (int j = 0; j < epochSize; j++) {
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
					learningNN.resetWeightDiffsMomentum(momentumRate);
					errorPerEpoch += currentError(expectedOutput, learningNN.getOutputs());
				}
				// learningNN.incrementWeights();
				// //learningNN.resetWeightDiffs();
				// learningNN.resetWeightDiffsMomentum(MOMENTUM_RATE);
				if (SHOW_ERROR_PER_EPOCH) {
					//System.out.println(errorPerEpoch);
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

	public LearningWindow(final String title) {
		super(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Visualisation");
		setSize(500, 528);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JComboBox simpleBox = new JComboBox();
		simpleBox.setModel(new DefaultComboBoxModel(new String[] {"Digit Recognition", "Simple Logic Functon Recognition"}));
		simpleBox.setBounds(10, 22, 218, 20);
		getContentPane().add(simpleBox);
		
		learningData = new XYSeriesCollection(series);
		JFreeChart chart = ChartFactory.createXYLineChart("Quadratic error per epoch", "Epoch number", "Error per epoch", learningData);
		
		graphPanel = new ChartPanel(chart);
		graphPanel.setReshowDelay(1);
		graphPanel.setRefreshBuffer(true);
		
		graphPanel.setBounds(10, 189, 464, 279);
		getContentPane().add(graphPanel);

		JRadioButton rdbtnPreprocessing = new JRadioButton("Preprocessing");
		rdbtnPreprocessing.setSelected(true);
		rdbtnPreprocessing.setBounds(10, 49, 139, 23);
		getContentPane().add(rdbtnPreprocessing);

		JRadioButton rdbtnIncrementEveryTime = new JRadioButton("Increment every time");
		rdbtnIncrementEveryTime.setSelected(true);
		rdbtnIncrementEveryTime.setBounds(10, 75, 139, 23);
		getContentPane().add(rdbtnIncrementEveryTime);

		JSpinner spinnerDecreaseLR = new JSpinner();
		spinnerDecreaseLR.setModel(new SpinnerNumberModel(0.8, 0.0, 1.0, 1.0));
		spinnerDecreaseLR.setBounds(368, 102, 46, 20);
		getContentPane().add(spinnerDecreaseLR);

		JSpinner spinnerIncreaseLR = new JSpinner();
		spinnerIncreaseLR.setModel(new SpinnerNumberModel(1.2, new Double(1), null, new Double(0)));
		spinnerIncreaseLR.setBounds(427, 102, 47, 20);
		getContentPane().add(spinnerIncreaseLR);

		

		JRadioButton rdbtnAdaptableLearningRate = new JRadioButton("Adaptable learning rate");
		rdbtnAdaptableLearningRate.setSelected(true);
		rdbtnAdaptableLearningRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtIncreaseAndDecrease.setEnabled(rdbtnAdaptableLearningRate.isSelected());
				spinnerIncreaseLR.setEnabled(rdbtnAdaptableLearningRate.isSelected());
				spinnerDecreaseLR.setEnabled(rdbtnAdaptableLearningRate.isSelected());

			}
		});
		rdbtnAdaptableLearningRate.setBounds(10, 101, 139, 23);
		getContentPane().add(rdbtnAdaptableLearningRate);

		JSpinner spinnerMomentumFact = new JSpinner();
		spinnerMomentumFact.setModel(new SpinnerNumberModel(0.4, 0.0, 1.0, .1));
		spinnerMomentumFact.setBounds(417, 128, 57, 20);
		getContentPane().add(spinnerMomentumFact);

		

		JRadioButton rdbtnMomentum = new JRadioButton("Momentum");
		rdbtnMomentum.setSelected(true);
		rdbtnMomentum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtMomentumFactor.setEnabled(rdbtnMomentum.isSelected());
				spinnerMomentumFact.setEnabled(rdbtnMomentum.isSelected());
			}
		});
		rdbtnMomentum.setBounds(10, 127, 109, 23);
		getContentPane().add(rdbtnMomentum);

		JSpinner spinnerLR = new JSpinner();
		spinnerLR.setModel(new SpinnerNumberModel(0.005, 0.0, 1.0, 0.001));
		spinnerLR.setBounds(389, 50, 85, 20);
		getContentPane().add(spinnerLR);

		txtDefaultLearningRate = new JTextField();
		txtDefaultLearningRate.setBorder(null);
		txtDefaultLearningRate.setHorizontalAlignment(SwingConstants.TRAILING);
		txtDefaultLearningRate.setEditable(false);
		txtDefaultLearningRate.setText("Default learning rate");
		txtDefaultLearningRate.setBounds(249, 50, 130, 20);
		getContentPane().add(txtDefaultLearningRate);
		txtDefaultLearningRate.setColumns(10);

		JSpinner spinnerEpochSize = new JSpinner();
		spinnerEpochSize.setModel(new SpinnerNumberModel(new Integer(100), new Integer(1), null, new Integer(1)));
		spinnerEpochSize.setBounds(417, 76, 57, 20);
		getContentPane().add(spinnerEpochSize);

		txtEpochSize = new JTextField();
		txtEpochSize.setBorder(null);
		txtEpochSize.setHorizontalAlignment(SwingConstants.TRAILING);
		txtEpochSize.setEditable(false);
		txtEpochSize.setText("Epoch size");
		txtEpochSize.setBounds(277, 76, 130, 20);
		getContentPane().add(txtEpochSize);
		txtEpochSize.setColumns(10);

		JSpinner spinnerTarget = new JSpinner();
		spinnerTarget.setModel(new SpinnerNumberModel(new Double(20), new Double(0), null, new Double(2)));
		spinnerTarget.setBounds(417, 22, 57, 20);
		getContentPane().add(spinnerTarget);

		txtTargetErrorPer = new JTextField();
		txtTargetErrorPer.setBorder(null);
		txtTargetErrorPer.setOpaque(false);
		txtTargetErrorPer.setFocusable(false);
		txtTargetErrorPer.setHorizontalAlignment(SwingConstants.TRAILING);
		txtTargetErrorPer.setEditable(false);
		txtTargetErrorPer.setText("Target error per epoch");
		txtTargetErrorPer.setBounds(277, 22, 130, 20);
		getContentPane().add(txtTargetErrorPer);
		txtTargetErrorPer.setColumns(10);

		txtIncreaseAndDecrease = new JTextField();
		txtIncreaseAndDecrease.setBorder(null);
		txtIncreaseAndDecrease.setHorizontalAlignment(SwingConstants.TRAILING);
		txtIncreaseAndDecrease.setEditable(false);
		txtIncreaseAndDecrease.setText("Decrease and increase LR factors");
		txtIncreaseAndDecrease.setBounds(188, 102, 170, 20);
		getContentPane().add(txtIncreaseAndDecrease);
		txtIncreaseAndDecrease.setColumns(10);
		

		txtMomentumFactor = new JTextField();
		txtMomentumFactor.setBorder(null);
		txtMomentumFactor.setHorizontalAlignment(SwingConstants.TRAILING);
		txtMomentumFactor.setEditable(false);
		txtMomentumFactor.setText("Momentum factor");
		txtMomentumFactor.setBounds(277, 128, 130, 20);
		getContentPane().add(txtMomentumFactor);
		txtMomentumFactor.setColumns(10);
		

		JButton btnLaunch = new JButton("Launch Learning Algorithm");
		btnLaunch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				btnLaunch.setEnabled(false);
				if (rdbtnMomentum.isSelected()) {
					learningAlg((simpleBox.getSelectedIndex() == 1), rdbtnAdaptableLearningRate.isSelected(),
							rdbtnPreprocessing.isSelected(), !rdbtnIncrementEveryTime.isSelected(),
							(double) spinnerLR.getValue(), (double) spinnerDecreaseLR.getValue(),
							(double) spinnerIncreaseLR.getValue(), (double) spinnerMomentumFact.getValue(),
							(int) spinnerEpochSize.getValue(), (double) spinnerTarget.getValue());
				} else {
					learningAlg((simpleBox.getSelectedIndex() == 1), rdbtnAdaptableLearningRate.isSelected(),
							rdbtnPreprocessing.isSelected(), !rdbtnIncrementEveryTime.isSelected(),
							(double) spinnerLR.getValue(), (double) spinnerDecreaseLR.getValue(),
							(double) spinnerIncreaseLR.getValue(), 0., (int) spinnerEpochSize.getValue(),
							(double) spinnerTarget.getValue());
				}

			}
		});
		btnLaunch.setBounds(10, 159, 464, 23);
		getRootPane().setDefaultButton(btnLaunch);
		getContentPane().add(btnLaunch);
		// contentPane.setBackground(Color.ORANGE);
		// setContentPane(contentPane);
		setVisible(true);

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
