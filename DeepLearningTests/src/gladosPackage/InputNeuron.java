package gladosPackage;

import java.util.List;

/**
 * The simples of all <code>AbstractNeuron</code>s it simply transmits the input
 * to the output. Most methods are dummies.
 * 
 * @author Laty
 *
 */
public class InputNeuron extends AbstractNeuron {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5887349009775758412L;
	private double input = 0;
	private double neuronDiff = 0;
	private double output;

	public InputNeuron() {
	}

	public void setOutputNeurons(List<AbstractNeuron> outNeurons) {
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

	public void resetWeightDiffsMomentum(double alphaRate) {
	}

	public void varyLR() {

	}

	public void resetLR() {

	}
}
