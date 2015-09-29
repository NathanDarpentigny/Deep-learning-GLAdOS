package gladosPackage;

import java.util.List;

/**
 * An output neuron for a neural network. Uses an inputLayer but no outputLayer,
 * otherwise works exactly like an <code>IntermediateNeuron</code>.
 * 
 * @author Laty
 *
 */
public class OutputNeuron extends AbstractNeuron {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1783963236181264684L;
	private double[] weights;
	private double[] weightDiffs;
	private double neuronDiff = 0;
	private List<AbstractNeuron> inputNeurons;
	private double output;
	private double[] varLR;
	private boolean[] gradientChangedSign;

	public OutputNeuron(int size) {
		weights = new double[size + 1];
		weightDiffs = new double[size + 1];
		gradientChangedSign = new boolean[size + 1];
		varLR = new double[size + 1];
		for (int c = 0; c < size + 1; c++) {
			weights[c] = (Math.random() * 2 * WEIGHT_RANGE - (WEIGHT_RANGE / 2)) / size;
			weightDiffs[c] = 0.;
			varLR[c] = LearningWindow.LEARNING_RATE;

		}
	}

	public void setInputNeurons(List<AbstractNeuron> inputN) {
		inputNeurons = inputN;
	}

	public double getOutput() {
		return output;
	}

	public void fire() {
		double temp = 0;
		for (int c = 0; c < inputNeurons.size(); c++) {
			temp += weights[c] * inputNeurons.get(c).getOutput();
		}
		temp += weights[weights.length - 1];
		output = activationFun(temp);
	}

	public double getNeuronDiff() {
		return neuronDiff;
	}

	public void calculateNeuronDiff(double expectedOutput) {
		neuronDiff = output * (1 - output) * (expectedOutput - output);
	}

	public void resetWeightDiffs() {
		for (int c = 0; c < weightDiffs.length; c++) {
			weightDiffs[c] = 0;
		}
	}

	public void incrementWeightDiffs() {
		for (int c = 0; c < weightDiffs.length - 1; c++) {
			double temp = weightDiffs[c];
			weightDiffs[c] += inputNeurons.get(c).getOutput() * neuronDiff;
			if (temp * weightDiffs[c] >= 0) {
				gradientChangedSign[c] = false;
			} else {
				gradientChangedSign[c] = true;
			}
		}
		weightDiffs[weightDiffs.length - 1] += neuronDiff;
	}

	public void incrementWeights(double learningRate) {
		for (int c = 0; c < weights.length; c++) {
			weights[c] += learningRate * weightDiffs[c];
		}
	}

	public Double getWeight(AbstractNeuron n) {
		if (inputNeurons.contains(n)) {
			return weights[inputNeurons.indexOf(n)];
		}
		System.out.println("No such linked neuron in an output layer");
		return null;
	}

	public void resetWeightDiffsMomentum(double alphaRate) {
		for (int c = 0; c < weightDiffs.length; c++) {
			weightDiffs[c] = weightDiffs[c] * alphaRate;
		}
	}

	public void varyLR() {
		for (int c = 0; c < weights.length; c++) {
			if (gradientChangedSign[c]) {
				varLR[c] = LearningWindow.INCREASE_LR * varLR[c];
			} else {
				varLR[c] = LearningWindow.DECREASE_LR * varLR[c];
			}
		}
	}

	public void resetLR() {
		for (int c = 0; c < weights.length; c++) {
			varLR[c] = LearningWindow.LEARNING_RATE;
		}

	}

}
