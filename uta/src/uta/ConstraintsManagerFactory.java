package uta;

public class ConstraintsManagerFactory {

	private class DummyConstraintsManager implements ConstraintsManager {

		public DummyConstraintsManager() {
		}

		@Override
		public void update(LinearFunction function, Point changedPoint) {

		}

		@Override
		public Constraint[] getConstraints() {
			return new Constraint[0];
		}

		@Override
		public Constraint getConstraintFor(Point p) {
			return null;
		}

	}

	private static final DummyConstraintsManager DUMMY_MANAGER;

	static {
		DUMMY_MANAGER = new ConstraintsManagerFactory().new DummyConstraintsManager();
	}

	private boolean freezeKendall;

	private ConstraintsManagerFactory() {

	}

	public ConstraintsManagerFactory(boolean freezeKendall) {
		this.setFreezeKendall(freezeKendall);
	}

	public ConstraintsManager createConstraintsManager(LinearFunction[] functions, Ranking<Alternative> referenceRanking,
			Ranking<Alternative> finalRanking) {
		if (!freezeKendall)
			return new StandardConstraintsManager(functions);
		// TODO return KendallAwareConstraintsManager
		return DUMMY_MANAGER;
	}

	public void setFreezeKendall(boolean freezeKendall) {
		this.freezeKendall = freezeKendall;
	}

}
