package utafx.data.util;

import java.util.Comparator;

import utafx.data.pref.jaxb.Alternative;
import utafx.data.pref.jaxb.CriteriaType;
import utafx.data.pref.jaxb.Criterion;
import utafx.data.pref.jaxb.Value;

public class CommonUtil {
    public static String toString(Alternative a) {
	StringBuilder sb = new StringBuilder();
	sb.append("id=\"");
	sb.append(a.getId());
	sb.append("\", ");
	sb.append("name=\"");
	sb.append(a.getName());
	sb.append("\", values: [");
	int i = 0;
	for (Value v : a.getValues().getValue()) {
	    sb.append("\"");
	    sb.append(v.getValue());
	    sb.append("\"");
	    if (i < a.getValues().getValue().size()-1) {
		sb.append(", ");
	    }
	    i++;
	}
	sb.append("]");
	return sb.toString();
    }

    public static CriteriaType getType(String cellValue) {
	if (cellValue != null) {
	    cellValue = cellValue.trim().toLowerCase();
	    if (cellValue.startsWith("g")) {
		return CriteriaType.GAIN;
	    } else if (cellValue.startsWith("c")) {
		return CriteriaType.COST;
	    }
	}
	return null;
    }

    public static int getSegments(String combinedValue) {
	String[] parts = combinedValue.split("\\s*,\\s*");
	if (parts.length == 1) {
	    // return default segments value
	    return 2;
	} else if (parts.length > 1) {
	    return Integer.parseInt(parts[1]);
	} else {
	    throw new NumberFormatException(
		    String.format(
			    "Argument %s must be in format: [c|g](, <noOfSegments>) (E.g. \"c, 3\")",
			    combinedValue));
	}

    }

    public static String toString(Criterion criterion) {
	StringBuilder str = new StringBuilder();
	str.append("id=\"");
	str.append(criterion.getId());
	str.append("\", ");
	str.append("name=\"");
	str.append(criterion.getName());
	str.append("\", ");
	str.append("type=\"");
	str.append(criterion.getType().toString().toLowerCase());
	str.append("\", ");
	str.append("segments=\"");
	str.append(criterion.getSegments());
	str.append("\"");
	return str.toString();
    }

    public static int getFirstNotEmptyValueIndex(String[] data) {
	if (data != null) {
	    for (int i = 0; i < data.length; i++) {
		if (isNotEmpty(data[i])) {
		    return i;
		}
	    }
	}
	return -1;
    }

    public static boolean isNotEmpty(String string) {
	return string != null && !string.trim().isEmpty();
    }

    public static boolean isEmpty(String string) {
	return string == null || string.trim().isEmpty();
    }

    public static Comparator<? super Criterion> createCriteriaComparator() {
	return new Comparator<Criterion>() {
	    public int compare(Criterion c1, Criterion c2) {
		return Integer.valueOf(c1.getId()).compareTo(
			Integer.valueOf(c2.getId()));
	    }
	};
    }

}
