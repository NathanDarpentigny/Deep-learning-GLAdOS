package cnnByGLAdOS;

import java.util.ArrayList;
import java.util.List;

public class InputLayer extends Layer{
	//private List<Kernel> outputKernel;
	private Layer outputLayer;
	//public InputLayer(int W, int H, int D, int Wk,int Hk, int Dk, int WEIGHT_RANGE ) {
		//super(W,H,D);
		//outputKernel = new ArrayList<Kernel>();
		//for(int i=0;i<D;i++){		
		//	Kernel ite = new Kernel(Wk,Hk,Dk,false,WEIGHT_RANGE);
		//	outputKernel.add(ite);
		//	ite.addInput(this);
		//}
	//}
	public InputLayer(int W, int H, int D){
		super(W,H,D);
	}
	public void setOutputLayer(Layer OL){
		outputLayer = OL;
	}
	public Layer getOutputLayer(){
		return outputLayer;
	}
}
