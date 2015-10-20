package gladosCommonConception;

import java.util.ArrayList;
import java.util.List;



public class FeedForward extends NeuralNetwork {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5178307416982024360L;
	private List<AbstractNeuron> inputLayer;
	


	private List<List<AbstractNeuron>> intermediateLayers;
	private List<AbstractNeuron> outputLayer;
	//private double learningRate;
	
	
	public FeedForward(int[] structure, double learningRate){
		//this.learningRate = learningRate;
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
				intermediateLayers.get(i - 1).add(new IntermediateNeuron(structure[i - 1]));
			}
		}

		outputLayer = new ArrayList<AbstractNeuron>();
		for (int j = 0; j < structure[structure.length - 1]; j++) {
			outputLayer.add(new OutputNeuron(structure[structure.length - 2]));
		}
		linkNetwork();
	}
	
	
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

	
	public List<Double> getOutputs() {
		List<Double> res = new ArrayList<Double>();
		for (AbstractNeuron n : outputLayer) {
			res.add(n.getOutput());
		}
		return res;
	}

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

	
	public void linkNetwork() {
		// TODO Auto-generated method stub
		for(AbstractNeuron n : inputLayer){
			if(intermediateLayers.size()>0){
				((InputNeuron) n).outLinkToLayer(intermediateLayers.get(0),inputLayer.size());
			}
			else{
				((InputNeuron) n).outLinkToLayer(outputLayer,inputLayer.size());
			}
		}
		for (AbstractNeuron n : intermediateLayers.get(intermediateLayers.size() - 1)) {
			((IntermediateNeuron) n).outLinkToLayer(outputLayer,intermediateLayers.get(intermediateLayers.size() - 1).size());
		}

		for (int i = 0; i < intermediateLayers.size() - 1; i++) {
			for (AbstractNeuron n : intermediateLayers.get(i)) {
				((IntermediateNeuron) n).outLinkToLayer(intermediateLayers.get(i + 1),intermediateLayers.get(i).size());
			}
		}
	}
	
	public List<Synapse> getAllSynapses(){
		List<Synapse> res = new ArrayList<Synapse>();
		for(AbstractNeuron n : inputLayer){
			res.addAll(((InputNeuron) n).getOutputSynapses());
		}
		for(List<AbstractNeuron> layer : intermediateLayers){
			for(AbstractNeuron n : layer){
				res.addAll(((IntermediateNeuron) n).getOutputSynapses());
			}
		}
		return res;
	}
	
	public List<AbstractNeuron> getAllActiveNeurons(){
		List<AbstractNeuron> res = new ArrayList<AbstractNeuron>();
		for(List<AbstractNeuron> layer : intermediateLayers){
			res.addAll(layer);
		}
		res.addAll(outputLayer);
		return res;
	}


	public final List<AbstractNeuron> getInputLayer() {
		return inputLayer;
	}


	public final List<List<AbstractNeuron>> getIntermediateLayers() {
		return intermediateLayers;
	}


	public final List<AbstractNeuron> getOutputLayer() {
		return outputLayer;
	}

}
