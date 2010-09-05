package uta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.LinearOptimizer;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;

public class UtaStarSolver implements IUtaSolver {

	private double indifferenceTreshold = 0.05;

	@Override
	public LinearFunction[] solve(Ranking<Alternative> ranking, List<Criterion> criteria) {

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
			RealPointValuePair solution = optimizer.optimize(objectiveFunction, constraints, GoalType.MINIMIZE, true);
			populateFunctions(functions, solution);
		} catch (OptimizationException e) {
			// Unbounded or not feasible?
			e.printStackTrace();
		}

		return functions;
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

	private List<LinearConstraint> prepareConstraints(WVariablesRepresentation[] wReps, Ranking<Alternative> ranking) {

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
					result.add(new LinearConstraint(coefficients, Relationship.GEQ, indifferenceTreshold));
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

	private WVariablesRepresentation[] createWVariablesRepresentations(List<Alternative> alternatives, LinearFunction[] functions) {
		MarginalValuesRepresentation[] mvReps = new MarginalValuesRepresentation[alternatives.size()];
		WVariablesRepresentation[] wvReps = new WVariablesRepresentation[alternatives.size()];

		for (int i = 0; i < alternatives.size(); i++) {
			mvReps[i] = new MarginalValuesRepresentation(alternatives.get(i), functions);
			wvReps[i] = new WVariablesRepresentation(mvReps[i].getCoefficients());
		}

		return wvReps;
	}

	Ranking<Alternative> buildRank(LinearFunction[] functions, Alternative[] alts) {
		Map<Alternative, Double> altsAndUtils = new HashMap<Alternative, Double>();
		for (Alternative alternative : alts) {
			for (LinearFunction function : functions) {
				double value = alternative.getValueOn(function.getCriterion());
				// function.get
			}
		}

		return null;
	}

}
