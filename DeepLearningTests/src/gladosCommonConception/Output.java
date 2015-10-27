package gladosCommonConception;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Output {

	private  List<Double> numberOfExample = new ArrayList<Double>();
	private  List<Double> averageQuadraticErrorTest = new ArrayList<Double>();
	private  List<Double> averageQuadraticErrorLearning = new ArrayList<Double>();
	private  List<Double> mistakePerEpochTest = new ArrayList<Double>();
	private  List<Double> mistakePerEpochLearning = new ArrayList<Double>();

	public Output(List<Double> numberOfExample, List<Double> averageQuadraticErrorTest,
			List<Double> averageQuadraticErrorLearning, List<Double> mistakePerEpochTest,
			List<Double> mistakePerEpochLearning) {
		this.numberOfExample = numberOfExample;
		this.averageQuadraticErrorTest = averageQuadraticErrorTest;
		this.averageQuadraticErrorLearning = averageQuadraticErrorLearning;
		this. mistakePerEpochTest = mistakePerEpochTest;
		this.mistakePerEpochLearning = mistakePerEpochLearning;
	}
	
	public Output(){
	}

	public final List<Double> getNumberOfExample() {
		return numberOfExample;
	}

	public final void setNumberOfExample(List<Double> numberOfExample) {
		this.numberOfExample = numberOfExample;
	}

	public final List<Double> getAverageQuadraticErrorTest() {
		return averageQuadraticErrorTest;
	}

	public final void setAverageQuadraticErrorTest(List<Double> averageQuadraticErrorTest) {
		this.averageQuadraticErrorTest = averageQuadraticErrorTest;
	}

	public final List<Double> getAverageQuadraticErrorLearning() {
		return averageQuadraticErrorLearning;
	}

	public final void setAverageQuadraticErrorLearning(List<Double> averageQuadraticErrorLearning) {
		this.averageQuadraticErrorLearning = averageQuadraticErrorLearning;
	}

	public final List<Double> getMistakePerEpochTest() {
		return mistakePerEpochTest;
	}

	public final void setMistakePerEpochTest(List<Double> mistakePerEpochTest) {
		this.mistakePerEpochTest = mistakePerEpochTest;
	}

	public final List<Double> getMistakePerEpochLearning() {
		return mistakePerEpochLearning;
	}

	public final void setMistakePerEpochLearning(List<Double> mistakePerEpochLearning) {
		this.mistakePerEpochLearning = mistakePerEpochLearning;
	}

	public void generateCsvFile(String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName+".csv");

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

			for (int i = 0; i < numberOfExample.size(); i++) {
				writer.append(numberOfExample.get(i).toString());
				writer.append(',');

				writer.append(averageQuadraticErrorTest.get(i).toString());
				writer.append(',');

				writer.append(averageQuadraticErrorLearning.get(i).toString());
				writer.append(',');

				writer.append(mistakePerEpochTest.get(i).toString());
				writer.append(',');

				writer.append(mistakePerEpochLearning.get(i).toString());
				writer.append('\n');

			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}