package newTest;

import java.util.List;

public class InputNeuron extends AbstractNeuron {

	private double input = 0;
	private double neuronDiff = 0;
	private List<AbstractNeuron> outputNeurons;
	private double output;

	public InputNeuron() {
	}

	public void setOutputNeurons(List<AbstractNeuron> outNeurons) {
		outputNeurons = outNeurons;
	}

	public double getOutput() {
		return output;
	}

	public void fire() {
		output = input;

	}

	public double getNeuronDiff() {
		return neuronDiff;
	}

	public void calculateNeuronDiff(double expectedOutput) {
	}

	public void resetWeightDiffs() {
	}

	public void incrementWeightDiffs() {
	}

	public void incrementWeights(double learningRate) {
	}

	public Double getWeight(AbstractNeuron n) {
		System.out.println("Trying to get input weights from an input neuron");
		return null;
	}

	public void setInput(double d) {
		input = d;
	}
}
