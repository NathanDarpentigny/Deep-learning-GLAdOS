package gladosCommonConception;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class is a window which allows the test of neural networks
 * when recognizing written characters.
 * @author Laty
 *
 */
public class TestingWindow extends JFrame{
	NeuralNetwork testedNN;
	boolean nnIsPreprocessed;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5218339699571735001L;
	private JPanel numbershowing;
	private JTextField txtEnterAnImage;
	private JPanel sidePanel;
	private JSpinner spinner;
	private JButton btnOK;
	private JPanel panel;
	private JTextField showOutput;
	
	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		@SuppressWarnings("unused")
		TestingWindow mainWindow = new TestingWindow();
		
		
	}
	
	@SuppressWarnings("unused")
	private void setNN(NeuralNetwork testedNN) {
		this.testedNN = testedNN;
		
	}

	public TestingWindow(){
		super();
		setSize(300,200);
		setLocationRelativeTo(null);
		setTitle("Image Preview");
		
		numbershowing = new JPanel();
		numbershowing.setPreferredSize(new Dimension(300,200));
		getContentPane().add(numbershowing, BorderLayout.NORTH);
		
		JPanel midPanel = new JPanel();
		getContentPane().add(midPanel, BorderLayout.SOUTH);
		midPanel.setLayout(new BorderLayout(0, 0));
		
		txtEnterAnImage = new JTextField();
		txtEnterAnImage.setEditable(false);
		txtEnterAnImage.setMinimumSize(new Dimension(500, 20));
		txtEnterAnImage.setPreferredSize(new Dimension(500, 20));
		txtEnterAnImage.setText("Enter an image number");
		midPanel.add(txtEnterAnImage, BorderLayout.CENTER);
		txtEnterAnImage.setColumns(10);
		
		sidePanel = new JPanel();
		midPanel.add(sidePanel, BorderLayout.EAST);
		sidePanel.setLayout(new BorderLayout(0, 0));
		
		spinner = new JSpinner();
		sidePanel.add(spinner, BorderLayout.WEST);
		
		panel = new JPanel();
		midPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		showOutput = new JTextField();
		showOutput.setEditable(false);
		panel.add(showOutput, BorderLayout.CENTER);
		showOutput.setColumns(10);
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestingWindow.this.setNumberShowingPanel(new ImageDisplayPanel((int)spinner.getValue()));
				TestingWindow.this.pack();
				TestingWindow.this.revalidate();
				Path trainImages = FileSystems.getDefault().getPath("src/filesMNIST", "train-images.idx3-ubyte");
				Path trainLabels = FileSystems.getDefault().getPath("src/filesMNIST", "train-labels.idx1-ubyte");
				byte[] rawImagesArray = null;
				byte[] labelsArray = null;
				try {
					rawImagesArray = Files.readAllBytes(trainImages);
					labelsArray = Files.readAllBytes(trainLabels);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				List<SourceImage> cleanInput = LearningWindow.createCleanInput(rawImagesArray,labelsArray);
				SourceImage currentImage = cleanInput.get((int)spinner.getValue());
				if(nnIsPreprocessed){
					currentImage.buildRelevantFeatures(testedNN.getInputLayer().size());
					testedNN.setInputs(currentImage.getRelevantFeatures());
				}
				else{
					testedNN.setInputs(currentImage.getCleanRawDoubleImage());
				}
				
				testedNN.fire();
				double max = 0;
				int c = -1;
				for(int i = 0 ; i< testedNN.getOutputs().size(); i++){
					if(testedNN.getOutputs().get(i)>max){
						max =testedNN.getOutputs().get(i);
						c = i;
					}
				}
				showOutput.setText("The Neuron Network reads : " + c + "| The label says : "+labelsArray[8+(int)spinner.getValue()]);
			}
		});
		sidePanel.add(btnOK, BorderLayout.EAST);
		getRootPane().setDefaultButton(btnOK);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		pack();
		revalidate();
		
		JFileChooser chooser = new JFileChooser(".");
		FileNameExtensionFilter preFilter = new FileNameExtensionFilter("Preprocessed NeuralNetworks","pre");
		FileNameExtensionFilter rwFilter = new FileNameExtensionFilter("Raw NeuralNetworks","rw");
		chooser.setFileFilter(preFilter);
		chooser.addChoosableFileFilter(rwFilter);
		int returnVal = chooser.showOpenDialog(this);

		try {
			// System.out.println(chooser.getSelectedFile().getPath());

			if (returnVal ==JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
				nnIsPreprocessed = chooser.getSelectedFile().toString().endsWith("pre");
				try{
					FileInputStream fileIn= new FileInputStream(chooser.getSelectedFile());
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
				
			}
			else{
				this.dispose();
			}
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		
		
		
		
		
	}
	
	public void setNumberShowingPanel(JPanel num){
		getContentPane().remove(numbershowing);
		numbershowing = num;
		getContentPane().add(numbershowing);
		
		
		
		this.pack();
	}
}
