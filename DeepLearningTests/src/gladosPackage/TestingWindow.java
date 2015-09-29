package gladosPackage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

public class TestingWindow extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5218339699571735001L;

	public static void main(String[] args){
		TestingWindow mainWindow = new TestingWindow();
		NeuralNetwork testedNN = null;
		try{
			FileInputStream fileIn= new FileInputStream("src/resultingNN/latest.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			
			try {
				testedNN = (NeuralNetwork) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			in.close();
			fileIn.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		mainWindow.setSize(300,200);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setTitle("Image Preview");
		mainWindow.setVisible(true);
		Scanner user_input;
		while(true){
			user_input = new Scanner(System.in);
			int n = 0;
			System.out.print("Select an image number  ");
			String nString = user_input.next();
			n = Integer.parseInt(nString);
			mainWindow.setContentPane(new ImageDisplay(n));
			mainWindow.revalidate();
			user_input.close();
			Path trainImages = FileSystems.getDefault().getPath("src/filesMNIST", "train-images.idx3-ubyte");
			Path trainLabels = FileSystems.getDefault().getPath("src/filesMNIST", "train-labels.idx1-ubyte");
			byte[] rawImagesArray = null;
			byte[] labelsArray = null;
			try {
				rawImagesArray = Files.readAllBytes(trainImages);
				labelsArray = Files.readAllBytes(trainLabels);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<SourceImage> cleanInput = LearningWindow.createCleanInput(rawImagesArray,labelsArray);
			SourceImage currentImage = cleanInput.get(n);
			currentImage.buildRelevantFeatures(testedNN.getInputLayer().size());
			testedNN.setInputs(currentImage.getRelevantFeatures());
			testedNN.fire();
			double max = 0;
			int c = -1;
			for(int i = 0 ; i< testedNN.getOutputs().size(); i++){
				if(testedNN.getOutputs().get(i)>max){
					max =testedNN.getOutputs().get(i);
					c = i;
				}
			}
			System.out.println(c);
			
		}
		
		
		
	}
	
	public TestingWindow(){
		super();
		
	}
}
