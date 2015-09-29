package gladosPackage;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

public class LearningWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final boolean SIMPLE = false;
	private static final boolean VAR_LEARNING_RATE = false;
	private static final boolean SHOW_ERROR_PER_EPOCH = true;
	private static final boolean PREPROCESSING = false;
	private static final boolean SHOW_OUTPUT = false;
	private static final boolean SHOW_IMAGE = false;
	private static final boolean STANDARD_OUTPUT = true;
	
	
	public static final double LEARNING_RATE =0.;
//	private static final double MAX_LR = 2.;
	public static final double DECREASE_LR = 0.5;
	public static final double INCREASE_LR = 2;
	private static final double MOMENTUM_RATE = 0.5; //0 is equivalent to no momentum.
	private static final int EPOCH_SIZE = 100;
	//private VisualPanel contentPane;
	//private static double[] expectedResult = new double[]{0.8,0.4,0.6};

	
	public static void main(String[] args) {
		//LearningWindow frame = new LearningWindow();
		Path trainImages = FileSystems.getDefault().getPath("src/filesMNIST", "train-images.idx3-ubyte");
		Path trainLabels = FileSystems.getDefault().getPath("src/filesMNIST", "train-labels.idx1-ubyte");
		byte[] rawImagesArray = null;
		byte[] labelsArray = null;
		try {
			rawImagesArray = Files.readAllBytes(trainImages);
			labelsArray = Files.readAllBytes(trainLabels);
			//System.out.println(new String(imagesArray,"ISO-8859-1"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		double errorPerEpoch = 0.;
		double lastEpochError = 1.;
		double learningRate = LEARNING_RATE;
		double[] expectedOutput = null;
		double[] input;
		byte[] show = null;
		
		
		if(!SIMPLE){
			JFrame mainwindow = new JFrame();
			if(SHOW_IMAGE){
				
				mainwindow.setSize(300,200);
				mainwindow.setLocationRelativeTo(null);
				mainwindow.setTitle("Image Preview");
				mainwindow.setVisible(true);
			}
			NeuralNetwork learningNN;
			if(PREPROCESSING){
				learningNN = new NeuralNetwork(new int[] {100,50,10},learningRate);
			}
			else{
				learningNN = new NeuralNetwork(new int[] {28*28,100,40,10},learningRate);
			}
			List<SourceImage> cleanInput = createCleanInput(rawImagesArray);
			for(int i = 0 ; i<cleanInput.size()/EPOCH_SIZE; i++){
				for(int j = 0 ; j<EPOCH_SIZE ; j++){
					show = cleanInput.get(i*EPOCH_SIZE+j).getCleanRawImage();
					if(PREPROCESSING){
						cleanInput.get(i*EPOCH_SIZE+j).buildRelevantFeatures(100);
						input = cleanInput.get(i*EPOCH_SIZE+j).getRelevantFeatures();
					}
					else{
						input = cleanInput.get(i*EPOCH_SIZE+j).getCleanRawDoubleImage();
					}			
					learningNN.setInputs(input);
					learningNN.fire();
					expectedOutput = createExpectedOutput(i*EPOCH_SIZE+j, labelsArray);
					learningNN.calculateNeuronDiffs(expectedOutput);
					learningNN.incrementWeightDiffs();
					learningNN.incrementWeights();
					learningNN.resetWeightDiffsMomentum(MOMENTUM_RATE);
					errorPerEpoch += currentError(expectedOutput,learningNN.getOutputs());
				}
				if(SHOW_IMAGE){
					mainwindow.setContentPane(new ImageDisplay(show,expectedOutput));
					mainwindow.repaint();
					mainwindow.revalidate();
				}
				if(SHOW_OUTPUT){
					System.out.println(learningNN.getOutputs());
				}
				
				learningNN.incrementWeights();
				learningNN.resetWeightDiffsMomentum(MOMENTUM_RATE);
				if(SHOW_ERROR_PER_EPOCH){
					System.out.println(errorPerEpoch);
				}
				if(VAR_LEARNING_RATE){
					if(errorPerEpoch>lastEpochError){
						learningNN.resetLR();
					}
					else{
						learningNN.varyLR();
					}
				}
				lastEpochError = errorPerEpoch;
				errorPerEpoch = 0.;
			}
		}
		
		else{
			NeuralNetwork learningNN = new NeuralNetwork(new int[] {2,3,4,1},learningRate);
			for(int i = 0 ; i<10000/EPOCH_SIZE; i++){
				for(int j = 0 ; j<EPOCH_SIZE ; j++){
					double inputA = Math.floor(Math.random()*2);
					double inputB = Math.floor(Math.random()*2);
					learningNN.setInputs(new double [] {inputA,inputB});
					learningNN.fire();
					if(inputA == inputB){
						expectedOutput = new double[] {inputA};
					}
					else{
						expectedOutput = new double[] {inputA+inputB};
					}
					learningNN.calculateNeuronDiffs(expectedOutput);
					learningNN.incrementWeightDiffs();
					
					//System.out.println("Got "+ learningNN.getOutputs() + " instead of "+ expectedOutput[0]);
					learningNN.incrementWeights();
					learningNN.resetWeightDiffsMomentum(MOMENTUM_RATE);
					errorPerEpoch += currentError(expectedOutput,learningNN.getOutputs());
				}
//				learningNN.incrementWeights();
//				//learningNN.resetWeightDiffs();
//				learningNN.resetWeightDiffsMomentum(MOMENTUM_RATE);
				if(SHOW_ERROR_PER_EPOCH){
					System.out.println(errorPerEpoch);
				}
				errorPerEpoch = 0.;
				
			}
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
	
	public static List<SourceImage> createCleanInput(byte[] rawImagesArray) {
		List<SourceImage> res = new ArrayList<SourceImage>();
		int imageSize = 28;
		int init = 16;
		int c = 0;
		
		
		while(init + (imageSize*imageSize)*c<rawImagesArray.length){
			byte[] temp =Arrays.copyOfRange(rawImagesArray, init + (imageSize*imageSize*c), init + imageSize*imageSize*(c+1));
			
			res.add(new SourceImage(temp,imageSize));
			c++;
		}
		return res;
	}
	
	public static double[] createExpectedOutput(int imageNumber, byte[] labelsArray){
		double[] res;
		if(STANDARD_OUTPUT){
			res = new double[10];
			res[labelsArray[8+imageNumber]] =1.;
		}
		else{
			res = new double[]{0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
			res[labelsArray[8+imageNumber]] =0.9;
		}
		
		return res;
	}
}
