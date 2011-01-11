package uta;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import uta.api.IUtaSolver;
import uta.star.UtaStarSolver;

/**
 * 
 * @author LG
 */
public class UtaSolverFactory {

	private static final Logger LOG = Logger.getLogger(UtaSolverFactory.class
			.getName());

	private static final String FOUND_SOLVER = "Found solver: ";

	private static final String CHOOSING_SOLVER = "Will solve using the following solver class: ";

	private String solverJarPath = "";

	public IUtaSolver createSolver() {
		IUtaSolver solver = null;

		try {
			URL[] urls = new URL[1];
			urls[0] = new URL("file://" + solverJarPath);

			ServiceLoader<IUtaSolver> serviceLoader = ServiceLoader.load(
					IUtaSolver.class, new URLClassLoader(urls));

			Iterator<IUtaSolver> loadedServicesIterator = serviceLoader
					.iterator();			

			if (loadedServicesIterator.hasNext()) {
				solver = loadedServicesIterator.next();
				LOG.info(FOUND_SOLVER + solver.getClass().getName());
			}
		} catch (java.security.AccessControlException e) {
			LOG.log(Level.INFO, null, e);
		} catch (MalformedURLException e) {
			LOG.log(Level.SEVERE, null, e);
		} finally {
			if (solver == null) {
				solver = new UtaStarSolver();
			}
		}

		LOG.info(CHOOSING_SOLVER + solver.getClass().getName());
		return solver;
	}

	public void setSolverJarPath(String path) {
		this.solverJarPath = path;
	}
}
