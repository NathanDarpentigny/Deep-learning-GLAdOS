package gladosPackage;

public abstract class ActivationFunction {
	private static ActivationFunction instance;
	
	abstract public double apply(double x);

	abstract public double applyDerivative(double x);

	public static ActivationFunction getInstance(){
		return instance;
	}
}
