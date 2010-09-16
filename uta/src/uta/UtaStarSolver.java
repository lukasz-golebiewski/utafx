package uta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.LinearOptimizer;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.matheclipse.generic.interfaces.Pair;

public class UtaStarSolver implements IUtaSolver {

	private static final KendallHelper KENDALL_HELPER = new KendallHelper();

	private double indifferenceThreshold = 0.05;

	private final boolean doPostOptimalAnalysis;

	private LinearFunction[] bestFunctions;

	private double bestKendall;

	/**
	 * Default UtaStarSolver with post-optimal analysis.
	 */
	public UtaStarSolver() {
		this(true);
	}

	/**
	 * For tests.
	 * 
	 * @param doPostOptimalAnalysis
	 */
	UtaStarSolver(boolean doPostOptimalAnalysis) {
		this.doPostOptimalAnalysis = doPostOptimalAnalysis;
	}

	public synchronized LinearFunction[] solve(Ranking<Alternative> ranking, List<Criterion> criteria,
			List<Alternative> alternatives) {
		setMinMaxValuesOfCriteria(criteria, alternatives);
		return solve(ranking, criteria);
	}

	private void setMinMaxValuesOfCriteria(List<Criterion> criteria, List<Alternative> alternatives) {
		for (Criterion criterion : criteria) {
			double maxValue = Double.MIN_VALUE;
			double minValue = Double.MAX_VALUE;
			for (Alternative alt : alternatives) {
				double val = alt.getValueOn(criterion);
				if (val > maxValue) {
					maxValue = val;
				}
				if (val < minValue) {
					minValue = val;
				}
			}
			if (criterion.isGain()) {
				criterion.setBestValue(maxValue);
				criterion.setWorstValue(minValue);
			} else {
				criterion.setBestValue(minValue);
				criterion.setWorstValue(maxValue);
			}
		}
	}

	public synchronized LinearFunction[] solve(Ranking<Alternative> ranking, List<Criterion> criteria) {

		if (!doPostOptimalAnalysis) {
			return solve_OldImplementation(ranking, criteria);
		}

		this.bestFunctions = null;
		this.bestKendall = -2;
		final double initialThreshold = 1d / (ranking.getNumberOfRanks() - 1d);

		// iteration 1:
		setIndifferenceThreshold(initialThreshold);
		solve_NewImplementation(ranking, criteria);

		// iteration 2:
		setIndifferenceThreshold(initialThreshold / 2);
		solve_NewImplementation(ranking, criteria);

		// iteration 3:
		setIndifferenceThreshold(0.0);
		solve_NewImplementation(ranking, criteria);

		return bestFunctions;
	}

	LinearFunction[] solve_OldImplementation(Ranking<Alternative> ranking, List<Criterion> criteria) {

		List<Alternative> alternatives = ranking.getAlternatives();
		LinearFunction[] functions = createResultFunctions(criteria);

		WVariablesRepresentation[] wReps = createWVariablesRepresentations(alternatives, functions);
		List<LinearConstraint> constraints = prepareConstraints(wReps, ranking);

		int wOffset = wReps[0].getFlattenedCoefficients().length;
		double[] objectiveCoefficients = new double[wOffset + alternatives.size() * 2];
		for (int i = wOffset; i < objectiveCoefficients.length; i++) {
			objectiveCoefficients[i] = 1;
		}
		LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(objectiveCoefficients, 0);

		// do the optimization
		LinearOptimizer optimizer = new SimplexSolver();

		try {
			// initial computation of the solution:
			RealPointValuePair solution = optimizer.optimize(objectiveFunction, constraints, GoalType.MINIMIZE, true);
			populateFunctions(functions, solution);

		} catch (OptimizationException e) {
			handleOptimizationException(e);
		}

		return functions;
	}

	LinearFunction[] solve_NewImplementation(Ranking<Alternative> ranking, List<Criterion> criteria) {

		List<Alternative> alternatives = ranking.getAlternatives();
		LinearFunction[] functions = createResultFunctions(criteria);

		WVariablesRepresentation[] wReps = createWVariablesRepresentations(alternatives, functions);

		List<LinearConstraint> constraints = prepareConstraints(wReps, ranking);

		int wOffset = wReps[0].getFlattenedCoefficients().length;
		double[] objectiveCoefficients = new double[wOffset + alternatives.size() * 2];
		for (int i = wOffset; i < objectiveCoefficients.length; i++) {
			objectiveCoefficients[i] = 1;
		}
		LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(objectiveCoefficients, 0);

		// do the optimization
		LinearOptimizer optimizer = new SimplexSolver();

		try {
			// initial computation of the solution:
			RealPointValuePair solution = optimizer.optimize(objectiveFunction, constraints, GoalType.MINIMIZE, true);
			populateFunctions(functions, solution);

			// if (solution.getValue() > 0) {

			checkKendall(ranking, functions);

			double distance = solution.getValue() + (0.1 * solution.getValue());
			LinearFunction[] optimizedFunctions = doPostOptimalAnalysis(distance, wReps, constraints, alternatives,
					criteria);

			checkKendall(ranking, optimizedFunctions);

			// } else {
			// solution = 0
			// check kendall just in case:
			// Ranking<Alternative> rankFromUtil = buildRank(functions,
			// alternatives);
			// double kendallsCoefficient =
			// KENDALL_HELPER.getCoefficient(ranking, rankFromUtil);
			// if (kendallsCoefficient > bestKendall) {
			// this.bestFunctions = functions;
			// this.bestKendall = kendallsCoefficient;
			// }
			// }
		} catch (OptimizationException e) {
			handleOptimizationException(e);
		}

		return bestFunctions;
	}

	private void checkKendall(Ranking<Alternative> ranking, LinearFunction[] functions) {
		Ranking<Alternative> rankFromUtil = buildRank(functions, ranking.getAlternatives());
		double kendallsCoefficient = KENDALL_HELPER.getCoefficient(ranking, rankFromUtil);
		if (kendallsCoefficient > bestKendall) {
			this.bestFunctions = functions;
			this.bestKendall = kendallsCoefficient;
		}
	}

	private void setIndifferenceThreshold(double value) {
		this.indifferenceThreshold = value;

	}

	LinearFunction[] doPostOptimalAnalysis(double searchDistance, WVariablesRepresentation[] wReps,
			List<LinearConstraint> constraints, List<Alternative> alternatives, List<Criterion> criteria) {

		int numberOfLinearFunctions = wReps[0].getCoefficients().length;
		LinearFunction[] result = createResultFunctions(criteria);
		RealPointValuePair[] intermediateSolutions = new RealPointValuePair[numberOfLinearFunctions];

		// add additional constraint on error variables:
		int wOffset = wReps[0].getFlattenedCoefficients().length;
		int numberOfAllCoefficients = wOffset + alternatives.size() * 2;

		double[] coefficients = new double[numberOfAllCoefficients];
		for (int i = wOffset; i < coefficients.length; i++) {
			coefficients[i] = 1;
		}

		LinearConstraint additionalConstraint = new LinearConstraint(coefficients, Relationship.LEQ, searchDistance);
		constraints.add(additionalConstraint);

		int offset = 0;
		for (int i = 0; i < numberOfLinearFunctions; i++) {
			double[] objectiveCoefficients = new double[numberOfAllCoefficients];
			int j = offset;
			for (j = offset; j < offset + wReps[0].getCoefficients()[i].length; j++) {
				objectiveCoefficients[j] = 1;
			}
			offset = j;
			LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(objectiveCoefficients, 0);
			try {
				intermediateSolutions[i] = new SimplexSolver().optimize(objectiveFunction, constraints,
						GoalType.MAXIMIZE, true);
			} catch (OptimizationException e) {
				handleOptimizationException(e);
			}
		}

		double[] d = new double[numberOfAllCoefficients];

		for (int i = 0; i < numberOfLinearFunctions; i++) {
			for (int j = 0; j < wOffset; j++) {
				d[j] += intermediateSolutions[i].getPoint()[j];
			}
		}

		for (int j = 0; j < wOffset; j++) {
			d[j] /= numberOfLinearFunctions;
		}
		RealPointValuePair solution = new RealPointValuePair(d, -1);
		populateFunctions(result, solution);

		return result;
	}

	private void handleOptimizationException(OptimizationException e) {
		throw new RuntimeException(
				"Simplex solver has thrown an OptimizationException, this should never happen and indicates an error in the program",
				e);
	}

	private void populateFunctions(LinearFunction[] functions, RealPointValuePair solution) {
		int varOffset = 0;
		for (int i = 0; i < functions.length; i++) {
			double increment = 0;
			for (int j = 1; j < functions[i].getNoOfPoints(); j++, varOffset++) {
				increment += solution.getPoint()[varOffset];
				functions[i].addValue(increment, j);
			}
		}
	}

	private LinearFunction[] createResultFunctions(List<Criterion> criteria) {
		LinearFunction[] functions = new LinearFunction[criteria.size()];

		for (int i = 0; i < criteria.size(); i++) {
			functions[i] = new LinearFunction(criteria.get(i));
		}
		return functions;
	}

	List<LinearConstraint> prepareConstraints(WVariablesRepresentation[] wReps, Ranking<Alternative> ranking) {

		List<LinearConstraint> result = new ArrayList<LinearConstraint>();

		List<Alternative> alts = ranking.getAlternatives();

		final int wCoefficientsCount = wReps[0].getFlattenedCoefficients().length;
		final double[] errorCoefficients = new double[] { -1, 1, 1, -1 };
		final int errorCoefficientsCount = alts.size() * 2;

		for (int i = 0; i < alts.size(); i++) {
			Alternative successor = ranking.getSuccessor(alts.get(i));

			// if a successor exists a constraint can be formulated
			if (successor != null) {
				double[] coefficients = new double[wCoefficientsCount + errorCoefficientsCount];
				Double[] firstAlternativeCoefficients = wReps[i].getFlattenedCoefficients();
				Double[] secondAlternativeCoefficients = wReps[i + 1].getFlattenedCoefficients();

				for (int j = 0; j < wCoefficientsCount; j++) {
					coefficients[j] = firstAlternativeCoefficients[j] - secondAlternativeCoefficients[j];
				}

				int errorCoefficientsOffset = wCoefficientsCount + (2 * i);
				for (int j = errorCoefficientsOffset, k = 0; j < errorCoefficientsOffset + errorCoefficients.length; j++, k++) {
					coefficients[j] = errorCoefficients[k];
				}

				if (ranking.sameRank(alts.get(i), successor)) {
					result.add(new LinearConstraint(coefficients, Relationship.EQ, 0));
				} else {
					result.add(new LinearConstraint(coefficients, Relationship.GEQ, indifferenceThreshold));
				}
			}
		}

		// last constraint, sum of all w(i,j) variables = 1
		double[] lastConstraint = new double[wCoefficientsCount + errorCoefficientsCount];
		for (int i = 0; i < wCoefficientsCount; i++) {
			lastConstraint[i] = 1;
		}
		result.add(new LinearConstraint(lastConstraint, Relationship.EQ, 1));

		return result;
	}

	private WVariablesRepresentation[] createWVariablesRepresentations(List<Alternative> alternatives,
			LinearFunction[] functions) {
		MarginalValuesRepresentation[] mvReps = new MarginalValuesRepresentation[alternatives.size()];
		WVariablesRepresentation[] wvReps = new WVariablesRepresentation[alternatives.size()];

		for (int i = 0; i < alternatives.size(); i++) {
			mvReps[i] = new MarginalValuesRepresentation(alternatives.get(i), functions);
			wvReps[i] = new WVariablesRepresentation(mvReps[i].getCoefficients());
		}

		return wvReps;
	}

	Ranking<Alternative> buildRank(LinearFunction[] functions, List<Alternative> alts) {
		return buildRank(functions, alts.toArray(new Alternative[0]));
	}

	Ranking<Alternative> buildRank(LinearFunction[] functions, Alternative[] alts) {

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
			double util = getGeneralUtil(functions, alternative);
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

	double getGeneralUtil(LinearFunction[] functions, Alternative alternative) {
		double util = 0;
		for (LinearFunction function : functions) {
			double value = alternative.getValueOn(function.getCriterion());
			util += function.getValueAt(value);
		}
		return util;
	}

}
