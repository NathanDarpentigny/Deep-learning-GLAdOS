package gladosPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A feed forward neural network which consists of an input layer, an output
 * layer and at least one hidden layer.
 * 
 * @author Laty
 *
 */
public class NeuralNetwork implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<AbstractNeuron> inputLayer;
	private List<List<AbstractNeuron>> intermediateLayers;
	private List<AbstractNeuron> outputLayer;
	private double learningRate;
	// private int numberOfLayers;

	/**
	 * Creates a feed forward neural network following the structure of
	 * <code>structure</code> (for example {1,3,2} would give a network with 1
	 * neuron in the inputLayer, 3 in one intermediateLayer and 2 in the
	 * outputLayer) Also takes the learning rate of the structure.
	 * 
	 * @param structure
	 * @param learningRate
	 */
	public NeuralNetwork(int[] structure, double learningRate) {
		this.learningRate = learningRate;
		if (structure.length < 2) {
			System.out.println("Please enter a structure with more than 2 layers");
			return;
		}
		inputLayer = new ArrayList<AbstractNeuron>();
		for (int j = 0; j < structure[0]; j++) {
			inputLayer.add(new InputNeuron());
		}
		intermediateLayers = new ArrayList<List<AbstractNeuron>>();
		for (int i = 1; i < structure.length - 1; i++) {
			intermediateLayers.add(new ArrayList<AbstractNeuron>());
			for (int j = 0; j < structure[i]; j++) {
				intermediateLayers.get(i - 1).add(new HiddenNeuron(structure[i - 1],learningRate));
			}
		}

		outputLayer = new ArrayList<AbstractNeuron>();
		for (int j = 0; j < structure[structure.length - 1]; j++) {
			outputLayer.add(new OutputNeuron(structure[structure.length - 2],learningRate));
		}
		linkNetwork();
	}

	public List<AbstractNeuron> getIntermediateLayer(int i) {
		return intermediateLayers.get(i);
	}

	public List<AbstractNeuron> getInputLayer() {
		return inputLayer;
	}

	public List<AbstractNeuron> getOutputLayer() {
		return outputLayer;
	}

	/**
	 * Sets the raw inputs to the neural network
	 * 
	 * @param inputs
	 */
	public void setInputs(double[] inputs) {
		if (inputs.length != inputLayer.size()) {
			System.out.println(
					"Mismatched input size : " + inputs.length + " inputs and " + inputLayer.size() + "input Neurons");
			return;
		}
		for (int i = 0; i < inputs.length; i++) {
			((InputNeuron) inputLayer.get(i)).setInput(inputs[i]);
		}
	}

	/**
	 * Modifies one of the inputs, at the <code>i</code> index.
	 * 
	 * @param input
	 * @param i
	 */
	public void setOneInput(double input, int i) {
		((InputNeuron) inputLayer.get(i)).setInput(input);
	}

	/**
	 * Applies the propagation algorithm (since this is a feed forward neural
	 * network, this simply calls the fire method of every Neuron layer by
	 * layer, starting from the <code>inputLayer</code> and ending with the
	 * <code>outputLayer</code>.
	 */
	public void fire() {
		for (AbstractNeuron n : inputLayer) {
			n.fire();
		}
		for (List<AbstractNeuron> layer : intermediateLayers) {
			for (AbstractNeuron n : layer) {
				n.fire();
			}
		}
		for (AbstractNeuron n : outputLayer) {
			n.fire();
		}
	}

	/**
	 * Returns the current outputs of the <code>outputLayer</code>, warning this
	 * does not apply a propagation algorithm, as such it does NOT modifiy the
	 * neural net in any way.
	 * 
	 * @return
	 */
	public List<Double> getOutputs() {
		List<Double> res = new ArrayList<Double>();
		for (AbstractNeuron n : outputLayer) {
			res.add(n.getOutput());
		}
		return res;
	}

	/**
	 * This private method is called in the net's constructor to correctly link
	 * all the neurons in the network
	 */
	private void linkNetwork() {
		for (AbstractNeuron n : inputLayer) {
			((InputNeuron) n).setOutputNeurons(intermediateLayers.get(0));
		}

		for (AbstractNeuron n : outputLayer) {
			((OutputNeuron) n).setInputNeurons(intermediateLayers.get(intermediateLayers.size() - 1));
		}

		for (AbstractNeuron n : intermediateLayers.get(0)) {
			((HiddenNeuron) n).setInputNeurons(inputLayer);
		}
		for (AbstractNeuron n : intermediateLayers.get(intermediateLayers.size() - 1)) {
			((HiddenNeuron) n).setOutputNeurons(outputLayer);
		}

		for (int i = 0; i < intermediateLayers.size() - 1; i++) {
			for (AbstractNeuron n : intermediateLayers.get(i)) {
				((HiddenNeuron) n).setOutputNeurons(intermediateLayers.get(i + 1));
			}
		}

		for (int i = 1; i < intermediateLayers.size(); i++) {
			for (AbstractNeuron n : intermediateLayers.get(i)) {
				((HiddenNeuron) n).setInputNeurons(intermediateLayers.get(i - 1));
			}
		}
	}

	/**
	 * Recalculates all the neuron gradients/errors following to the
	 * backpropagation algorithm.
	 * 
	 * @param expectedOutputs
	 */
	public void calculateNeuronDiffs(double[] expectedOutputs) {
		int c = 0;
		for (AbstractNeuron n : outputLayer) {
			n.calculateNeuronDiff(expectedOutputs[c]);
			c++;
		}

		for (int i = intermediateLayers.size() - 1; i >= 0; i--) {
			for (AbstractNeuron n : intermediateLayers.get(i)) {
				n.calculateNeuronDiff(0);
			}
		}

		for (AbstractNeuron n : inputLayer) {
			n.calculateNeuronDiff(0);
		}
	}

	/**
	 * Increments all the weight gradients for all the neurons in the net
	 */
	public void incrementWeightDiffs() {
		for (AbstractNeuron n : outputLayer) {
			n.incrementWeightDiffs();
		}

		for (int i = intermediateLayers.size() - 1; i >= 0; i--) {
			for (AbstractNeuron n : intermediateLayers.get(i)) {
				n.incrementWeightDiffs();
			}
		}

		for (AbstractNeuron n : inputLayer) {
			n.incrementWeightDiffs();
		}
	}

	/**
	 * Increments all the weights for all the neurons in the net.
	 */
	public void incrementWeights() {
		for (AbstractNeuron n : outputLayer) {
			n.incrementWeights(learningRate);
		}

		for (int i = intermediateLayers.size() - 1; i >= 0; i--) {
			for (AbstractNeuron n : intermediateLayers.get(i)) {
				n.incrementWeights(learningRate);
			}
		}

		for (AbstractNeuron n : inputLayer) {
			n.incrementWeights(learningRate);
		}
	}

	/**
	 * Resets all the weight gradients for all the neurons in the net.
	 */
	public void resetWeightDiffs() {
		for (AbstractNeuron n : outputLayer) {
			n.resetWeightDiffs();
		}

		for (List<AbstractNeuron> layer : intermediateLayers) {
			for (AbstractNeuron n : layer) {
				n.resetWeightDiffs();
			}
		}

		for (AbstractNeuron n : inputLayer) {
			n.resetWeightDiffs();
		}
	}

	/**
	 * Sets a learning rate for the backpropagation algorithm.
	 * 
	 * @param newLearningRate
	 */
	public void setLearningRate(double newLearningRate) {
		learningRate = newLearningRate;
	}

	/**
	 * Resets all the weight gradients for all the neurons in the net, according
	 * to the momentum method and with the alphaRate as parameter.
	 * 
	 * @param alphaRate
	 */
	public void resetWeightDiffsMomentum(double alphaRate) {
		for (AbstractNeuron n : outputLayer) {
			n.resetWeightDiffsMomentum(alphaRate);
		}

		for (List<AbstractNeuron> layer : intermediateLayers) {
			for (AbstractNeuron n : layer) {
				n.resetWeightDiffsMomentum(alphaRate);
			}
		}

		for (AbstractNeuron n : inputLayer) {
			n.resetWeightDiffsMomentum(alphaRate);
		}
	}

	/**
	 * Changes the learning rate for every neuron in the network when using
	 * adaptable Learning Rates.
	 */
	public void varyLR(double decreaseLR, double increaseLR) {
		for (AbstractNeuron n : outputLayer) {
			n.varyLR( decreaseLR,  increaseLR);
		}

		for (List<AbstractNeuron> layer : intermediateLayers) {
			for (AbstractNeuron n : layer) {
				n.varyLR(decreaseLR,  increaseLR);
			}
		}

		for (AbstractNeuron n : inputLayer) {
			n.varyLR(decreaseLR,  increaseLR);
		}
	}

	/**
	 * Resets the learning rate to default for all the neurons in the network
	 * when using adaptable Learning Rate.
	 */
	public void resetLR() {
		for (AbstractNeuron n : outputLayer) {
			n.resetLR();
		}

		for (List<AbstractNeuron> layer : intermediateLayers) {
			for (AbstractNeuron n : layer) {
				n.resetLR();
			}
		}

		for (AbstractNeuron n : inputLayer) {
			n.resetLR();
		}
	}
}
