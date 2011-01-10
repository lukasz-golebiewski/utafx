package utafx.newui;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utafx.newui.helper.UtaFXHelper;
import utafx.ui.Constants;

public class IncorrectData_EmptyModel extends BaseUtaFXTest {

  @Test
  public void test() {
    helper.clickNext();
    helper.clickSolve();

    // assert that "solve" button is still visible:
    assertTrue(helper.getButtonWrapByText(UtaFXHelper.SOLVE).getControl().$visible);
    // assert that criteria table is also still visible:
    assertTrue(helper.getTableByName(Constants.CRITERIA_TABLE_ID).getControl().$visible);
  }

}
