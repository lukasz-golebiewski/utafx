package utafx.newui;

import static org.junit.Assert.assertEquals;

import org.jemmy.control.Wrap;
import org.junit.Test;

import utafx.newui.helper.UtaFXHelper;
import utafx.ui.generic.table.TableUI;

public class Solve_DataFromInput_SimplestCase extends BaseUtaFXTest {

  private static final int UTIL_INDEX = 3;

  @Test
  public void test() {
    helper.clickNext();

    helper.getButtonWrap("Add criterion").mouse().click(2);
    helper.getButtonWrap("Add alternative").mouse().click(3);
    helper.getButtonWrap("Add alt to rank").mouse().click();
    helper.clickButton("Accept");
    helper.getButtonWrap("Add alt to rank").mouse().click();
    helper.clickButton("Accept");
    helper.clickSolve();
    checkFinalRank();
  }

  private void checkFinalRank() {
    Wrap<? extends TableUI> tableWrap = helper.getFinalRankTable();

    // check whether result table is populated with correct data:
    TableUI tableUI = tableWrap.getControl();
    assertEquals("Alternative 3", helper.getTextFromTable(tableUI, 0, 0));
    assertEquals("Alternative 1", helper.getTextFromTable(tableUI, 1, 0));
    assertEquals("Alternative 2", helper.getTextFromTable(tableUI, 2, 0));

    assertEquals(1d, helper.getDoubleFromTable(tableUI, 0, UTIL_INDEX), UtaFXHelper.DOUBLE_PRECISION);
    assertEquals(0d, helper.getDoubleFromTable(tableUI, 1, UTIL_INDEX), UtaFXHelper.DOUBLE_PRECISION);
    assertEquals(0d, helper.getDoubleFromTable(tableUI, 2, UTIL_INDEX), UtaFXHelper.DOUBLE_PRECISION);

  }
}
