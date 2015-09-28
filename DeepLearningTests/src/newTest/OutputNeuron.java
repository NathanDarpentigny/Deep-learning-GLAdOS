package newTest;

import java.util.List;

public class OutputNeuron extends AbstractNeuron {
	private double[] weights;
	private double[] weightDiffs;
	private double neuronDiff = 0;
	private List<AbstractNeuron> inputNeurons;
	private double output;

	public OutputNeuron(int size) {
		weights = new double[size + 1];
		weightDiffs = new double[size + 1];
		for (int i = 0; i <= size; i++) {
			weights[i] = ((Math.random() * 4.8) - 2.4) / size;
			weightDiffs[i] = 0.;
		}
	}

	public double getOutput() {

		return output;
	}

	public void fire() {
		double res = 0.0;
		int c = 0;
		for (AbstractNeuron n : inputNeurons) {
			res += weights[c] * n.getOutput();
			c++;
		}
		res += weights[weights.length - 1];
		output = activationFun(res);

	}

	public double getNeuronDiff() {
		return neuronDiff;
	}

	public void calculateNeuronDiff(double expectedOutput) {

		neuronDiff = output * (1. - output) * (expectedOutput - output);

	}

	public Double getWeight(AbstractNeuron n) {
		if (inputNeurons.contains(n)) {
			return weights[inputNeurons.indexOf(n)];
		}
		return null;
	}

	public void resetWeightDiffs() {
		weightDiffs = new double[weights.length];
	}

	public void incrementWeightDiffs() {

		for (int i = 0; i < inputNeurons.size(); i++) {
			weightDiffs[i] += inputNeurons.get(i).getOutput() * neuronDiff;
		}
		weightDiffs[weightDiffs.length - 1] += neuronDiff;

	}

	public void incrementWeights(double learningRate) {
		for (int i = 0; i < weights.length; i++) {
			weights[i] += weightDiffs[i] * learningRate;
		}
	}

	public void setInputNeurons(List<AbstractNeuron> inputN) {
		inputNeurons = inputN;

	}

}
