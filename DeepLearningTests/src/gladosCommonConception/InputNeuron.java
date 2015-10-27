package gladosCommonConception;

import java.util.ArrayList;
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
	private double input = 0.;
	private List<Synapse> outputSynapses;

	public InputNeuron() {
		outputSynapses = new ArrayList<Synapse>();
	}

	public void addOutputSynapse(Synapse syn) {
		outputSynapses.add(syn);
	}

	public List<Synapse> getOutputSynapses() {
		return outputSynapses;
	}

	public void setInput(double x) {
		input = x;
	}

	public void outLinkToLayer(List<AbstractNeuron> layer, int inSize, double defLR){
		for(AbstractNeuron n : layer){
			new Synapse(this, n, (Math.random() * 2 * WEIGHT_RANGE - (WEIGHT_RANGE / 2))/inSize, defLR);
		}
	}
	
	public void fire() {
		setOutput(input);

	}

}
