package gladosPackage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
/**
 * This class is build entirely to save the preprocessed images as a
 * <code>List</code> of <code>SourceImage</code>
 */
class Preprocessing {
	List<SourceImage> info;
	
	
	public Preprocessing() {
		
		
	}
	
	public void preprocessAndSave(Path imagesPath, Path labelsPath, int inputLength){
		byte[] rawImagesArray = null;
		byte[] labelsArray = null;
		try {
			rawImagesArray = Files.readAllBytes(imagesPath);
			labelsArray = Files.readAllBytes(labelsPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<SourceImage> savedInfo = LearningWindow.createCleanInput(rawImagesArray, labelsArray);
		int c = 0;
		for (SourceImage img : savedInfo) {
			img.buildRelevantFeatures(inputLength);
			c++;
			System.out.println(c+"/"+60000);
		}
		try{
			FileOutputStream fileOut= new FileOutputStream("src/filesMNIST/dummy.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(savedInfo);
			out.close();
			fileOut.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double[][] getFeatures(){
		double[][] res = null;
		try{
			FileInputStream fileIn= new FileInputStream("src/filesMNIST/imageFeatures.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			res = (double[][]) in.readObject();
			in.close();
			fileIn.close();
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("SourceImage class not found");
			e.printStackTrace();
		}
		//System.out.println("Loaded features");
		return res;
	}
	
	public double[][] getExpectedOutputs(){
		double[][] res = null;
		try{
			FileInputStream fileIn= new FileInputStream("src/filesMNIST/expectedOutputs.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			res = (double[][]) in.readObject();
			in.close();
			fileIn.close();
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("SourceImage class not found");
			e.printStackTrace();
		}
		//System.out.println("Loaded labels");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public List<SourceImage> getPreprocessedImages(){
		try{
			FileInputStream fileIn= new FileInputStream("src/filesMNIST/dummy.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			info = (List<SourceImage>) in.readObject();
			in.close();
			fileIn.close();
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("SourceImage class not found");
			e.printStackTrace();
		}
		return info;
	}
	
	public void saveFeaturesOnly(){
		getPreprocessedImages();
		double[][] res = new double[info.size()][info.get(0).getRelevantFeatures().length];
		
		for(int i = 0;  i<res.length ; i++){
			res[i] = info.get(i).getRelevantFeatures();
		}
		try{
			FileOutputStream fileOut= new FileOutputStream("src/filesMNIST/imageFeatures.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(res);
			out.close();
			fileOut.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void saveExpectedOutputsOnly(){
		getPreprocessedImages();
		double[][] res = new double[info.size()][10];
		for(int i = 0;  i<res.length ; i++){
			res[i] = info.get(i).getExpectedOutput();
		}
		try{
			FileOutputStream fileOut= new FileOutputStream("src/filesMNIST/expectedOutputs.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(res);
			out.close();
			fileOut.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
