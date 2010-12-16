package utafx.newui;

import static org.junit.Assert.assertEquals;

import org.jemmy.control.Wrap;
import org.junit.Test;

import utafx.newui.helper.UtaFXHelper;
import utafx.ui.generic.table.TableUI;

/**
 * This test imports default data and checks whether table with final rank
 * contains correct data.
 * 
 * @author Lukasz Golebiewski
 * 
 */
public class Solve_ImportedDefaultData extends BaseUtaFXTest {

  private static final int UTIL_INDEX = 4;

  @Test
  public void testDataImport() {
    helper.clickNext();
    helper.importDefaultData();
    helper.clickSolve();
    checkFinalRank();
  }

  public void checkFinalRank() {
    Wrap<? extends TableUI> tableWrap = helper.getFinalRankTable();

    // check whether result table is populated with correct data:
    TableUI tableUI = tableWrap.getControl();
    assertEquals(1d, helper.getDoubleFromTable(tableUI, 0, UTIL_INDEX), UtaFXHelper.DOUBLE_PRECISION);
    assertEquals(0.6666666666667, helper.getDoubleFromTable(tableUI, 1, UTIL_INDEX), UtaFXHelper.DOUBLE_PRECISION);
    assertEquals(0.6666666666667, helper.getDoubleFromTable(tableUI, 2, UTIL_INDEX), UtaFXHelper.DOUBLE_PRECISION);
    assertEquals(0.3333333333333, helper.getDoubleFromTable(tableUI, 3, UTIL_INDEX), UtaFXHelper.DOUBLE_PRECISION);
    assertEquals(0d, helper.getDoubleFromTable(tableUI, 4, UTIL_INDEX), UtaFXHelper.DOUBLE_PRECISION);
  }

}
