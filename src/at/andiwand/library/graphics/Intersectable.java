package at.andiwand.library.graphics;

import java.awt.Point;


/**
 * 
 * A simple interface that should be invoked by a <code>Intersectable</object>
 * object.
 * 
 * @author Andreas Stefl
 * 
 */
public interface Intersectable {
	
	/**
	 * Tests an intersection of the object with the given <code>point</code>.
	 * 
	 * @param point the possible intersection point.
	 * @return <code>true</code> if the point intersects the object.
	 */
	public boolean intersects(Point point);
	
}