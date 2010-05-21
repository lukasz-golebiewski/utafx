package uta;

import java.util.List;

public interface IUtaSolver {

	/**
	 * Solves the decision problem using UTA method.
	 * 
	 * @param ranking
	 *            - reference ranking of the Decision Maker
	 * @param criteria
	 *            - list of criteria which are to be considered
	 * @return array of LinearFunctions, the i-th function corresponds to the
	 *         i-th criterion in the param list of criterions
	 */
	LinearFunction[] solve(ReferenceRanking ranking, List<Criterion> criteria);

}
