package util;

import java.util.List;


public interface Matrix<E> {
	
	public String toString();
	public boolean equals(Object object);
	public int hashCode();
	
	
	
	public int rows();
	public int columns();
	
	public List<E> getRow(int row);
	public List<E> getColumn(int column);
	
	public E get(int row, int column);
	
	
	public void setRow(int index, List<E> row);
	public void setColumn(int index, List<E> column);
	
	public void set(int row, int column, E element);
	
	
	
	public void addRow();
	public void addRow(List<E> row);
	public void addRow(int row);
	public void addRow(int index, List<E> row);
	public void addColomn();
	public void addColomn(List<E> column);
	public void addColomn(int column);
	public void addColomn(int index, List<E> column);
	
	
	public void removeRow(int row);
	public void removeColumn(int column);
	
}