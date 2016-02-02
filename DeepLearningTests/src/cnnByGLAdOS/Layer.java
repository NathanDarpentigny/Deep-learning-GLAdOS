package cnnByGLAdOS;


public abstract class Layer{
	protected double[][][] matrice;
	//private double LayerDiff;

	public Layer(int W,int H,int D){
		matrice=new double[W][][];
		for (int i=0 ; i<H; i++){
			matrice[i]=new double[H][];
			for (int j=0; j<H;j++){
				matrice[i][j]=new double [D];
			}
		}
	}
	
	public void setLayer(double[][][] newLayer){
		matrice=newLayer;
	}
	public void setValue(int i, int j, int k, double x){
		matrice[i][j][k]=x;
	}
	public double getValue(int i,int j,int k){
		return matrice[i][j][k];
	}
	public double[][][] getMatrice(){
		return matrice;
	}
	public void fire(){
		//TODO
	}
}
