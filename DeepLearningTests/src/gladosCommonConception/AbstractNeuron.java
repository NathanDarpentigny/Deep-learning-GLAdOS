package gladosCommonConception;

import java.io.Serializable;




/**
 * A class that represents a neuron, the fact that it is abstract allows for
 * more flexibility in any NeuralNetwork.
 * @author Laty
 *
 */
@SuppressWarnings("unused")
public abstract class AbstractNeuron implements Serializable {

	private double output;
	private double neuronDiff;
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
	abstract public double getOutput();

	/**
	 * Calculates and sets the output for this neuron, considering current
	 * inputs.
	 */
	abstract public void fire();

	/**
	 * This method should return the current stored value of the neuron's
	 * gradient.
	 * 
	 * @return
	 */
	abstract public double getNeuronDiff();

	/**
	 * Calculates the Neuron gradient and refreshes attribute.
	 */
	abstract public void calculateNeuronDiff(double expectedOutput);

	/**
	 * Resets the weight gradient for all weights in the neuron
	 */
	abstract public void resetWeightDiffs();

	/**
	 * "Resets" the weight gradient for all weights in the neuron according to
	 * the momentum method.
	 */
	abstract public void resetWeightDiffsMomentum(double alphaRate);

	/**
	 * Increments the weight gradient for all the weights in the neuron
	 */
	abstract public void incrementWeightDiffs();

	/**
	 * Increments all the neuron's weight according to its current weight
	 * gradients and the given <code>learningRate</code>
	 * 
	 * @param learningRate
	 */
	abstract public void incrementWeights(double learningRate);

	/**
	 * Returns the weight associated with the neuron <code> n</code>. If it
	 * doesn't or if <code>this</code> is an InputNeuron, null is returned.
	 * 
	 * @param n
	 * @return
	 */
	abstract public Double getWeight(AbstractNeuron n);

	/**
	 * The currently used activation function.
	 * 
	 * @param x
	 * @return
	 */
	public static double activationFun(double x) {
		if (x < -45.) {
			return 0;
		} else if (x > 45.) {
			return 1;
		} else {
			return 1. / (1 + Math.exp(-x));
		}
	}

	/**
	 * Applies the variation of learning rate to every weight of this neuron
	 * following a specific law.
	 */
	abstract public void varyLR(double decreaseLR, double increaseLR);

	/**
	 * Resets the learning rate of every weight to its default value.
	 */
	abstract public void resetLR();

}
