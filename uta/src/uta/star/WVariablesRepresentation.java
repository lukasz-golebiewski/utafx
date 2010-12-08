package uta.star;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the representation of an alternative in terms of W(i,j) variables.
 * 
 * @author masklin
 * 
 */
class WVariablesRepresentation {

	private double[][] w;

	WVariablesRepresentation(double[][] wCoefficients, Object dummy) {
		this.w = wCoefficients;		
	}
	
	WVariablesRepresentation(double[][] mvrCoefficients) {
		create(mvrCoefficients);
	}

	private void create(double[][] mvrCoefficients) {
		this.w = new double[mvrCoefficients.length][];
		for (int i = 0; i < w.length; i++) {
			w[i] = new double[mvrCoefficients[i].length - 1];
		}

		for (int i = 0; i < mvrCoefficients.length; i++) {
			for (int j = 0; j < mvrCoefficients[i].length; j++) {
				double factor = mvrCoefficients[i][j];
				for (int k = 0; k <= j - 1; k++) {
					w[i][k] += factor;
				}
			}
		}

	}

	double[][] getCoefficients() {
		return w;
	}

	Double[] getFlattenedCoefficients() {
		List<Double> result = new ArrayList<Double>();

		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[i].length; j++) {
				result.add(w[i][j]);
			}
		}

		return result.toArray(new Double[0]);

	}
}
