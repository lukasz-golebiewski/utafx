package uta.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import uta.api.IUtaSolver;

/**
 * 
 * @author LG
 */
public class UtaSolverFactory {

	private static final Logger LOG = Logger.getLogger(UtaSolverFactory.class
			.getName());

	private static final String FOUND_SOLVER = "Found solver: ";

	private static final String CHOOSING_SOLVER = "Will solve using the following solver class: ";

	private static final String DUMMY_JAR_MISSING = "Couldn't load the default solver - make sure uta-default.jar is on the runtime classpath";

	private String solverJarPath = "";

	public IUtaSolver createSolver() {
		URL[] urls = new URL[1];
		try {
			urls[0] = new URL("file://" + solverJarPath);
		} catch (MalformedURLException ex) {
			Logger.getLogger(UtaSolverFactory.class.getName()).log(
					Level.SEVERE, null, ex);
		}

		ServiceLoader<IUtaSolver> serviceLoader = ServiceLoader.load(
				IUtaSolver.class, new URLClassLoader(urls));

		Iterator<IUtaSolver> loadedServicesIterator = serviceLoader.iterator();
		if (!loadedServicesIterator.hasNext()) {
			throw new RuntimeException(DUMMY_JAR_MISSING);
		}

		IUtaSolver solver = null;
		while (loadedServicesIterator.hasNext()) {
			solver = loadedServicesIterator.next();
			LOG.info(FOUND_SOLVER + solver.getClass().getName());
		}
		LOG.info(CHOOSING_SOLVER + solver.getClass().getName());
		return solver;
	}

	public void setSolverJarPath(String path) {
		this.solverJarPath = path;
	}
}
