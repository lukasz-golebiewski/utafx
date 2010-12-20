package uta.dummy;

import java.util.List;

import uta.api.Alternative;
import uta.api.Criterion;
import uta.api.IUtaSolver;
import uta.api.LinearFunction;
import uta.api.Point;
import uta.api.Ranking;

/**
 * Dummy Uta Solver implementation. Returns zero value functions, except the
 * first one (for first Criterion) which evaluates to 1 at it's best point.
 * 
 * @author Lukasz Golebiewski
 * 
 */
public class DummyUtaSolver implements IUtaSolver {

  @Override
  public LinearFunction[] solve(Ranking<Alternative> ranking, List<Criterion> criteria, List<Alternative> alternatives) {
    return this.solve(ranking, criteria.toArray(new Criterion[0]), alternatives.toArray(new Alternative[0]));
  }

  @Override
  public LinearFunction[] solve(Ranking<Alternative> ranking, Criterion[] criteria, Alternative[] alternatives) {
    LinearFunction[] result = new LinearFunction[criteria.length];

    for (int i = 0; i < criteria.length; i++) {
      result[i] = new LinearFunction(criteria[i]);
      if (i == 0)
        result[i].setPointAt(result[i].getBestPoint().getX(), new Point(result[i].getBestPoint().getX(), 1));
    }

    return result;
  }

}
