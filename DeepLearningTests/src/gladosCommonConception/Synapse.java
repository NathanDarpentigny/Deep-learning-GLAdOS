package gladosCommonConception;

import java.io.Serializable;

public class Synapse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4883554879579494943L;
	private AbstractNeuron inputNeuron;
	private double weight;
	private double weightDiff;
	private AbstractNeuron outputNeuron;
	private double synLR;

	public Synapse(AbstractNeuron in, AbstractNeuron out, double w, double defLR) {
		weight = w;
		weightDiff = 0.;
		inputNeuron = in;
		setSynLR(defLR);
		if (in instanceof InputNeuron) {
			((InputNeuron) in).addOutputSynapse(this);
		} else if (in instanceof IntermediateNeuron) {
			((IntermediateNeuron) in).addOutputSynapse(this);
		}
		outputNeuron = out;
		if (out instanceof ActiveNeuron) {
			((ActiveNeuron) out).addInputSynapse(this);
		}

	}

	/**
	 * @return the inputNeuron
	 */
	public AbstractNeuron getInputNeuron() {
		return inputNeuron;
	}

	/**
	 * @param inputNeuron
	 *            the inputNeuron to set
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
	 * @param weight
	 *            the weight to set
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
	 * @param weightDiff
	 *            the weightDiff to set
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
	 * @param outputNeuron
	 *            the outputNeuron to set
	 */
	public void setOutputNeuron(AbstractNeuron outputNeuron) {
		this.outputNeuron = outputNeuron;
	}

	public double getSynLR() {
		return synLR;
	}

	public void setSynLR(double synLR) {
		this.synLR = synLR;
	}

}
