package uta;

/**
 * 
 * @author masklin
 * 
 */
public class LinearFunction {

	private double[] characteristicPoints;
	private double[] values;
	private Criterion criterion;

	LinearFunction(Criterion criterion) {
		this.criterion = criterion;
		characteristicPoints = new double[criterion.getNoOfSegments() + 1];
		values = new double[characteristicPoints.length];
		double interval = (criterion.getBestValue() - criterion.getWorstValue()) / criterion.getNoOfSegments();

		characteristicPoints[0] = criterion.getWorstValue();
		for (int i = 1; i < characteristicPoints.length; i++) {
			characteristicPoints[i] = characteristicPoints[i - 1] + interval;
		}
	}

	LinearFunction(double[] characteristicPoints, double[] values, Criterion criterion) {
		this.characteristicPoints = characteristicPoints;
		this.values = values;
		this.criterion = criterion;
	}

	public double[] getValues() {
		return values;
	}

	public double[] getCharacteristicPoints() {
		return characteristicPoints;
	}

	public Criterion getCriterion() {
		return criterion;
	}

	public boolean isIncreasing() {
		return characteristicPoints[characteristicPoints.length - 1] - characteristicPoints[0] > 0;

	}

}
