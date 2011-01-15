package math;

import java.awt.geom.Rectangle2D;


public class Rectangle {
	
	private Vector2d position;
	private Vector2d size;
	
	
	
	public Rectangle(Vector2d size) {
		this(size.div(2).negate(), size);
	}
	
	public Rectangle(Vector2d position, Vector2d size) {
		if ((size.getX() < 0) || (size.getY() < 0))
			throw new IllegalArgumentException("the size cannot be negative");
		
		this.position = position;
		this.size = size;
	}
	
	public Rectangle(Rectangle rectangle) {
		position = rectangle.position;
		size = rectangle.size;
	}
	
	public Rectangle(Rectangle2D rectangle) {
		position = new Vector2d(rectangle.getCenterX(), rectangle.getCenterY());
		size = new Vector2d(rectangle.getMaxX() - rectangle.getMinX(), rectangle.getMaxY() - rectangle.getMinY());
	}
	
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{");
		builder.append("position: ");
		builder.append(position);
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
		
		return position.equals(rectangle.position) &&
			size.equals(rectangle.size);
	}
	
	@Override
	public int hashCode() {
		return position.hashCode() * (position.hashCode() + size.hashCode());
	}
	
	
	
	public Vector2d getPosition() {
		return position;
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
		return position.x - size.x / 2;
	}
	
	public double right() {
		return position.x + size.x / 2;
	}
	
	public double top() {
		return position.y - size.y / 2;
	}
	
	public double bottom() {
		return position.y + size.y / 2;
	}
	
	public Vector2d leftTop() {
		return position.sub(size.div(2));
	}
	
	
	
	public boolean intersection(Vector2d point) {
		return point.sub(position).abs().lessThanOrEqual(size.div(2)).all();
	}
	
	public boolean intersection(Rectangle rectangle) {
		Vector2d absDistance = position.sub(rectangle.position).abs();
		Vector2d maxDistance = size.add(rectangle.size).div(2);
		
		return absDistance.lessThanOrEqual(maxDistance).all();
	}
	
}