package uta;

import java.util.Arrays;
import java.util.List;

/**
 * This class holds the reference ranking of the decision maker.
 * 
 * @author masklin
 * 
 */
public class ReferenceRanking {

	private List<Alternative> alternatives;

	private double[] ranking;

	public ReferenceRanking(double[] ranking, Alternative... alternatives) {
		this.ranking = ranking;
		this.alternatives = Arrays.asList(alternatives);
	}

	public Alternative[] getAlternatives() {
		return alternatives.toArray(new Alternative[0]);
	}

	public Alternative getSuccessor(Alternative alternative) {
		int index = alternatives.indexOf(alternative);
		if (++index >= alternatives.size())
			return null;

		return alternatives.get(index);
	}

	public boolean sameRank(Alternative alt1, Alternative alt2) {
		int index1 = alternatives.indexOf(alt1);
		int index2 = alternatives.indexOf(alt2);
		return ranking[index1] == ranking[index2];
	}

}
