package cnnByGLAdOS;

import java.util.ArrayList;
import java.util.List;

public class HiddenLayer extends Layer{	
	private List<Kernel> inputKernel;
	private Layer outputLayer;
	private double [][][] maxPos;
	private int sampling;
	
//	public HiddenLayer( int W, int H, int D, boolean rgb, double WEIGHT_RANGE, int scale,int depth){
////		Conv= new Kernel(Math.floor(W/scale),Math.floor(H/scale),Math.floor(D/scale),rgb,WEIGHT_RANGE);
//		Conv= new Kernel(scale,scale,depth,rgb,WEIGHT_RANGE);
//		
//	}
	public HiddenLayer(int W, int H, int D, int S){
		super(W,H,D);
		sampling=S;
		inputKernel =new ArrayList<Kernel>();
		maxPos = new double[W][][];
		for (int i=0 ; i<H; i++){
			maxPos[i]=new double[H][];
			for (int j=0; j<H;j++){
				maxPos[i][j]=new double [2];
				maxPos[i][j][0]=0;
				maxPos[i][j][1]=0;
			}
		}
		
		
	}
	
	public void setOutputLayer(Layer l){
		outputLayer = l;
	}
	public void addInputKernel( Kernel k){
		inputKernel.add(k);
	}
	public Layer getOutputLayer(){
		return outputLayer;	
	}
	//public Layer getInputLayer(){
		//return inputKernel(0).getInput();;	
	//}
	public List<Kernel> getInputKernel(){
		return inputKernel;	
	}
	
	
	// à changer
	public void findMaxPos(int division){
		for(int d=0; d<this.matrice[0][0].length;d++){
			double maxi=this.matrice[0][0][d];
			int wMax=0;
			int hMax=0;
			for(int w=0; w<this.matrice.length;w++){
				for(int h=0; h<this.matrice[0].length;h++){
					if(maxi<this.matrice[w][h][d]){
						maxi=this.matrice[w][h][d];
						wMax=w;
						hMax=h;
					}
				}
			}
		}
	}

}
