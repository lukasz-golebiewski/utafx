package uta;

import java.util.Arrays;
import java.util.List;

public class Alternative implements Comparable<Alternative> {

	private List<Criterion> criteria;
	private double[] values;

	public Alternative() {

	}

	public Alternative(double[] values, Criterion... criteria) {
		this.criteria = Arrays.asList(criteria);
		this.values = values;
	}

	public Alternative(double[] values, List<Criterion> criteria) {
		this.criteria = criteria;
		this.values = values;
	}

	public double getValueOn(Criterion criterion) {
		int criterionIndex = criteria.indexOf(criterion);
		return values[criterionIndex];
	}

	@Override
	public int compareTo(Alternative o) {
		return this.toString().compareTo(o.toString());
	}
}
