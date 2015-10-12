package gladosCommonConception;

import java.util.ArrayList;
import java.util.List;

/**
 * The most versatile <code>AbstractNeuron</code>, it is made to be part of the
 * hidden layers and thus have both an <code>inputNeuron</code> layer and an
 * <code>outputNeuron</code> layer
 * 
 * @author Laty
 *
 */
public class IntermediateNeuron extends AbstractNeuron {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7121607590851459193L;
	private double[] weights; // la ieme case designe le poids du synapse reliant le neurone a son ieme input
	private double[] weightDiffs; // DE/Dwij = la ieme case designe la derivee de l'erreur par rapport a wij 
	private double neuronDiff; // DE/DZj= derivee de l erreur par rapport a l'output (sans fonction d'activation) du neurone
	private List<AbstractNeuron> inputNeurons = new ArrayList<AbstractNeuron>();  //la liste est construite de telle sorte que le dernier element soit le biais
	private List<AbstractNeuron> outputNeurons; 
	private double output;
	private double defaultLR;
	private double[] varLR; //Contient les taux d'apprentissage des neurones d'inputs pour accelerer la convergence en modifiant les taux d'apprentissage de chaque neurone
	private boolean[] gradientChangedSign; // Utile pour savoir comment changer les taux d'apprentissage et donc changer varLR

	public IntermediateNeuron(int size, double defaultLR) {
		weights = new double[size + 1]; // le +1 est ajoute pour prendre en compte le biais
		this.defaultLR = defaultLR;
		weightDiffs = new double[size + 1];
		gradientChangedSign = new boolean[size + 1];
		varLR = new double[size + 1];
		for (int c = 0; c < size + 1; c++) {
			weights[c] = (Math.random() * 2 * WEIGHT_RANGE - (WEIGHT_RANGE / 2)) / size; // Loi de proba uniforme sur [-2/size,2/size]
			weightDiffs[c] = 0.;
			varLR[c] = defaultLR;

		}
	}
//Je suggere que fire soit renomer updateoutputs qui est plus explicite 
	public void fire() {
		double temp = 0;
		for (int c = 0; c < inputNeurons.size(); c++) {
			temp += weights[c] * inputNeurons.get(c).getOutput();
		}
		temp += weights[weights.length - 1]; //biais
		output = activationFun(temp);
	} 

	public double getOutput() {
		return output;
	}

	public void setInputNeurons(List<AbstractNeuron> inputs) {
		this.inputNeurons = inputs;
	}

	/**
	 * Checks if the number of weights is one more than the number of inputs.
	 * 
	 * @return
	 */
	public boolean checkValidity() {
		return weights.length == inputNeurons.size() + 1;
	}

	/**
	 * Sets the output neurons of this neuron
	 * 
	 * @param outputs
	 */
	public void setOutputNeurons(List<AbstractNeuron> outputs) {
		this.outputNeurons = outputs;

	}

	public double getNeuronDiff() {
		return neuronDiff;
	}

	public void calculateNeuronDiff(double expectedOutput) {
		double temp = 0;
		for (AbstractNeuron n : outputNeurons) {
			temp += n.getNeuronDiff() * n.getWeight(this);
		}
		neuronDiff = output * (1 - output) * temp; //Attention ne marche que pour la sigmoide

	}

	public void resetWeightDiffs() {
		for (int c = 0; c < weightDiffs.length; c++) {
			weightDiffs[c] = 0;
		}
	}
//Nous choisissons de modifier les poids en Full Batch au lieu de le faire Online => Sommation des gradients
	public void incrementWeightDiffs() {
		for (int c = 0; c < weightDiffs.length - 1; c++) {
			double temp = weightDiffs[c];
			weightDiffs[c] += inputNeurons.get(c).getOutput() * neuronDiff;
			//comparaison de signe entre l'ancien et le nouveau grad pour changer eventuellement les taux d'apprentissage
			if (temp * weightDiffs[c] >= 0) {
				gradientChangedSign[c] = false;
			} else {
				gradientChangedSign[c] = true;
			}
		}
		weightDiffs[weightDiffs.length - 1] += neuronDiff; //poids du biais
	}

	public void incrementWeights(double learningRate) {
		for (int c = 0; c < weights.length; c++) {
			weights[c] += learningRate * weightDiffs[c];
		}

	}

	public Double getWeight(AbstractNeuron n) {
		if (inputNeurons.contains(n)) {
			return weights[inputNeurons.indexOf(n)];
		}
		System.out.println("No such linked neuron in an intermediate layer");
		return null;
	}

	public void resetWeightDiffsMomentum(double alphaRate) {
		for (int c = 0; c < weightDiffs.length; c++) {
			weightDiffs[c] = weightDiffs[c] * alphaRate;
		}
	}

	public void varyLR(double decreaseLR, double increaseLR) {
		for (int c = 0; c < weights.length; c++) {
			if (gradientChangedSign[c]) {
				varLR[c] = decreaseLR * varLR[c];
			} else {
				varLR[c] = increaseLR * varLR[c];
			}
		}
	}

	public void resetLR() {
		for (int c = 0; c < weights.length; c++) {
			varLR[c] = defaultLR;
		}

	}

}
