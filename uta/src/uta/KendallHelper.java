package uta;

import java.util.Collections;
import java.util.List;

public class KendallHelper {

	public double getCoefficient(Ranking<Alternative> rankRef, Ranking<Alternative> rankFromUtil) {
		List<Alternative> alternatives = rankRef.getAlternatives();
		List<Alternative> alternativesFromUtil = rankFromUtil.getAlternatives();

		Collections.sort(alternatives);
		Collections.sort(alternativesFromUtil);

		int matrixSize = alternatives.size();

		double[][] matrix1 = new double[matrixSize][matrixSize];
		populateMatrix(rankRef, alternatives, matrix1);

		double[][] matrix2 = new double[matrixSize][matrixSize];

		populateMatrix(rankFromUtil, alternativesFromUtil, matrix2);

		double sum = 0;
		for (int i = 0; i < matrix2.length; i++) {
			for (int j = 0; j < matrix2.length; j++) {
				sum += Math.abs(matrix1[i][j] - matrix2[i][j]);
			}
		}

		double result = 1 - 4 * ((0.5 * sum) / (matrixSize * (matrixSize - 1)));
		// System.out.println(result);

		return result;

	}

	private void populateMatrix(Ranking<Alternative> rank, List<Alternative> alternatives, double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (i == j || rank.getRank(alternatives.get(j)) > rank.getRank(alternatives.get(i))) {
					matrix[i][j] = 0;
				} else if (rank.getRank(alternatives.get(j)) == rank.getRank(alternatives.get(i))) {
					matrix[i][j] = 0.5;
				} else {
					matrix[i][j] = 1;
				}

			}
		}
	}
}
