package uta;


public class ReferenceRanking {

	private Action[] actions;

	private double[] ranking;

	public ReferenceRanking(double[] ranking, Action... actions) {
		this.ranking = ranking;
		this.actions = actions;
	}

}
