package at.andiwand.library.math;

import java.awt.geom.Rectangle2D;


public class Rectangle {
	
	private Vector2d middle;
	private Vector2d size;
	
	
	
	public Rectangle(Vector2d size) {
		this(size.div(2).negate(), size);
	}
	public Rectangle(Vector2d middle, Vector2d size) {
		if ((size.getX() < 0) || (size.getY() < 0))
			throw new IllegalArgumentException("the size cannot be negative");
		
		this.middle = middle;
		this.size = size;
	}
	public Rectangle(Rectangle rectangle) {
		middle = rectangle.middle;
		size = rectangle.size;
	}
	public Rectangle(Rectangle2D rectangle) {
		middle = new Vector2d(rectangle.getCenterX(), rectangle.getCenterY());
		size = new Vector2d(rectangle.getMaxX() - rectangle.getMinX(), rectangle.getMaxY() - rectangle.getMinY());
	}
	
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{");
		builder.append("position: ");
		builder.append(middle);
		builder.append(", ");
		builder.append("size: ");
		builder.append(size);
		builder.append("}");
		
		return builder.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null) return false;
		
		if (!(obj instanceof Rectangle)) return false;
		Rectangle rectangle = (Rectangle) obj;
		
		return middle.equals(rectangle.middle) &&
			size.equals(rectangle.size);
	}
	@Override
	public int hashCode() {
		return middle.hashCode() * (middle.hashCode() + size.hashCode());
	}
	
	
	
	public Vector2d getMiddle() {
		return middle;
	}
	public Vector2d getSize() {
		return size;
	}
	public double getWidth() {
		return size.x;
	}
	public double getHeight() {
		return size.y;
	}
	
	
	public double left() {
		return middle.x - size.x / 2;
	}
	public double right() {
		return middle.x + size.x / 2;
	}
	public double top() {
		return middle.y - size.y / 2;
	}
	public double bottom() {
		return middle.y + size.y / 2;
	}
	
	public Vector2d leftTop() {
		return middle.sub(size.div(2));
	}
	
	
	public boolean intersection(Vector2d point) {
		return point.sub(middle).abs().lessThanOrEqual(size.div(2)).all();
	}
	public boolean intersection(Rectangle rectangle) {
		Vector2d absDistance = middle.sub(rectangle.middle).abs();
		Vector2d maxDistance = size.add(rectangle.size).div(2);
		
		return absDistance.lessThanOrEqual(maxDistance).all();
	}
	
}