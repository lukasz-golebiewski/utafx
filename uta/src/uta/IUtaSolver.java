package uta;

import java.util.List;

/**
 * Classes implementing this interface should be able to solve MCDM problems
 * using any of the UTA methods.
 * 
 * @author golebiew
 * 
 */
public interface IUtaSolver {

	/**
	 * Solves the decision problem using UTA method.
	 * 
	 * @param ranking
	 *            - reference ranking of the Decision Maker
	 * @param criteria
	 *            - list of criteria which are to be considered
	 * @param alternatives
	 *            - all defined alternatives (not only those from the reference
	 *            ranking)
	 * @return array of LinearFunctions, the i-th function corresponds to the
	 *         i-th criterion in the param list of criterions
	 */
	LinearFunction[] solve(Ranking<Alternative> ranking, List<Criterion> criteria, List<Alternative> alternatives);

}
