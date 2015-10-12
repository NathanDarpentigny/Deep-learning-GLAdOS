package gladosCommonConception;

import java.util.ArrayList;
import java.util.List;

/**
 * The most versatile <code>AbstractNeuron</code>, it is made to be part of the
 * hidden layers and thus have both an <code>inputNeuron</code> layer and an
 * <code>outputNeuron</code> layer
 * 
 * @author Laty
 *
 */
public class IntermediateNeuron extends AbstractNeuron {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7121607590851459193L;
	private double[] weights;
	private double[] weightDiffs;
	private double neuronDiff;
	private List<AbstractNeuron> inputNeurons = new ArrayList<AbstractNeuron>();
	private List<AbstractNeuron> outputNeurons;
	private double output;
	private double defaultLR;
	private double[] varLR;
	private boolean[] gradientChangedSign;

	public IntermediateNeuron(int size, double defaultLR) {
		weights = new double[size + 1];
		this.defaultLR = defaultLR;
		weightDiffs = new double[size + 1];
		gradientChangedSign = new boolean[size + 1];
		varLR = new double[size + 1];
		for (int c = 0; c < size + 1; c++) {
			weights[c] = (Math.random() * 2 * WEIGHT_RANGE - (WEIGHT_RANGE / 2)) / size;
			weightDiffs[c] = 0.;
			varLR[c] = defaultLR;

		}
	}

	public void fire() {
		double temp = 0;
		for (int c = 0; c < inputNeurons.size(); c++) {
			temp += weights[c] * inputNeurons.get(c).getOutput();
		}
		temp += weights[weights.length - 1];
		output = activationFun(temp);
	} 

	public double getOutput() {
		return output;
	}

	public void setInputNeurons(List<AbstractNeuron> inputs) {
		this.inputNeurons = inputs;
	}

	/**
	 * Checks if the number of weights is one more than the number of inputs.
	 * 
	 * @return
	 */
	public boolean checkValidity() {
		return weights.length == inputNeurons.size() + 1;
	}

	/**
	 * Sets the output neurons of this neuron
	 * 
	 * @param outputs
	 */
	public void setOutputNeurons(List<AbstractNeuron> outputs) {
		this.outputNeurons = outputs;

	}

	public double getNeuronDiff() {
		return neuronDiff;
	}

	public void calculateNeuronDiff(double expectedOutput) {
		double temp = 0;
		for (AbstractNeuron n : outputNeurons) {
			temp += n.getNeuronDiff() * n.getWeight(this);
		}
		neuronDiff = output * (1 - output) * temp;

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
		System.out.println("No such linked neuron in an intermediate layer");
		return null;
	}

	public void resetWeightDiffsMomentum(double alphaRate) {
		for (int c = 0; c < weightDiffs.length; c++) {
			weightDiffs[c] = weightDiffs[c] * alphaRate;
		}
	}

	public void varyLR(double decreaseLR, double increaseLR) {
		for (int c = 0; c < weights.length; c++) {
			if (gradientChangedSign[c]) {
				varLR[c] = decreaseLR * varLR[c];
			} else {
				varLR[c] = increaseLR * varLR[c];
			}
		}
	}

	public void resetLR() {
		for (int c = 0; c < weights.length; c++) {
			varLR[c] = defaultLR;
		}

	}

}
