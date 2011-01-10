package utafx.newui.helper;

import java.io.File;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import org.jemmy.control.Wrap;
import org.jemmy.interfaces.Parent;
import org.jemmy.lookup.Lookup;
import org.jemmy.lookup.LookupCriteria;
import org.netbeans.jemmy.operators.JFileChooserOperator;

import utafx.ui.Constants;
import utafx.ui.generic.table.TableUI;

import com.javafx.preview.control.Menu;
import com.javafx.preview.control.PopupMenu;

public class UtaFXHelper {

  public static final double DOUBLE_PRECISION = 0.0000000001;

  public static final String SOLVE = "Solve";

  private static final String NEXT = "Next";

  private static final String DEFAULT_DATA_PATH = "./data";

  private static final String DEFAULT_DATA_XML = "real_data.xml";

  private Wrap<? extends Scene> scene;

  public UtaFXHelper(Wrap<? extends Scene> scene) {
    this.scene = scene;
  }

  public Wrap<? extends TableUI> getFinalRankTable() {
    return getTableByName(Constants.FINAL_RANK_TABLE_ID);
  }

  public Wrap<? extends TableUI> getTableByName(final String name) {
    Wrap<? extends TableUI> tableWrap = scene.as(Parent.class, Node.class).lookup(TableUI.class, new LookupCriteria<TableUI>() {
      @Override
      public boolean check(TableUI table) {
        return table.get$id().equals(name);
      }
    }).wrap();
    return tableWrap;
  }

  public double getDoubleFromTable(TableUI tableUI, int rowIndex, int columnIndex) {
    return Double.parseDouble(getTextFromTable(tableUI, rowIndex, columnIndex));
  }

  public String getTextFromTable(TableUI tableUI, int rowIndex, int columnIndex) {
    return tableUI.get$rows().get(rowIndex).get$cells().get(columnIndex).get$text();
  }

  public void importDefaultData() {
    Wrap<? extends Menu> menuFile = findMenuByText("File").wrap();
    menuFile.mouse().click();
    Lookup<PopupMenu> popupMenu = menuFile.as(Parent.class).lookup(PopupMenu.class);
    popupMenu.wrap().mouse().click();

    final JFileChooserOperator chooserOperator = new JFileChooserOperator();
    chooserOperator.setCurrentDirectory(new File(DEFAULT_DATA_PATH));
    chooserOperator.setSelectedFile(new File(DEFAULT_DATA_XML));
    chooserOperator.approve();
  }

  public void clickNext() {
    clickButtonWithText(NEXT);
  }

  public void clickSolve() {
    clickButtonWithText(SOLVE);
  }

  public void clickButtonWithText(final String buttonText) {
    getButtonWrapByText(buttonText).mouse().click();
  }

  public Wrap<Button> getButtonWrapByText(final String text) {
    return scene.as(Parent.class, Node.class).lookup(Button.class, new LookupCriteria<Button>() {
      public boolean check(Button button) {
        return button.get$text().equals(text);
      }
    }).wrap();
  }

  public Wrap<Button> getButtonWrapByID(final String buttonId) {
    return scene.as(Parent.class, Node.class).lookup(Button.class, new LookupCriteria<Button>() {
      @Override
      public boolean check(Button button) {
        return button.get$id().equals(buttonId);
      }
    }).wrap();
  }

  private Lookup<Menu> findMenuByText(final String text) {
    return scene.as(Parent.class, Node.class).lookup(Menu.class, new LookupCriteria<Menu>() {
      public boolean check(Menu cntrl) {
        return cntrl.get$text().equals(text);
      }
    });
  }
}
