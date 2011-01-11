package uta.api;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class RankingUtils {

    private static final Comparator<Alternative> COMPARATOR = new Comparator<Alternative>() {

        @Override
        public int compare(Alternative o1, Alternative o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

        public double getKendallsCoefficient(Ranking<Alternative> referenceRank, Ranking<Alternative> finalRank) {
		return getCoefficient(referenceRank, finalRank);
	}

	/**
	 * @deprecated Use {@link #getKendallsCoefficient(Ranking, Ranking)} instead
	 * @param rankRef
	 * @param rankFromUtil
	 * @return
	 */
	public double getCoefficient(Ranking<Alternative> rankRef, Ranking<Alternative> rankFromUtil) {
		List<Alternative> alternatives = rankRef.getAlternatives();
		List<Alternative> alternativesFromUtil = rankFromUtil.getAlternatives();

		stripRank(rankFromUtil, alternatives, alternativesFromUtil);

		Collections.sort(alternatives, COMPARATOR);
		Collections.sort(alternativesFromUtil, COMPARATOR);

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

		return result;

	}

        /**
         * Removes irrelevant (not existing in reference rank) alternatives from final rank.
         * @param rankFromUtil
         * @param alternatives
         * @param alternativesFromUtil
         */
	private void stripRank(Ranking<Alternative> rankFromUtil,
			List<Alternative> alternatives,
			List<Alternative> alternativesFromUtil) {
		List<Alternative> copyOfalternativesFromUtil = rankFromUtil.getAlternatives();
		for (Alternative alt : copyOfalternativesFromUtil) {
			if (!alternatives.contains(alt))
				alternativesFromUtil.remove(alt);
		}
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

	public Ranking<Alternative> buildRank(LinearFunction[] functions, List<Alternative> alts) {
		return buildRank(functions, alts.toArray(new Alternative[alts.size()]));
	}

	public Ranking<Alternative> buildRank(LinearFunction[] functions, Alternative[] alts) {

		SortedSet<Pair<Alternative, Double>> altsAndUtils = new TreeSet<Pair<Alternative, Double>>(
				new Comparator<Pair<Alternative, Double>>() {

					@Override
					public int compare(Pair<Alternative, Double> o1, Pair<Alternative, Double> o2) {
						int result = Double.compare(o2.getSecond(), o1.getSecond());
						if (result == 0) {
							return 1;
						}
						return result;

					}
				});

		for (Alternative alternative : alts) {
			double util = alternative.getGeneralUtil(functions);
			altsAndUtils.add(new Pair<Alternative, Double>(alternative, util));
		}

		Iterator<Pair<Alternative, Double>> iterator = altsAndUtils.iterator();

		Alternative[] alts2 = new Alternative[alts.length];
		double[] ranking = new double[alts.length];

		double prevUtil = 2.0;
		int rank = 0;
		int i = 0;
		while (iterator.hasNext()) {
			Pair<Alternative, Double> pair = iterator.next();
			pair.getFirst();
			if (pair.getSecond() < prevUtil) {
				rank++;
				prevUtil = pair.getSecond();
			}
			ranking[i] = rank;
			alts2[i] = pair.getFirst();
			i++;
		}

		return new Ranking<Alternative>(ranking, alts2);
	}
}
