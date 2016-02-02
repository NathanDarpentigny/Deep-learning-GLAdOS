package cnnByGLAdOS;

import java.util.List;
import java.util.ArrayList;

public  class NeuronalNetwork {
	private Layer inputLayer; 
	
	private List<Layer> hiddenLayers;
	private Layer outputLayer;
	
	public NeuronalNetwork(int N,int wi,int hi,int di,int[][] hlpara,int wo,int ho,int d_o){
		inputLayer = new InputLayer(wi,hi,di);
		hiddenLayers = new ArrayList<Layer>();
		
		for(int i=0;i<hlpara.length;i++){
			int a=hlpara[i][0];
			int b=hlpara[i][1];
			int c=hlpara[i][2];
			int d=hlpara[i][3];
			Layer hl= new HiddenLayer(a,b,c,d);// W H D S
			for(int j=0;j<c;j++){
				Kernel k=new Kernel(3,3,c,2.34);// W H D Weight_range
				k.setOutput(hl);
				if(j==0){
					k.setInput(inputLayer);
				}else{
					k.setInput(hiddenLayers.get(j-1));
				}
			}	
			hiddenLayers.add(hl);
		}
		outputLayer = new OutputLayer(wo,ho,d_o);
		
	}
	
	public void setInputs(double[][][] inputs){
		
	}
	
	
	public void fire() {
		for (Layer n : hiddenLayers) {
			n.fire();
		}
		outputLayer.fire();
	}
	
}