package gladosPackage;

import java.util.List;

/**
 * The simples of all <code>AbstractNeuron</code>s it simply transmits the input
 * to the output. Most methods are dummies.
 * 
 * @author Laty
 *
 */
class InputNeuron extends AbstractNeuron {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5887349009775758412L;
	private double input = 0;

	public InputNeuron() {
	}

	public void setOutputNeurons(List<AbstractNeuron> outNeurons) {
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

	public void varyLR(double dummy, double dummy2) {

	}

	public void resetLR() {

	}

	@Override
	public double getOutput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getNeuronDiff() {
		// TODO Auto-generated method stub
		return 0;
	}
}
