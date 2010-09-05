package uta;

import java.util.ArrayList;
import java.util.List;

import org.matheclipse.generic.interfaces.Pair;

/**
 * This class represents a ranking.
 * 
 * @author masklin
 * 
 */
public class Ranking<T> {

	private List<Pair<T, Double>> pairs;

	public Ranking(double[] ranking, T... alts) {

		this.pairs = new ArrayList<Pair<T, Double>>();
		for (int i = 0; i < alts.length; i++) {
			pairs.add(new Pair<T, Double>(alts[i], ranking[i]));
		}
	}

	public List<T> getAlternatives() {
		List<T> result = new ArrayList<T>();
		for (int i = 0; i < pairs.size(); i++) {
			result.add(pairs.get(i).getFirst());
		}
		return result;
	}

	public T getSuccessor(T alternative) {
		double rankOfCurrent = getRank(alternative);

		T result = null;
		double previousRank = Long.MAX_VALUE;
		boolean currentPassed = false;

		for (int i = 0; i < pairs.size(); i++) {
			Pair<T, Double> processed = pairs.get(i);
			if (!processed.getFirst().equals(alternative)) {
				if (currentPassed) {
					if (processed.getSecond() >= rankOfCurrent && processed.getSecond() < previousRank) {
						previousRank = processed.getSecond();
						result = processed.getFirst();
					}
				} else {
					if (processed.getSecond() > rankOfCurrent && processed.getSecond() < previousRank) {
						previousRank = processed.getSecond();
						result = processed.getFirst();
					}
				}
			} else {
				currentPassed = true;
			}
		}

		return result;
	}

	public boolean sameRank(T alt1, T alt2) {
		return getRank(alt1) == getRank(alt2);
	}

	public double getRank(T a) {
		for (int i = 0; i < pairs.size(); i++) {
			if (pairs.get(i).getFirst().equals(a)) {
				return pairs.get(i).getSecond();
			}
		}
		return -1;
	}

}
