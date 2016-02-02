package cnnByGLAdOS;

import java.util.ArrayList;
import java.util.List;

public class Kernel {

	private double bias=0.;
	private double[][][] kernel;
	private Layer inputLayer;
	private Layer outputLayer;//useless

	public Kernel(int W, int H, int D, double WEIGHT_RANGE){
		double[][][] kernel=new double[W][][];
		for (int i=0 ; i<H; i++){
			kernel[i]=new double[H][];
			for (int j=0; j<H;j++){
				kernel[i][j]=new double [D];
				for(int k=0; k<D;k++){
					kernel[i][j][k]=Math.random() * 2 * WEIGHT_RANGE - (WEIGHT_RANGE / 2);
				}
			}
		}
	}
	public double getBias(){
		return bias;
	}
	public double getvalues(int i, int j, int k){
		return kernel[i][j][k];
	}
	public void setBias(double x){
		bias=x;
	}
	public void setValue(int i, int j, int k, double x){
		kernel[i][j][k]=x;
	}
	public void setOutput(Layer layer){
		outputLayer=layer;		
	}
	public void setInput(Layer layer){
		inputLayer=layer;		
	}
	public Layer getInput(){
		return inputLayer;		
	}
	public Layer getOutput(){
		return outputLayer;		
	}
}