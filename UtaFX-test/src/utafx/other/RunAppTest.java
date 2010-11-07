package utafx.other;

import java.io.InputStreamReader;
import java.util.Date;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

public class RunAppTest {

	public static void main(String[] args) throws Exception {
		// set up script:
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		ScriptEngineManager manager = new ScriptEngineManager(loader);
		ScriptEngine engine = manager.getEngineByExtension("fx");

		Bindings bindings = engine.createBindings();
		bindings.put("now:java.util.Date", new Date()); // Note-1

		ScriptContext context = new SimpleScriptContext();
		// Bug workaround
		context.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
		context.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		engine.setContext(context);

		InputStreamReader reader = new InputStreamReader(RunAppTest.class.getResourceAsStream("HelloWorld.fx"));
		Object scriptReturnValue = engine.eval(reader);
		System.out.println(scriptReturnValue);
	}
}
