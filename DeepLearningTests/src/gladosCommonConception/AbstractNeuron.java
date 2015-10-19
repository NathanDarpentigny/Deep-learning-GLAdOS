package gladosCommonConception;

import java.io.Serializable;

/**
 * A class that represents a neuron, the fact that it is abstract allows for
 * more flexibility in any NeuralNetwork.
 * 
 * @author Laty
 *
 */

public abstract class AbstractNeuron implements Serializable {

	private double output = 0.;
	private double neuronDiff =0.;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8406554630872605961L;
	/**
	 * A manually adjusted value that allows weights to be initialized randomly
	 * between <code>WEIGHT_RANGE</code> and <code>-WEIGHT_RANGE</code>
	 */
	public static final double WEIGHT_RANGE = 2.4;

	/**
	 * This function should return the current stored output for this neuron.
	 * 
	 * @return
	 */

	/**
	 * Calculates and sets the output for this neuron, considering current
	 * inputs.
	 */
	abstract public void fire();

	public void setOutput(double x) {
		output = x;
	}

	public double getOutput() {
		return output;
	}

	/**
	 * This method should return the current stored value of the neuron's
	 * gradient.
	 * 
	 * @return
	 */
	public double getNeuronDiff() {
		return neuronDiff;
	}

	/**
	 * Calculates the Neuron gradient and refreshes attribute.
	 */
	public void setNeuronDiff(double x) {
		neuronDiff = x;
	}

}
