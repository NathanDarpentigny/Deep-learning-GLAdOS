package gladosPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestImageDisplay extends JPanel {
	
	private int[][] imageMatrix = new int[28][28];
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame mainwindow = new JFrame();
		mainwindow.setSize(300,200);
		mainwindow.setLocationRelativeTo(null);
		mainwindow.setTitle("Image Preview");
		mainwindow.setContentPane(new TestImageDisplay());
		mainwindow.setVisible(true);
	}

	public TestImageDisplay() {
		super();
		Path trainImages = FileSystems.getDefault().getPath("src/filesMNIST", "train-images.idx3-ubyte");
		Path trainLabels = FileSystems.getDefault().getPath("src/filesMNIST", "train-labels.idx1-ubyte");
		byte[] imagesArray;
		byte[] labelsArray;
		try {
			imagesArray = Files.readAllBytes(trainImages);
			labelsArray = Files.readAllBytes(trainLabels);
			int c = 42;
			int start = 16+c*(28*28);
			for(int i = start ; i<(28*28)+start ; i++){
				//System.out.println(imagesArray[i]);
				imageMatrix [(i-start)%28][(i-start)/28] = (int)imagesArray[i];
			}
			System.out.println(labelsArray[8+c]);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int value;
		for(int i = 0 ; i< imageMatrix.length; i++){
			for(int j = 0 ; j<imageMatrix[i].length ; j++){
				if(imageMatrix[i][j] != 0){
					 value = 127-imageMatrix[i][j];
				}
				else{
					 value = 255;
				}
				
				g.setColor(new Color(value,value,value));
				g.fillRect(5*i, 5*j, 5, 5);
			}
		}
	}
}
