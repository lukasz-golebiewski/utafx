package uta.constraint;

import java.util.HashMap;
import java.util.Map;
import uta.api.LinearFunction;
import uta.api.Point;

public abstract class AbstractConstraintsManager implements ConstraintsManager {

	protected LinearFunction[] functions;
	protected Map<Point, Constraint> constraints = new HashMap<Point, Constraint>();

	public AbstractConstraintsManager(LinearFunction[] functions) {
		super();
		this.functions = functions;
	}

	public Constraint[] getConstraints() {
		return constraints.values().toArray(new Constraint[constraints.size()]);
	}

	public Constraint getConstraintFor(Point p) {
		return constraints.get(p);
	}

	public abstract void update(LinearFunction function, Point changedPoint);

}