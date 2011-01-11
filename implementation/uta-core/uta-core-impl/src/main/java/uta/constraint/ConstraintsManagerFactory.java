package uta.constraint;

import uta.api.Alternative;
import uta.api.LinearFunction;
import uta.api.Ranking;

public class ConstraintsManagerFactory {

	private boolean freezeKendall;

	public ConstraintsManagerFactory(boolean freezeKendall) {
		this.setFreezeKendall(freezeKendall);
	}

	public ConstraintsManager createConstraintsManager(LinearFunction[] functions, Ranking<Alternative> referenceRanking,
			Ranking<Alternative> finalRanking) {
		if (!freezeKendall)
			return new StandardConstraintsManager(functions);

		return new ConstantKendallConstraintsManager(functions, referenceRanking, finalRanking);
	}

	public void setFreezeKendall(boolean freezeKendall) {
		this.freezeKendall = freezeKendall;
	}

}
