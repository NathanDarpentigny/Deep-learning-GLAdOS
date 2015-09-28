package gladosPackage;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JFrame;

public class LearningWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	//private VisualPanel contentPane;
	//private static double[] expectedResult = new double[]{0.8,0.4,0.6};

	public static void main(String[] args) {
		//LearningWindow frame = new LearningWindow();
		Path trainImages = FileSystems.getDefault().getPath("C:/Users/Laty/workspace/DeepLearningTests/bin/newTest/TrainImages", "TrainImages.idx3-ubyte");
		Path trainLabels = FileSystems.getDefault().getPath("C:/Users/Laty/workspace/DeepLearningTests/bin/newTest/TrainLabels", "TrainLabels.idx1-ubyte");
		byte[] imagesArray = null;
		byte[] labelsArray = null;
		try {
			imagesArray = Files.readAllBytes(trainImages);
			labelsArray = Files.readAllBytes(trainLabels);
			//System.out.println(new String(imagesArray,"ISO-8859-1"));
			
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//NeuralNetwork learningNN = new NeuralNetwork(new int[] {784,392,196,98,10},0.5);
		NeuralNetwork learningNN = new NeuralNetwork(new int[] {2,2,1},0.2);
		double errorPerEpoch = 0.;
		double[] expectedOutput;
		for(int i = 0 ; i<1000; i++){
			for(int j = 0 ; j<10 ; j++){
				double inputA = Math.floor(Math.random()*2);
				double inputB = Math.floor(Math.random()*2);
				learningNN.setInputs(new double [] {inputA,inputB});
				//learningNN.setInputs(createInput(i*30+j,imagesArray));
				learningNN.fire();
				//expectedOutput = createExpectedOutput(i*30+j, labelsArray);
				
				if(inputA == inputB){
					expectedOutput = new double[] {inputA};
				}
				else{
					expectedOutput = new double[] {inputA+inputB};
				}
				learningNN.calculateNeuronDiffs(expectedOutput);
				learningNN.incrementWeightDiffs();
				
				//System.out.println("Got "+ learningNN.getOutputs() + " instead of "+ expectedOutput[0]);
				
				errorPerEpoch += currentError(expectedOutput,learningNN.getOutputs());
			}
			learningNN.incrementWeights();
			learningNN.resetWeightDiffs();
			System.out.println(errorPerEpoch);
			
			errorPerEpoch = 0.;
			
			
		}
	
	}
	
	
	private static double currentError(double[] expectedOutput, List<Double> outputs) {
		double res = 0;
		for(int i = 0 ; i< expectedOutput.length ; i++){
			res+=(expectedOutput[i]-outputs.get(i))*(expectedOutput[i]-outputs.get(i));
		}
		res = res/2.;
		return res;
	}


	public LearningWindow(){
		
		//contentPane = new VisualPanel(expectedResult[0]*500,expectedResult[1]*500,expectedResult[2]*50);
		setTitle("Visualisation");
		setSize(500,500);
		setLocationRelativeTo(null);
		setUndecorated(true);
		//contentPane.setBackground(Color.ORANGE);
		//setContentPane(contentPane);
		//setVisible(true);
		
	}
	
	public void setX(double x){
		//contentPane.setX(x);
		
	}
	public void setY(double y){
		//contentPane.setY(y);
	}
	
	public void setW(double w){
		//contentPane.setW(w);
	}
	
	public static double[] createInput(int imageNumber, byte[] imagesArray){
		double [] res = new double[784];
		;
		for(int i = 0 ; i< 28*28 ; i++){
			res[i]= (imagesArray[16+imageNumber*(28*28)+i])/128.;
		}
		return res;
	}
	
	public static double[] createExpectedOutput(int imageNumber, byte[] labelsArray){
		double[] res = new double[10];
		res[labelsArray[8+imageNumber]] =1.;
		return res;
	}
}
