package gladosCommonConception;

import java.util.ArrayList;
import java.util.List;

public abstract class ActiveNeuron extends AbstractNeuron {

	/**
	 * 
	 */
	private static final long serialVersionUID = -145627062784046745L;
	private double bias = 0.;
	private double biasDiff = 0.;
	private List<Synapse> inputSynapses;
	private double intermediateValue = 0;

	public ActiveNeuron(int finalInputSize){
		inputSynapses = new ArrayList<Synapse>();
		bias = (Math.random() * 2 * WEIGHT_RANGE - (WEIGHT_RANGE / 2))/finalInputSize;
	}
	
	public ActiveNeuron(boolean determistic){
		inputSynapses = new ArrayList<Synapse>();
		bias = 1.;
	}

	public void fire() {
		double res = bias;
		for(Synapse s : getInputSynapses()){
			res+=s.getWeight()*s.getInputNeuron().getOutput();
		}
		intermediateValue = res;
		setOutput(Sigmoid.getInstance().apply(res)); 
	}
	
	public void addInputSynapse(Synapse syn) {
		inputSynapses.add(syn);
	}

	public List<Synapse> getInputSynapses() {
		return inputSynapses;
	}

	public void setBias(double x) {
		bias = x;
	}

	public void setBiasDiff(double x) {
		biasDiff = x;
	}

	public double getBias() {
		return bias;
	}

	public double getBiasDiff() {
		return biasDiff;
	}
	
	public double getIntermediateValue(){
		return intermediateValue;
	}

}
