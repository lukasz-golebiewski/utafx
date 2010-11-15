package utafx.frameworks.jemmy;

import static org.junit.Assert.assertTrue;
import javafx.scene.Node;
import javafx.scene.Scene;

import org.jemmy.control.Wrap;
import org.jemmy.fx.FXAppExecutor;
import org.jemmy.fx.FXRoot;
import org.jemmy.interfaces.Parent;
import org.jemmy.lookup.Lookup;
import org.jemmy.lookup.LookupCriteria;
import org.junit.Test;

import com.javafx.preview.control.Menu;
import com.javafx.preview.control.PopupMenu;

/**
 * First test class for UtaFX. Used to learn how to use the framework.
 * 
 * @author Lukasz Golebiewski
 * 
 */
public class JemmyTest {

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

    Wrap<? extends Menu> menu = sceneAsParent.lookup(Menu.class, new LookupCriteria<Menu>() {
      public boolean check(Menu cntrl) {
        return cntrl.get$visible();
      }
    }).wrap();

    menu.mouse().click();

    // assert that popup is visible
    boolean hover = menu.getControl().get$children().get(0).get$hover();
    assertTrue(hover);

    // click popup
    Lookup popupMenu = menu.as(Parent.class).lookup(PopupMenu.class);
    Wrap<? extends PopupMenu> wrap = popupMenu.wrap();

    wrap.mouse().click();

    Thread.sleep(2000);

    // Lookup<SwingScrollableComponent> fileChooser =
    // sceneAsParent.lookup(javafx.ext.swing.SwingScrollableComponent.class);
    // fileChooser.as(Parent.class, Node.class).lookup();
    // Lookup<Scene> newScene = FXRoot.ROOT.lookup();

    FXRoot.ROOT.lookup(new LookupCriteria<Scene>() {
      @Override
      public boolean check(Scene arg0) {
        return false;
      }
    }).as(Parent.class).lookup(javafx.ext.swing.SwingScrollableComponent.class);

    System.out.println();
  }

  private void startAppAndWait() throws InterruptedException {
    new FXAppExecutor("utafx.Main").execute();
    Thread.sleep(5000);
  }

}
