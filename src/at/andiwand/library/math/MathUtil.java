package at.andiwand.library.math;


/**
 * 
 * A simple static class that caches sine and cosine values for quick access.
 * It does also wrap some <code>Math</code> methods to combine them with the
 * <code>Vector</code> and <code>Matrix</code> classes.
 * 
 * @author Andreas Stefl
 * 
 */
public class MathUtil {
	
	/**
	 * The product of a degree value and the constant is the radiant value.
	 */
	public static final double DEG2RAD = Math.PI / 180;
	
	/**
	 * The product of a radiant value and the constant is the degree value.
	 */
	public static final double RAD2DEG = 1 / DEG2RAD;
	
	
	/**
	 * The radiant value of a full circle.
	 */
	public static final double CIRCLE_RAD = 2 * Math.PI;
	
	private static final int SIN_COS_TABLE_ENTRIES = 1000;
	private static final double SIN_COS_TABLE_INDEX_TO_RAD =
		CIRCLE_RAD / SIN_COS_TABLE_ENTRIES;
	private static final double RAD_TO_SIN_COS_TABLE_INDEX =
		1 / SIN_COS_TABLE_INDEX_TO_RAD;
	private static final double[] SIN_TABLE = new double[SIN_COS_TABLE_ENTRIES];
	private static final double[] COS_TABLE = new double[SIN_COS_TABLE_ENTRIES];
	
	
	
	static {
		//TODO: optimize;
		for (int i = 0; i < SIN_COS_TABLE_ENTRIES; i++) {
			SIN_TABLE[i] = Math.sin(i * SIN_COS_TABLE_INDEX_TO_RAD);
			COS_TABLE[i] = Math.cos(i * SIN_COS_TABLE_INDEX_TO_RAD);
		}
	}
	
	
	
	/**
	 * Returns the cached value of the sine for the given radiant value.
	 * 
	 * @param r the radiant value.
	 * @return the cached value of the sine for the given radiant value.
	 */
	public static double sin(double r) {
		r = Math.abs(r % CIRCLE_RAD);
		return SIN_TABLE[(int) (r * RAD_TO_SIN_COS_TABLE_INDEX)];
	}
	
	/**
	 * Returns the cached value of the cosine for the given radiant value.
	 * 
	 * @param r the radiant value.
	 * @return the cached value of the cosine for the given radiant value.
	 */
	public static double cos(double r) {
		r = Math.abs(r % CIRCLE_RAD);
		return COS_TABLE[(int) (r * RAD_TO_SIN_COS_TABLE_INDEX)];
	}
	
	
	/**
	 * Returns the result of
	 * <code>Math.atan2(vector.getY(), vector.getX())</code>.
	 * 
	 * @param vector the x and y value.
	 * @return the result of
	 * <code>Math.atan2(vector.getY(), vector.getX())</code>.
	 */
	public static double atan2(Vector2d vector) {
		return Math.atan2(vector.getY(), vector.getX());
	}
	
	
	
	
	/**
	 * Prevents creating an instance of this class.
	 */
	private MathUtil() {}
	
}