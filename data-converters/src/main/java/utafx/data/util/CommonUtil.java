package utafx.data.util;

import utafx.data.pref.jaxb.Alternative;
import utafx.data.pref.jaxb.CriteriaType;
import utafx.data.pref.jaxb.Criterion;
import utafx.data.pref.jaxb.Value;

public class CommonUtil {
    public static String toString(Alternative a) {
	StringBuilder sb = new StringBuilder();
	sb.append(a.getName());
	sb.append(" - ");
	for (Value v : a.getValues().getValue()) {
	    sb.append(v.getValue());
	    sb.append(", ");
	}
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
	str.append("id=");
	str.append(criterion.getId());
	str.append(" ");
	str.append("name=");
	str.append(criterion.getName());
	str.append(" ");
	str.append("type=");
	str.append(criterion.getType().toString());
	str.append(" ");
	str.append("segments=");
	str.append(criterion.getSegments());
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
    
}
