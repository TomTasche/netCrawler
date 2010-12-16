package util;


public class ArrayMatrix<E> extends AbstractMatrix<E> {
	
	private int rows;
	private int columns;
	
	private E[][] matrix;
	
	
	
	
	public ArrayMatrix(int both) {
		this(both, both);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayMatrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		matrix = (E[][]) new Object[rows][columns];
	}
	
	public ArrayMatrix(E[]... matrix) {
		this(matrix.length, matrix[0].length);
		
		for (int row = 0; row < rows; row++) {
			System.arraycopy(matrix[row], 0, this.matrix[row], 0, Math.min(columns, matrix[row].length));
		}
	}
	
	public ArrayMatrix(Matrix<E> matrix) {
		this(matrix.rows(), matrix.columns());
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				set(row, column, matrix.get(row, column));
			}
		}
	}
	
	
	
	
	@Override
	public int rows() {
		return rows;
	}
	
	@Override
	public int columns() {
		return columns;
	}
	
	
	@Override
	public E get(int row, int column) {
		return matrix[row][column];
	}
	
	
	
	@Override
	public void set(int row, int column, E element) {
		matrix[row][column] = element;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void addRow() {
		E[][] newMatrix = (E[][]) new Object[rows + 1][columns];
		
		for (int row = 0; row < rows; row++) {
			newMatrix[row] = matrix[row];
		}
		
		matrix = newMatrix;
		rows++;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addColomn() {
		E[][] newMatrix = (E[][]) new Object[rows][columns + 1];
		
		for (int row = 0; row < rows; row++) {
			System.arraycopy(matrix[row], 0, newMatrix[row], 0, columns);
		}
		
		matrix = newMatrix;
		columns++;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void removeRow(int index) {
		if (index < 0) throw new ArrayIndexOutOfBoundsException(index);
		if (index >= rows()) throw new ArrayIndexOutOfBoundsException(index);
		
		E[][] newMatrix = (E[][]) new Object[rows - 1][columns];
		
		for (int row = 0, j = 0; row < rows; row++) {
			if (row == index) continue;
			
			newMatrix[j++] = matrix[row];
		}
		
		matrix = newMatrix;
		rows--;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void removeColumn(int index) {
		if (index < 0) throw new ArrayIndexOutOfBoundsException(index);
		if (index >= columns()) throw new ArrayIndexOutOfBoundsException(index);
		
		E[][] newMatrix = (E[][]) new Object[rows][columns - 1];
		
		for (int row = 0; row < rows; row++) {
			System.arraycopy(matrix[row], 0, newMatrix[row], 0, index);
			System.arraycopy(matrix[row], index + 1, newMatrix[row], index, columns - index - 1);
		}
		
		matrix = newMatrix;
		columns--;
	}
	
	
	
	
	public static void main(String[] args) {
		Matrix<String> matrix = new ArrayMatrix<String>(3, 3);
		
		for (int row = 0; row < matrix.rows(); row++) {
			for (int column = 0; column < matrix.columns(); column++) {
				matrix.set(row, column, row + "," + column);
			}
		}
		
		matrix.set(2, 2, "asdfasdf\n\nasdfasdf");
		
		System.out.println(matrix);
	}
	
}