package utafx.frameworks.jemmy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javafx.scene.Node;
import javafx.scene.Scene;

import org.jemmy.Point;
import org.jemmy.control.Wrap;
import org.jemmy.fx.FXAppExecutor;
import org.jemmy.fx.FXRoot;
import org.jemmy.interfaces.Parent;
import org.jemmy.lookup.Lookup;
import org.jemmy.lookup.LookupCriteria;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JFileChooserOperator;

import utafx.Main;
import utafx.ui.generic.table.TableUI;

import com.javafx.preview.control.Menu;
import com.javafx.preview.control.PopupMenu;

/**
 * First test class for UtaFX. Used to learn how to use the framework.
 * 
 * @author Lukasz Golebiewski
 * 
 */
public class JemmyTest {

  private static final String DATA_PATH = "./data";
  private static final String REAL_DATA_XML = "real_data.xml";

  @BeforeClass
  public static void beforeClass() {
    // needed to avoid deadlocks:
    JemmyProperties.setCurrentDispatchingModel(JemmyProperties.ROBOT_MODEL_MASK);
  }

  /**
   * Finds the menu bar and clicks first item.
   * 
   * @throws InterruptedException
   * @author Lukasz Golebiewski
   */
  @SuppressWarnings("unchecked")
  @Test
  public void findMenuAndClickFirstItemUsingLookupCriteria() throws InterruptedException {
    startAppAndWait();
    // start lookup:
    Wrap<? extends Scene> scene = FXRoot.ROOT.lookup().wrap();
    Parent<Node> sceneAsParent = scene.as(Parent.class, Node.class);

    Wrap<? extends Menu> menuFile = findMenuByText(sceneAsParent, "File").wrap();
    menuFile.mouse().click();

    // assert that popup is visible
    boolean hover = menuFile.getControl().get$children().get(0).get$hover();
    assertTrue(hover);

    // click popup
    Lookup popupMenu = menuFile.as(Parent.class).lookup(PopupMenu.class);
    Wrap<? extends PopupMenu> popupWrap = popupMenu.wrap();

    popupWrap.mouse().click();

    Thread.sleep(2000);

    importFile();
    Thread.sleep(2000);

    Wrap<? extends Menu> menuTools = findMenuByText(sceneAsParent, "Tools").wrap();
    menuTools.mouse().click();

    // click popup
    popupMenu = menuTools.as(Parent.class).lookup(PopupMenu.class);
    popupWrap = popupMenu.wrap(0);

    // Wrap<? extends PopupMenuSkin> menuItemWrap =
    // sceneAsParent.lookup(PopupMenuSkin.class).wrap();
    // System.out.println(popupWrap.getControl().$items.get(3).get$translateX());
    // System.out.println(popupWrap.getControl().$items.get(3).get$translateY());

    // Lookup<MenuItem> popupItem = sceneAsParent.lookup(MenuItem.class, new
    // LookupCriteria<MenuItem>() {
    //
    // @Override
    // public boolean check(MenuItem arg0) {
    // System.out.println(arg0.get$id());
    // return false;
    // }
    // });
    //
    // popupItem.wrap();

    // No idea how to get out the 'Solve' MenuItem from PopupMenu (which is not
    // it's parent!). Hence this hack:
    // HACKBEGIN:
    Point point = new Point(30, 35);
    popupWrap.mouse().click(0, point);
    // HACKEND

    Thread.sleep(2000);

    Wrap<? extends TableUI> tableWrap = sceneAsParent.lookup(TableUI.class, new LookupCriteria<TableUI>() {
      @Override
      public boolean check(TableUI table) {
        return table.get$id().equals("finalRankTable");
      }
    }).wrap();

    // check whether result table is populated with correct data:
    TableUI tableUI = tableWrap.getControl();
    assertEquals(1d, getUtilityFromTable(tableUI, 0), 0.0000000001);
    assertEquals(0.6666666666667, getUtilityFromTable(tableUI, 1), 0.0000000001);
    assertEquals(0.6666666666667, getUtilityFromTable(tableUI, 2), 0.0000000001);
    assertEquals(0.3333333333333, getUtilityFromTable(tableUI, 3), 0.0000000001);
    assertEquals(0d, getUtilityFromTable(tableUI, 4), 0.0000000001);

  }

  private Lookup<Menu> findMenuByText(Parent<Node> sceneAsParent, final String text) {
    return sceneAsParent.lookup(Menu.class, new LookupCriteria<Menu>() {
      public boolean check(Menu cntrl) {
        return cntrl.get$text().equals(text);
      }
    });
  }

  private double getUtilityFromTable(TableUI tableUI, int rowIndex) {
    return Double.parseDouble(tableUI.get$rows().get(rowIndex).get$cells().get(4).get$text());
  }

  private void importFile() {
    // find the file chooser - since it is actually a swing component wrapped in
    // an fx component, Jemmy API for Swing must be used:
    final JFileChooserOperator chooserOperator = new JFileChooserOperator();
    chooserOperator.setCurrentDirectory(new File(DATA_PATH));
    chooserOperator.setSelectedFile(new File(REAL_DATA_XML));
    chooserOperator.approve();
  }

  private void startAppAndWait() throws InterruptedException {
    new FXAppExecutor(Main.class).execute();
    Thread.sleep(5000);
  }

}
