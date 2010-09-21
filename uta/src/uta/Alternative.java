package uta;

import java.util.Arrays;
import java.util.List;

public class Alternative implements Comparable<Alternative> {

	private String name;
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
		int criterionIndex = getIndexOf(criterion);
		return values[criterionIndex];
	}

	@Override
	public int compareTo(Alternative o) {
		return (new Integer(this.hashCode()).compareTo(o.hashCode()));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValues(double[] newValues) {
		this.values = newValues;
	}

	public void setCriteria(Criterion[] criteria) {
		this.criteria = Arrays.asList(criteria);
	}

	public double[] getValues() {
		return values;
	}

	@Override
	public String toString() {
		return String.format("%s %s", name, Arrays.toString(values));
	}

	/**
	 * Can be different instances, but the same values, so checking based on
	 * criterion name (must be unique!)
	 * 
	 * @param criterion
	 * @return position in list of criterion
	 */
	private int getIndexOf(Criterion criterion) {
		int index = criteria.indexOf(criterion);
		if (index == -1) {
			for (Criterion c : criteria) {
				index++;
				if (c.getName().equals(criterion.getName())) {
					return index;
				}
			}
		}
		return index;
	}

	public double getGeneralUtil(LinearFunction[] functions) {
		double util = 0;
		for (LinearFunction function : functions) {
			double value = getValueOn(function.getCriterion());
			util += function.getValueAt(value);
		}
		return util;
	}

}
