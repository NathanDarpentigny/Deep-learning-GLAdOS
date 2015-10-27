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
public class IntermediateNeuron extends ActiveNeuron {
	/**
	 * 
	 */
	private static final long serialVersionUID = 931358472690883801L;

	private List<Synapse> outputSynapses;

	public IntermediateNeuron(int finalInputSize) {
		super(finalInputSize);
		outputSynapses = new ArrayList<Synapse>();

	}

	public List<Synapse> getOutputSynapses() {
		return outputSynapses;
	}

	public void addOutputSynapse(Synapse syn) {
		outputSynapses.add(syn);
	}
	
	public void outLinkToLayer(List<AbstractNeuron> layer, int inSize,double defLR){
		for(AbstractNeuron n : layer){
			new Synapse(this, n, (Math.random() * 2 * WEIGHT_RANGE - (WEIGHT_RANGE / 2))/inSize,defLR);
		}
	}

	public void resetWeightDiffs() {
		// TODO Auto-generated method stub

	}

	public void resetWeightDiffsMomentum(double alphaRate) {
		// TODO Auto-generated method stub

	}

}
