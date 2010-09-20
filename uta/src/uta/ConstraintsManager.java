package uta;

/**
 * Classes implementing this interface know how to manage Constraints defined
 * for all function's characteristic points.
 * 
 * @author masklin
 * 
 */
public interface ConstraintsManager {

	/**
	 * This method should be called whenever some point belonging to specified
	 * function has changed it's value - possibly by user interaction.
	 * 
	 * @param function
	 *            - LinearFunction to which the changed point belongs
	 * @param changedPoint
	 *            - point which's changed it's Y value
	 */
	void update(LinearFunction function, Point changedPoint);

	/**
	 * 
	 * @return - all Constraints computed by this manager
	 */
	Constraint[] getConstraints();

	/**
	 * 
	 * @param p
	 *            - a Point
	 * 
	 * @return - Constraint for point p
	 */
	Constraint getConstraintFor(Point p);

}