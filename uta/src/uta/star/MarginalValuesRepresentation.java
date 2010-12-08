package uta.star;

import uta.api.LinearFunction;
import uta.api.Alternative;
import java.util.Arrays;
import java.util.List;

/**
 * Contains the representation of an alternative in terms of functions' marginal
 * values
 * 
 * @author masklin
 * 
 */
class MarginalValuesRepresentation {

	private final Alternative alternative;
	private final List<LinearFunction> functions;

	private final double[][] coefficients;

	MarginalValuesRepresentation(Alternative alternative, LinearFunction[] functions) {
		this(alternative, Arrays.asList(functions));
	}

	MarginalValuesRepresentation(Alternative alternative, List<LinearFunction> functions) {
		this.alternative = alternative;
		this.functions = functions;
		this.coefficients = new double[functions.size()][];
		this.create();
	}

	private void create() {
		// init vectors
		for (int i = 0; i < functions.size(); i++) {
			coefficients[i] = new double[functions.get(i).getNoOfPoints()];
		}

		// interpolate
		for (int i = 0; i < functions.size(); i++) {
			LinearFunction currentFunction = functions.get(i);

			double evaluation = alternative.getValueOn(currentFunction.getCriterion());
			Double[] charPoints = currentFunction.getCharacteristicPoints();
			boolean increasingFunction = currentFunction.isIncreasing();

			for (int j = 0; j < charPoints.length; j++) {
				if (evaluation == charPoints[j]) {
					coefficients[i][j] = 1.0;
					break;
				}
				if (increasingFunction) {
					if (evaluation < charPoints[j]) {
						double interval = charPoints[j] - charPoints[j - 1];
						coefficients[i][j - 1] = 1.0 - ((evaluation - charPoints[j - 1]) / interval);
						coefficients[i][j] = ((evaluation - charPoints[j]) / interval);
					}
				} else {
					if (evaluation > charPoints[j]) {
						double interval = charPoints[j - 1] - charPoints[j];
						coefficients[i][j - 1] = ((evaluation - charPoints[j]) / interval);
						coefficients[i][j] = ((charPoints[j - 1] - evaluation) / interval);

					}
				}
			}
		}

	}

	double[][] getCoefficients() {
		return coefficients;
	}

}
