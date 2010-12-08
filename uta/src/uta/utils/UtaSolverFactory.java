package uta.utils;

import java.util.Iterator;
import java.util.ServiceLoader;
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

	private static final String DUMMY_JAR_MISSING = "Couldn't load the default solver - make sure uta-dummy.jar is on the runtime classpath";
	
	public IUtaSolver createSolver() {
		ServiceLoader<IUtaSolver> serviceLoader = ServiceLoader
				.load(IUtaSolver.class);

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
}
