package cnnByGLAdOS;

import java.util.ArrayList;
import java.util.List;

public class OutputLayer extends Layer{
	private List<Kernel> inputKernel;
	
	
//	public HiddenLayer( int W, int H, int D, boolean rgb, double WEIGHT_RANGE, int scale,int depth){
////		Conv= new Kernel(Math.floor(W/scale),Math.floor(H/scale),Math.floor(D/scale),rgb,WEIGHT_RANGE);
//		Conv= new Kernel(scale,scale,depth,rgb,WEIGHT_RANGE);
//		
//	}
	public OutputLayer(int W, int H, int D){
		super(W,H,D);
		inputKernel= new ArrayList<Kernel>();
	}
	public void addInputKernel(Kernel k){
		inputKernel.add(k);
	}
	public List<Kernel> getInputKernel(){
		return inputKernel;
	}
}
