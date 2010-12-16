package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class AbstractMatrix<E> implements Matrix<E> {
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		int rows = rows();
		int columns = columns();
		String[][] stringTable = new String[rows][columns];
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				E element = get(row, column);
				
				if (element == null) stringTable[row][column] = "null";
				else stringTable[row][column] = element.toString();
			}
		}
		
		int[] maxColumnWidth = new int[columns];
		int[] maxRowHeight = new int[rows];
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				String string = stringTable[row][column];
				int width = StringUtil.width(string);
				int height = StringUtil.height(string);
				
				if (maxColumnWidth[column] < width) maxColumnWidth[column] = width;
				if (maxRowHeight[row] < height) maxRowHeight[row] = height;
			}
		}
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				stringTable[row][column] = StringUtil.center(stringTable[row][column], maxColumnWidth[column], maxRowHeight[row]);
			}
		}
		
		StringBuilder rowSeparatorBuilder = new StringBuilder();
		rowSeparatorBuilder.append("+");
		for (int column = 0; column < columns; column++) {
			rowSeparatorBuilder.append(StringUtil.multiply("-", maxColumnWidth[column]));
			rowSeparatorBuilder.append("+");
		}
		String rowSeparator = rowSeparatorBuilder.toString();
		
		builder.append(rowSeparator);
		for (int row = 0; row < rows; row++) {
			builder.append(StringUtil.NEW_LINE);
			
			for (int height = 0; height < maxRowHeight[row]; height++) {
				builder.append("|");
				
				for (int column = 0; column < columns; column++) {
					builder.append(StringUtil.lines(stringTable[row][column])[height]);
					builder.append("|");
				}
				
				builder.append(StringUtil.NEW_LINE);
			}
			
			builder.append(rowSeparator);
		}
		
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null) return false;
		if (!(obj instanceof AbstractMatrix<?>)) return false;
		
		AbstractMatrix<?> matrix = (AbstractMatrix<?>) obj;
		
		int rows = rows();
		int columns = columns();
		
		if (rows != matrix.rows()) return false;
		if (columns != matrix.columns()) return false;
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				Object object1 = get(row, column);
				Object object2 = matrix.get(row, column);
				
				if (!object1.equals(object2)) return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int rows = rows();
		int columns = columns();
		int result = 0;
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				Object object = get(row, column);
				
				result *= (object.hashCode() + column) * row;
			}
		}
		
		return result;
	}
	
	
	
	
	@Override
	public abstract int rows();
	
	@Override
	public abstract int columns();
	
	
	@Override
	public List<E> getRow(int row) {
		if (row < 0) throw new ArrayIndexOutOfBoundsException(row);
		if (row >= rows()) throw new ArrayIndexOutOfBoundsException(row);
		
		int columns = columns();
		List<E> result = new ArrayList<E>();
		
		for (int column = 0; column < columns; column++) {
			E element = get(row, column);
			
			result.add(element);
		}
		
		return result;
	}
	
	@Override
	public List<E> getColumn(int column) {
		if (column < 0) throw new ArrayIndexOutOfBoundsException(column);
		if (column >= columns()) throw new ArrayIndexOutOfBoundsException(column);
		
		int rows = rows();
		List<E> result = new ArrayList<E>();
		
		for (int row = 0; row < rows; row++) {
			E element = get(row, column);
			
			result.add(element);
		}
		
		return result;
	}
	
	
	@Override
	public abstract E get(int row, int column);
	
	
	
	@Override
	public void setRow(int index, List<E> row) {
		if (index < 0) throw new ArrayIndexOutOfBoundsException(index);
		if (index >= rows()) throw new ArrayIndexOutOfBoundsException(index);
		
		int columns = columns();
		Iterator<E> iterator = row.iterator();
		
		for (int column = 0; column < columns; column++) {
			E element = null;
			
			if (iterator.hasNext()) element = iterator.next();
			
			set(index, column, element);
		}
	}
	
	@Override
	public void setColumn(int index, List<E> column) {
		if (index < 0) throw new ArrayIndexOutOfBoundsException(index);
		if (index >= columns()) throw new ArrayIndexOutOfBoundsException(index);
		
		int rows = rows();
		Iterator<E> iterator = column.iterator();
		
		for (int row = 0; row < rows; row++) {
			E element = null;
			
			if (iterator.hasNext()) element = iterator.next();
			
			set(row, index, element);
		}
	}
	
	
	@Override
	public abstract void set(int row, int column, E element);
	
	
	
	
	@Override
	public abstract void addRow();
	
	@Override
	public void addRow(List<E> row) {
		addRow();
		
		setRow(columns() - 1, row);
	}
	
	@Override
	public void addRow(int row) {
		if (row < 0) throw new ArrayIndexOutOfBoundsException(row);
		if (row - 1 >= rows()) throw new ArrayIndexOutOfBoundsException(row);
		
		addRow();
		
		int rows = rows();
		List<E> tmp;
		
		for (int i = rows - 1; i > row; i--) {
			tmp = getRow(i);
			setRow(i, getRow(i - 1));
			setRow(i - 1, tmp);
		}
	}
	
	@Override
	public void addRow(int index, List<E> row) {
		if (index < 0) throw new ArrayIndexOutOfBoundsException(index);
		if (index - 1 >= rows()) throw new ArrayIndexOutOfBoundsException(index);
		
		addRow();
		
		int rows = rows();
		List<E> tmp;
		
		for (int i = rows - 1; i > index; i--) {
			tmp = getRow(i);
			setRow(i, getRow(i - 1));
			setRow(i - 1, tmp);
		}
		
		setRow(index, row);
	}
	
	@Override
	public abstract void addColomn();
	
	@Override
	public void addColomn(List<E> column) {
		addColomn();
		
		setColumn(rows() - 1, column);
	}
	
	@Override
	public void addColomn(int column) {
		if (column < 0) throw new ArrayIndexOutOfBoundsException(column);
		if (column - 1 >= columns()) throw new ArrayIndexOutOfBoundsException(column);
		
		addColomn();
		
		int columns = columns();
		List<E> tmp;
		
		for (int i = columns - 1; i > column; i--) {
			tmp = getColumn(i);
			setColumn(i, getColumn(i - 1));
			setColumn(i - 1, tmp);
		}
	}
	
	@Override
	public void addColomn(int index, List<E> column) {
		if (index < 0) throw new ArrayIndexOutOfBoundsException(index);
		if (index - 1 >= columns()) throw new ArrayIndexOutOfBoundsException(index);
		
		addColomn();
		
		int columns = columns();
		List<E> tmp;
		
		for (int i = columns - 1; i > index; i--) {
			tmp = getColumn(i);
			setColumn(i, getColumn(i - 1));
			setColumn(i - 1, tmp);
		}
		
		setColumn(index, column);
	}
	
	
	
	@Override
	public abstract void removeRow(int row);
	
	@Override
	public abstract void removeColumn(int column);
	
}