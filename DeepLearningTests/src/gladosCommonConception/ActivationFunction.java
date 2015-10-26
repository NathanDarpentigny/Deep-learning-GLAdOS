package gladosCommonConception;

public abstract class ActivationFunction {
	private static ActivationFunction instance;
	
	abstract public double apply(double x);

	abstract public double applyDerivative(double x);
	
	abstract public double inverse(double x);

	public ActivationFunction getInstance(){
		return instance;
	}
}
