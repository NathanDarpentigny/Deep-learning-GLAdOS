package gladosPackage;

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
		for (int c = 0; c < size + 1; c++) {
			weights[c] = (Math.random() * 4.8 - 2.4) / size;
			weightDiffs[c] = 0.;
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
		for(int c = 0 ; c<inputNeurons.size() ; c++){
			temp += weights[c]*inputNeurons.get(c).getOutput();
		}
		temp += weights[weights.length-1];
		output = activationFun(temp);
	}


	public double getNeuronDiff() {
		return neuronDiff;
	}

	
	public void calculateNeuronDiff(double expectedOutput) {
		neuronDiff = output*(1-output)*(expectedOutput -output);
	}


	public void resetWeightDiffs() {
		for(int c = 0 ;  c< weightDiffs.length  ; c++){
			weightDiffs[c] = 0;
		}
	}


	public void incrementWeightDiffs() {
		for(int c = 0 ;  c< weightDiffs.length-1 ; c++){
			weightDiffs[c]+=inputNeurons.get(c).getOutput()*neuronDiff;
		}
		weightDiffs[weightDiffs.length-1]+=neuronDiff;
	}

	
	public void incrementWeights(double learningRate) {
		for(int c = 0 ;  c<weights.length ; c++){
			weights[c] += learningRate*weightDiffs[c];
		}
	}


	public Double getWeight(AbstractNeuron n) {
		if(inputNeurons.contains(n)){
			return weights[inputNeurons.indexOf(n)];
		}
		System.out.println("No such linked neuron in an output layer");
		return null;
	}

}
