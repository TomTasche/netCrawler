package at.andiwand.library.math;


public class MathUtil {
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public static final double DEG2RAD = Math.PI / 180;
	public static final double RAD2DEG = 1 / DEG2RAD;
	
	public static final double MAX_RAD = 2 * Math.PI;
	
	private static final int SIN_COS_TABLE_ENTRIES = 1000;
	private static final double SIN_COS_TABLE_INDEX_TO_RAD = MAX_RAD / SIN_COS_TABLE_ENTRIES;
	private static final double RAD_TO_SIN_COS_TABLE_INDEX = 1 / SIN_COS_TABLE_INDEX_TO_RAD;
	private static final double[] SIN_TABLE = new double[SIN_COS_TABLE_ENTRIES];
	private static final double[] COS_TABLE = new double[SIN_COS_TABLE_ENTRIES];
	
	
	static {
		// TODO optimize;
		for (int i = 0; i < SIN_COS_TABLE_ENTRIES; i++) {
			SIN_TABLE[i] = Math.sin(i * SIN_COS_TABLE_INDEX_TO_RAD);
			COS_TABLE[i] = Math.cos(i * SIN_COS_TABLE_INDEX_TO_RAD);
		}
	}
	
	
	private MathUtil() {}
	
	
	public static double sin(double r) {
		r = Math.abs(r % MAX_RAD);
		return SIN_TABLE[(int) (r * RAD_TO_SIN_COS_TABLE_INDEX)];
	}
	public static double cos(double r) {
		r = Math.abs(r % MAX_RAD);
		return COS_TABLE[(int) (r * RAD_TO_SIN_COS_TABLE_INDEX)];
	}
	
	public static double atan2(Vector2d vector) {
		return Math.atan2(vector.getY(), vector.getX());
	}
	
}