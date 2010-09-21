package uta;

public class ConstantKendallConstraintsManager extends AbstractConstraintsManager {

	private Ranking<Alternative> referenceRank;
	private Ranking<Alternative> finalRank;
	private final double currentKendall;

	public ConstantKendallConstraintsManager(LinearFunction[] functions, Ranking<Alternative> referenceRank, Ranking<Alternative> finalRank) {
		super(functions);
		this.referenceRank = referenceRank;
		this.finalRank = finalRank;
		this.currentKendall = new RankingUtils().getKendallsCoefficient(referenceRank, finalRank);
	}

	@Override
	public void update(LinearFunction function, Point changedPoint) {
		// TODO Auto-generated method stub

	}

}
