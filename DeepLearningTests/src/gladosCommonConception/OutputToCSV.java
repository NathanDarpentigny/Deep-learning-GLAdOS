package gladosCommonConception;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OutputToCSV {
	
	public static List<Double> NumberOfExample= new ArrayList<Double>();
	public static List<Double> AverageQuadraticErrorTest= new ArrayList<Double>();
	public static List<Double> AverageQuadraticErrorLearning= new ArrayList<Double>();
	public static List<Double> MistakePerEpochTest= new ArrayList<Double>();
	public static List<Double> MistakePerEpochLearning= new ArrayList<Double>();

	public static void generateCsvFile(String ErrorMistake)
	   {
		try
		{
		    FileWriter writer = new FileWriter("");
			
		    writer.append("NumberOfExample");
		    writer.append(',');
		    writer.append("AverageQuadraticErrorTest");
		    writer.append(',');
		    writer.append("AverageQuadraticErrorLearning");
		    writer.append(',');
		    writer.append("MistakePerEpochTest");
		    writer.append(',');
		    writer.append("MistakePerEpochLearning");
		    writer.append('\n');
		    
	
			for(int i=1; i < NumberOfExample.size()-1; i++) {			
				writer.append(NumberOfExample.get(i).toString());
			    writer.append(',');
			    
			    writer.append(AverageQuadraticErrorTest.get(i).toString());
			    writer.append(',');
			    
			    writer.append(AverageQuadraticErrorLearning.get(i).toString());
			    writer.append(',');
			    
			    writer.append(MistakePerEpochTest.get(i).toString());
			    writer.append(',');
			    
			    writer.append(MistakePerEpochLearning.get(i).toString());
			    writer.append('\n');	

			}
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	    }
	}