package utafx.jemmy;

import javafx.scene.Node;

import org.jemmy.fx.FXAppExecutor;
import org.jemmy.fx.FXRoot;
import org.jemmy.interfaces.Parent;
import org.junit.Test;

public class JemmyTest {

	@Test
	public void testJemmy() throws InterruptedException {
		startApp();
		Thread.sleep(5000);
		// start lookup:
		Parent<Node> parent = FXRoot.ROOT.lookup().as(Parent.class, Node.class);

	}

	private void startApp() {
		new FXAppExecutor("utafx.Main").execute();
	}
}
