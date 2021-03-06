package gladosPackage;

public class Sigmoid extends ActivationFunction {

	
	public double apply(double x) {	
		if (x < -45.) {
			return 0;
		} else if (x > 45.) {
			return 1;
		} else {
			return 1. / (1 + Math.exp(-x));
		}
	}

	
	public double applyDerivative(double x) {
		return apply(x)*(1-apply(x));
	}

}
