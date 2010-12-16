package utafx.newui;

import javafx.scene.Scene;

import org.jemmy.control.Wrap;
import org.jemmy.fx.FXAppExecutor;
import org.jemmy.fx.FXRoot;
import org.junit.After;
import org.junit.BeforeClass;
import org.netbeans.jemmy.JemmyProperties;

import utafx.newui.helper.UtaFXHelper;

/**
 * All UtaFX functional test classes should extend this class.
 * 
 * @author Lukasz Golebiewski
 * 
 */
public class BaseUtaFXTest {

  private static final int STARTUP_WAIT_TIME = 5000;

  protected Wrap<? extends Scene> scene;

  protected UtaFXHelper helper;

  @BeforeClass
  public static void beforeClass() {
    // needed to avoid deadlocks:
    JemmyProperties.setCurrentDispatchingModel(JemmyProperties.ROBOT_MODEL_MASK);
  }

  public BaseUtaFXTest() {
    super();
    startAppAndWait();
    scene = FXRoot.ROOT.lookup().wrap();
    helper = new UtaFXHelper(scene);
  }

  @After
  public void after() {
    // System.exit(0);
    // scene.getControl().get$stage().close();
    if (scene != null)
      scene.getControl().get$stage().set$visible(false);
  }

  private void startAppAndWait() {
    new FXAppExecutor(MainScene.class).execute();
    try {
      Thread.sleep(STARTUP_WAIT_TIME);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}