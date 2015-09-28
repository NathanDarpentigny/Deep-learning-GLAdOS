package newTest;

import java.util.ArrayList;
import java.util.List;

public class IntermediateNeuron extends AbstractNeuron {
	private double[] weights;
	private double[] weightDiffs;
	private double neuronDiff;
	private List<AbstractNeuron> inputNeurons = new ArrayList<AbstractNeuron>();
	private List<AbstractNeuron> outputNeurons;
	private double output;

	public IntermediateNeuron(int size) {
		weights = new double[size + 1];
		weightDiffs = new double[size + 1];
		for (int i = 0; i <= size; i++) {
			weights[i] = ((Math.random() * 4.8) - 2.4) / size;
			weightDiffs[i] = 0.;
		}

	}

	public void fire() {
		double res = 0.0;

		int c = 0;
		for (AbstractNeuron n : inputNeurons) {
			res += weights[c] * n.getOutput();

			c++;
		}
		res -= weights[weights.length - 1];
		output = activationFun(res);

	}

	public double getOutput() {
		return output;
	}

	public List<AbstractNeuron> getInputNeurons() {
		return inputNeurons;
	}

	public void setInputNeurons(List<AbstractNeuron> inputs) {
		this.inputNeurons = inputs;
	}

	public List<AbstractNeuron> getOutputNeurons() {
		return outputNeurons;
	}

	public double getNeuronDiff() {

		return neuronDiff;
	}

	public void calculateNeuronDiff(double expectedOutput) {
		double temp = 0.;
		for (AbstractNeuron n : outputNeurons) {
			temp += n.getNeuronDiff() * n.getWeight(this);
		}
		neuronDiff = output * (1. - output) * temp;
	}

	public void setWeight(int i, double value) {
		weights[i] = value;
	}

	public void setWeightDiff(int i, double value) {
		weightDiffs[i] = value;
	}

	public void setNeuronDiff(double value) {
		neuronDiff = value;
	}

	public Double getWeight(AbstractNeuron n) {

		if (inputNeurons.contains(n)) {
			return weights[inputNeurons.indexOf(n)];
		}
		System.out.println("no such neuron");
		return null;
	}

	public double getWeight(int i) {
		return weights[i];
	}

	/**
	 * Checks if the number of weights is one more than the number of inputs.
	 * 
	 * @return
	 */
	public boolean checkValidity() {
		return weights.length == inputNeurons.size() + 1;
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

	/**
	 * Sets the output neurons of this neuron
	 * 
	 * @param outputNeurons
	 */
	public void setOutputNeurons(List<AbstractNeuron> outputNeurons) {
		this.outputNeurons = outputNeurons;

	}

}
