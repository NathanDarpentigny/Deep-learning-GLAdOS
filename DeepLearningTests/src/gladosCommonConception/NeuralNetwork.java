package gladosCommonConception;

import java.io.Serializable;
import java.util.List;

/**
 * A feed forward neural network which consists of an input layer, an output
 * layer and at least one hidden layer.
 * 
 * @author Laty
 *
 */
public abstract class NeuralNetwork implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2098285940401171678L;
	
	public abstract void fire();
	
	public abstract List<Double> getOutputs();
	
	public abstract void setInputs(double[] inputs);

	public abstract void linkNetwork();
}
