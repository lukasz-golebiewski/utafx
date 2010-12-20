package utafx.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import utafx.data.pref.jaxb.AltValues;
import utafx.data.pref.jaxb.Alternative;
import utafx.data.pref.jaxb.CriteriaType;
import utafx.data.pref.jaxb.Criterion;
import utafx.data.pref.jaxb.Value;

public class CommonUtilTest {

    @Test
    public void testToStringAlternative() {
	Alternative a = new Alternative();
	a.setId(12);
	a.setName("a1");
	AltValues valueList = new AltValues();
	for (int i = 0; i < 3; i++) {
	    Value valueObject = new Value();
	    valueObject.setValue("v" + i);
	    valueList.getValue().add(valueObject);
	}
	a.setValues(valueList);
	assertEquals(
		"id=\"12\", name=\"a1\", values: [\"v0\", \"v1\", \"v2\"]",
		CommonUtil.toString(a));
    }

    @Test
    public void testGetType() {
	assertNull(CommonUtil.getType(null));
	assertNull(CommonUtil.getType(""));
	assertNull(CommonUtil.getType("   "));
	assertNull(CommonUtil.getType("b"));
	assertNotNull(CommonUtil.getType("c"));
	assertSame(CriteriaType.COST, CommonUtil.getType("c"));
	assertSame(CriteriaType.COST, CommonUtil.getType("c,1"));
	assertSame(CriteriaType.COST, CommonUtil.getType("c, 2"));
	assertSame(CriteriaType.COST, CommonUtil.getType("  c  , 3   "));

	assertSame(CriteriaType.GAIN, CommonUtil.getType("g"));
	assertSame(CriteriaType.GAIN, CommonUtil.getType("g,1"));
	assertSame(CriteriaType.GAIN, CommonUtil.getType("g, 2"));
	assertSame(CriteriaType.GAIN, CommonUtil.getType("  g  , 3   "));
    }

    @Test
    public void testGetSegments() {
	Criterion c = new Criterion();
	assertEquals(2, c.getSegments());
	c.setSegments(3);
	assertEquals(3, c.getSegments());
    }

    @Test
    public void testToStringCriterion() {
	Criterion c = new Criterion();
	c.setName("c1");
	c.setType(CriteriaType.COST);
	c.setId(13);
	assertEquals("id=\"13\", name=\"c1\", type=\"cost\", segments=\"2\"",
		CommonUtil.toString(c));

	c.setType(CriteriaType.GAIN);
	c.setSegments(3);
	assertEquals("id=\"13\", name=\"c1\", type=\"gain\", segments=\"3\"",
		CommonUtil.toString(c));

    }

    @Test
    public void testGetFirstNotEmptyValueIndex() {
	String[] data = null;
	assertEquals(-1, CommonUtil.getFirstNotEmptyValueIndex(data));

	data = new String[10];
	assertEquals(-1, CommonUtil.getFirstNotEmptyValueIndex(data));

	data = new String[] { "", "", "", "" };
	assertEquals(-1, CommonUtil.getFirstNotEmptyValueIndex(data));

	data = new String[] { " ", "   ", "    ", "      " };
	assertEquals(-1, CommonUtil.getFirstNotEmptyValueIndex(data));

	data = new String[] { "a", "b", "c", "d" };
	assertEquals(0, CommonUtil.getFirstNotEmptyValueIndex(data));

	data = new String[] { "", "b", "c", "d" };
	assertEquals(1, CommonUtil.getFirstNotEmptyValueIndex(data));

	data = new String[] { "", " ", "c", "d" };
	assertEquals(2, CommonUtil.getFirstNotEmptyValueIndex(data));

	data = new String[] { "", " ", "  ", "d" };
	assertEquals(3, CommonUtil.getFirstNotEmptyValueIndex(data));

    }

    @Test
    public void testIsNotEmpty() {
	assertTrue(CommonUtil.isNotEmpty("a"));
	assertTrue(CommonUtil.isNotEmpty(" 2"));
	assertTrue(CommonUtil.isNotEmpty("1"));
	assertTrue(CommonUtil.isNotEmpty("                  b , 2"));
    }

    @Test
    public void testIsEmpty() {
	assertTrue(CommonUtil.isEmpty(null));
	assertTrue(CommonUtil.isEmpty(""));
	assertTrue(CommonUtil.isEmpty("  "));
	assertTrue(CommonUtil.isEmpty("				"));
	assertFalse(CommonUtil.isEmpty("				a"));
    }

    @Test
    public void testCreateCriteriaComparator() {
	List<Criterion> list = new ArrayList<Criterion>();
	Criterion c1 = new Criterion();
	Criterion c2 = new Criterion();
	Criterion c3 = new Criterion();
	c1.setId(123);
	c2.setId(1);
	c3.setId(12);
	list.add(c1);
	list.add(c2);
	list.add(c3);
	Collections.sort(list, CommonUtil.createCriteriaComparator());
	int[] expected = { 1, 12, 123 };
	for (int i = 0; i < expected.length; i++) {
	    assertEquals(expected[i], list.get(i).getId());
	}
	Collections.shuffle(list);
	Collections.sort(list, CommonUtil.createCriteriaComparator());
	for (int i = 0; i < expected.length; i++) {
	    assertEquals(expected[i], list.get(i).getId());
	}
    }

}
