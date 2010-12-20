package uta.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a ranking.
 * 
 * @author LG
 * 
 */
public class Ranking<T> {   

	private List<Pair<T, Double>> pairs;

	public Ranking() {
		this.pairs = new ArrayList<Pair<T, Double>>();
	}

	public Ranking(double[] ranking, T... alts) {
		this();
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

	public int getNumberOfRanks() {
		Set<Double> set = new HashSet<Double>();
		for (Pair<T, Double> pair : pairs) {
			set.add(pair.getSecond());
		}
		return set.size();
	}

	public void add(T t, double rank) {
		pairs.add(new Pair<T, Double>(t, rank));
	}

	@Override
	public String toString() {
		Collections.sort(pairs, new Comparator<Pair<T, Double>>() {
			@Override
			public int compare(Pair<T, Double> o1, Pair<T, Double> o2) {
				return o1.getSecond().compareTo(o2.getSecond());
			}
		});
		StringBuilder out = new StringBuilder();
		for (Pair<T, Double> p : pairs) {
			out.append(p.getFirst().toString());
			out.append(" ");
			out.append(p.getSecond().toString());
			out.append("\n");
		}
		return out.toString();
	}
}
