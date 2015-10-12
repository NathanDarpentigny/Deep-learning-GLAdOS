package gladosCommonConception;

public class Synapse {
	private AbstractNeuron inputNeuron;
	private double weight;
	private double weightDiff;
	private AbstractNeuron outputNeuron;

	public Synapse(AbstractNeuron in, AbstractNeuron out, double w){
		weight = w;
		weightDiff = 0;
		inputNeuron = in;
		outputNeuron = out;
	}

	/**
	 * @return the inputNeuron
	 */
	public AbstractNeuron getInputNeuron() {
		return inputNeuron;
	}

	/**
	 * @param inputNeuron the inputNeuron to set
	 */
	public void setInputNeuron(AbstractNeuron inputNeuron) {
		this.inputNeuron = inputNeuron;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return the weightDiff
	 */
	public double getWeightDiff() {
		return weightDiff;
	}

	/**
	 * @param weightDiff the weightDiff to set
	 */
	public void setWeightDiff(double weightDiff) {
		this.weightDiff = weightDiff;
	}

	/**
	 * @return the outputNeuron
	 */
	public AbstractNeuron getOutputNeuron() {
		return outputNeuron;
	}

	/**
	 * @param outputNeuron the outputNeuron to set
	 */
	public void setOutputNeuron(AbstractNeuron outputNeuron) {
		this.outputNeuron = outputNeuron;
	}
	
	
}
