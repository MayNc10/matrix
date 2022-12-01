package matrix;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


/**
 * A class representing a 2d matrix
 */
public class Matrix<T extends Num> implements Cloneable {
    // Store the backing array, as well as size information
    private T[][] inner;
    private int rowNum; 
    private int colNum;
    // Store a constructor so that we can make more Ts on the fly
    private Constructor<T> maker;

    /**
     * Creates an instance of the Matrix class.
     * This constructor initializes every value to 0. 
     * Matrix Constructor.
     * 
     * @param rowNum the number of rows of the new matrix
     * @param colNum the number of columns of the new matrix
     * @param clazz the class of T
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Matrix(int rowNum, int colNum, Class<T> clazz) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        maker = clazz.getConstructor();
        inner = (T[][]) (maker.newInstance()).new2DArray(rowNum, colNum);
        this.rowNum = rowNum;
        this.colNum = colNum;
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                inner[i][j].makeThisZero();
            }
        }
        
    }
    /**
     * Creates an instance of the Matrix class.
     * This constructor initializes every value to a given default value. 
     * Matrix Constructor.
     * 
     * @param initVal the initial value for every item in the matrix
     * @param rowNum the number of rows of the new matrix
     * @param colNum the number of columns of the new matrix
     * @param clazz the class of T
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Matrix(T initVal, int rowNum, int colNum, Class<T> clazz) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException  {
        maker = clazz.getConstructor();
        inner = (T[][]) (maker.newInstance()).new2DArray(rowNum, colNum);
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                inner[i][j] = initVal;
            }
        }
        this.rowNum = rowNum;
        this.colNum = colNum;
    }
    /**
     * Creates an instance of the Matrix class.
     * This constructor creates a matrix based on a given initial array
     * Matrix Constructor.
     * 
     * @param initArr the array of initial values for the matrix
     * @param clazz the class of T
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Matrix(T[][] initArr, Class<T> clazz) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        this.maker = clazz.getConstructor();
        inner = initArr.clone();
        rowNum = inner.length;
        colNum = inner[0].length;
        if (colNum != rowNum) {
            inner = null;
            rowNum = 0;
            colNum = 0;
            return;
        }
        for (T[] arr : inner) {
            if (colNum != arr.length) {
                inner = null;
                rowNum = 0;
                colNum = 0;
                return;
            }
        }
    }
    /**
     * Creates an instance of the Matrix class.
     * This constructor creates a matrix based on a given initial array
     * Matrix Constructor.
     * 
     * @param initArr the array of initial values for the matrix
     * @param maker the constructor object representing T's default constructor
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Matrix(T[][] initArr, Constructor<T> maker) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        this.maker = maker;
        inner = initArr.clone();
        rowNum = inner.length;
        colNum = inner[0].length;
        if (colNum != rowNum) {
            inner = null;
            rowNum = 0;
            colNum = 0;
            return;
        }
        for (T[] arr : inner) {
            if (colNum != arr.length) {
                inner = null;
                rowNum = 0;
                colNum = 0;
                return;
            }
        }
    }
    /**
     * Creates the identity matrix for a given size.
     * An identity matrix is a square matrix with 1s down the diagonal and 0s everywhere else.
     * 
     * @param <T> the type of the elements of the matrix
     * @param rowNum the number of rows (and therefore the number of columns as well)
     * @param clazz the class object of T
     * @return a new Matrix object representing an identity matrix
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T extends Num> Matrix<T> identity(int rowNum, Class<T> clazz)  throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        T[][] arr = (T[][]) (clazz.getConstructor().newInstance()).new2DArray(rowNum, rowNum);
        for (int i = 0; i < rowNum; i++) {
            arr[i][i].makeThisOne();
        }

        return new Matrix<T>(arr, clazz);
    }
    /**
     * Computes the hashcode for this Matrix
     * @return an <code>int</code> representing the hashcode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(inner);
        return result;
    }
    /**
     * Checks if two Matrices are equal
     * @return <code>true</code> if the two matrices have the same parameterized type and matrix layout, 
     *         <code>false</code> otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Matrix otherGeneric = (Matrix) obj;
        if (!maker.equals(otherGeneric.maker))
            return false;
        Matrix<T> other = (Matrix<T>) otherGeneric;
        if (rowNum != other.rowNum || colNum != other.colNum) 
            return false;
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                if (!inner[row][col].equals(other.inner[row][col]))
                    return false;
            }
        }
        return true;
    }
    /**
     * Clones a matrix
     * 
     * @return a new deep copy of the matrix as an <code>Object</code>
     */
    @Override
    public Object clone() {
        try {
            return (Object) new Matrix<T>(inner, maker);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString() + " when attempting to clone Matrix");
            return null;
        }
    }
    /**
     * Gets a row of the matrix.
     * The returned row is a shallow copy. 
     * @param row the row number to access
     * @return <code>null</code> if the row is outside the matrix bounds, 
     *         otherwise the row with the specified number
     */
    public T[] getRow(int row) {
        if (row < 0 || row > inner.length) {
            return null;
        } else {
            return inner[row];
        }
    }
    /**
     * Gets a value from a matrix (shallow copy).
     * 
     * @param row the row of the value
     * @param col the column of the value
     * @return <code>null</code> if the row or column are out of bounds, 
     *         otherwise the value at the specified row and column
     */
    public T getVal(int row, int col) {
        T[] rowNum = getRow(row);
        if (rowNum == null) {
            return null;
        }
        if (col < 0 || col > rowNum.length) {
            return null;
        }
        return rowNum[col];
    }
    /**
     * Gets the string representation of a matrix.
     * 
     * @return a <code>String</code> representing the matrix
     */
    @Override
    public String toString() {
        String s = "";
        for (T[] arr : inner) {
            s += "[ " + Arrays.toString(arr) + " ]\n";
        }
        return s;
    }
    /**
     * Computes the determinant of a matrix. 
     * 
     * @return <code>null</code> if the matrix is non-square or zero-sized, 
     *         otherwise the determinant of the matrix as a T
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public T det() throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        // Check the determinant is defined for this matrix
        if (rowNum != colNum) {
            return null;
        }
        else if (rowNum == 0) {
            return null;
        }
        // The determinant of a 1x1 matrix is the only number in the matrix
        else if (rowNum == 1) {
            return inner[0][0];
        }
        // If the matrix is 2x2, it can be expressed as [[a b] [c d]], and the detdrminant is ad - bc
        else if (rowNum == 2) {
            return (T) inner[0][0].mul(inner[1][1]).sub(inner[1][0].mul(inner[0][1]));
        }
        // For an nxn matrix, a simple (although slow) way to compute the determinant is Laplace expansion.
        // This is simply the sum of cofacor(1, j) * mat[1][j] along the any row (1-indexed).
        // For this code I chose the first row because it's very simple. 
        else {
            // Initialize the determinant.
            T det = (T) inner[0][0].clone();
            det.makeThisZero();
            for (int j = 0; j < colNum; j++) { 
                // Set up the scalar
                T scalar = inner[0][j];
                if (j % 2 == 1) {
                    scalar.invertThis();
                }
                // Computer cofactor 
                det.addAssign(scalar.mul(minor(1, j)));
            }
            return det;
        }
    }
    /**
     * Swaps two rows. 
     * If the row indexes are out of bounds, this method does nothing.
     * @param r1 the first row index to swap
     * @param r2 the second row index to swap
     */
    public void swapRows(int r1, int r2) {
        if (r1 > rowNum || r1 < 0 || r2 > rowNum || r2 < 0) {
            return;
        }
        else if (r1 == r2) {
            return;
        } 
        else if (r2 < r1) {
            int temp = r1;
            r1 = r2;
            r2 = temp;
        }
        T[] temp_row = inner[r1].clone();
        inner[r1] = inner[r2];
        inner[r2] = temp_row;

    }
    /**
     * Multiplies a row by a scalar.
     * If the row index is out of bounds, this method does nothing.
     * @param r the row index to multiply
     * @param m the value to scale the row by
     */
    public void mul_row(int r, T m) {
        if (rowNum < 0 || this.rowNum > r) {
            return;
        } 
        for (int i = 0; i < inner[r].length; i++) {
            inner[r][i].mulAssign(m);
        }
        return;
    }
    /**
     * Divides a row by a scalar.
     * If the row index is out of bounds, this method does nothing.
     * @param r the row index to divide
     * @param d the value to divide the row by
     */
    public void div_row(int r, T d) {
        if (rowNum < 0 || this.rowNum > r) {
            return;
        } 
        for (int i = 0; i < inner[r].length; i++) {
            inner[r][i].mulAssign(d);
        }
        return;
    }
    /**
     * Adds the elements of two rows together.
     * If r1 is out of bounds, this method does nothing.
     * 
     * @param r1 the index of the first row
     * @param r2 a row to add to the indexed row
     */
    public void add_row_by_ref(int r1, T[] r2) {
        if (r1 < 0 || r1 > rowNum) {
            return;
        } 
        for (int i = 0; i < inner[r1].length; i++) {
            inner[r1][i].addAssign(r2[i]);
        }
        return;
    }
    /**
     * Subtracts the elements of one row from elements of another
     * If r1 is out of bounds, this method does nothing.
     * 
     * @param r1 the index of the first row
     * @param r2 a row to subtract from the first row
     */
    public void sub_row_by_ref(int r1, T[] r2) {
        if (r1 < 0 || r1 > rowNum) {
            return;
        } 
        for (int i = 0; i < inner[r1].length; i++) {
            inner[r1][i].subAssign(r2[i]); 
        }
        return;
    }
    /**
     * Converts a matrix to row-reduced eschelon form. 
     * This is useful for solving linear equations.
     */
    public void to_rref() {
        // I'm gonna be completely honest, I just got this code from wikipedia. 
        // I don't really understand how it works but it does. 
        // https://en.wikipedia.org/wiki/Row_echelon_form#Pseudocode_for_reduced_row_echelon_form

        int lead = 0;
        for (int rowNum = 0; rowNum < rowNum; rowNum++) {
            if (lead > colNum) {
                return;
            }
            int i = rowNum;
            while (inner[i][lead].isZero()) {
                i++;
                if (i == rowNum) {
                    i = rowNum;
                    lead++;
                    if (lead == colNum) {
                        return;
                    }
                }
            }
            if (i != rowNum) {
                swapRows(i, rowNum);
            }
            div_row(rowNum, getVal(rowNum, lead));
            for (int j = 0; j < rowNum; j++) {
                if (j != rowNum) {
                    T[] new_r2 = getRow(rowNum).clone();
                    for (int idx = 0; idx < new_r2.length; idx++) {
                        new_r2[i].mulAssign(getVal(j, lead));
                    }
                    sub_row_by_ref(rowNum, new_r2);
                }
            }
        }
    }
    /**
     * Computes the trace of a matrix.
     * The trace is the sum of elements down the main diagonal.
     * If the trace is not defined for the matrix (i.e. it's zero-sized or non-square), this method returns null.
     * 
     * @return The trace of the matrix, or null if the trace is not well-defined
     */
    public T trace() {
        if (rowNum != colNum) {
            return null;
        }
        if (rowNum == 0) {
            return null;
        }
        T init = (T) inner[0][0].clone();
        for (int row = 1; row < rowNum; row++) {
            for (int col = 1; col < colNum; col++) {
                init.addAssign(inner[row][col]);
            }
        }
        return init;
    }
    /**
     * Computes the minor(i,j) for a matrix. 
     * The minor is the determinant of a smaller matrix created by cutting out the ith row and the jth column
     * @param i The row to cut out
     * @param j The column to cut out
     * @return The minor(i,j), or <code>null</code> if the matrix is non-square
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public T minor(int i, int j) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        if (rowNum != colNum) {
            return null;
        }
        T[][] new_arr = (T[][]) (maker.newInstance()).new2DArray(rowNum - 1, rowNum - 1);
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < rowNum; col++) {
                if (row == i || col == j) {
                    continue;
                }
                int row_idx = row;
                if (row > i) {
                    row_idx--;
                }
                int col_idx = col;
                if (col > j) {
                    col_idx--;
                }
                new_arr[row_idx][col_idx].assign(inner[row][col]);
            }
        }
        Matrix<T> new_mat = new Matrix<T>(new_arr, maker);
        return new_mat.det();
    }
    /**
     * Computes the cofactor matrix of a matrix.
     * This is just the process of computing the cofactor for each element and creating a new matrix.
     * 
     * @return the matrix of cofactors, or <code>null</code> if the matrix is not square
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Matrix<T> cofactor() throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        if (rowNum != colNum) {
            return null;
        }
        if (rowNum == 0) {
            return new Matrix<T>((T[][]) (maker.newInstance()).new2DArray(0, 0), maker);
        }
        T[][] newInner = (T[][]) (maker.newInstance()).new2DArray(rowNum, colNum);
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                T scalar = inner[row][col];
                if ((row + col) % 2 == 1) {
                    scalar.invertThis();
                }
                newInner[row][col].assign(minor(row, col) );
            }
        }
        return new Matrix<>(newInner, maker);
    }
    /**
     * Converts a matrix into its transposed form. 
     * The transpose of a matrix can be visualized as flipping the matrix along its main diagonal.
     * Note that the transpose is defined even for non-square matrices, and will result in a matrix with different dimensions.
     * 
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void transposeThis() throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        T[][] newInner = (T[][]) (maker.newInstance()).new2DArray(colNum, rowNum);
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                newInner[col][row].assign(inner[row][col]);
            }
        }
        inner = newInner;
        int temp = rowNum;
        rowNum = colNum;
        colNum = temp;
    }
    /**
     * Converts a matrix into its inverse. 
     * If inversion is not well-defined for the matrix, this method will do nothing.
     * The inverse is calculated as scaling the cofactor matrix by the inverse of the determinant.
     * 
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void invertThis() throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        if (rowNum != colNum) {
            return;
        }
        if (rowNum == 0) {
            return;
        }
        Matrix<T> newThis = cofactor(); newThis.transposeThis();
        T scalar = det();
        scalar.invertThis();
        newThis.scaleThis(scalar);
        inner = newThis.inner;
    }
    /**
     * Adds one matrix to another and returns the result.\
     * 
     * @param other the matrix to add
     * @return the result of adding the two matrices, or <code>null</code> if matrix addition between them is not well-defined
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Matrix<T> add(Matrix<T> other) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        if (rowNum != other.rowNum || colNum != other.colNum) {
            return null;
        }
        T[][] newArr = (T[][]) (maker.newInstance()).new2DArray(rowNum, colNum);
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                newArr[row][col] = (T) inner[row][col].add(other.inner[row][col]);
            }
        }
        return new Matrix<T>(newArr, maker);
    }
    /**
     * Subtracts one matrix from another and returns the result.\
     * 
     * @param other the matrix to subtract
     * @return the result of subtract one matrix from the other, or <code>null</code> if matrix subtraction between them is not well-defined
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Matrix<T> sub(Matrix<T> other) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        if (rowNum != other.rowNum || colNum != other.colNum) {
            return null;
        }
        T[][] newArr = (T[][]) (maker.newInstance()).new2DArray(rowNum, colNum);
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                newArr[row][col] = (T) inner[row][col].sub(other.inner[row][col]);
            }
        }
        return new Matrix<T>(newArr, maker);
    }
    /**
     * Multiplies one matrix with another and returns the result.\
     * 
     * @param other the matrix to multiply by
     * @return the result of multiplying the two matrices, or <code>null</code> if matrix multiplying between them is not well-defined
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Matrix<T> mul(Matrix<T> other) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        if (colNum != other.rowNum) {
            return null;
        }
        T[][] newArr = (T[][]) (maker.newInstance()).new2DArray(rowNum, other.colNum);
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < other.colNum; col++) {
                T init = maker.newInstance(); init.makeThisZero();
                for (int otherRow = 0; otherRow < other.rowNum; otherRow++) {
                    for (int otherCol = 0; otherCol < colNum; otherCol++) {
                        init.addAssign((T) inner[row][otherCol].add(other.inner[otherRow][col]));
                    }
                }
                newArr[row][col].assign(init);
            }
        }
        return new Matrix<T>(newArr, maker);
    }
    /**
     * Scales each value in a matrix by a scalar
     * @param scalar the scalar to scale by
     */
    public void scaleThis(T scalar) {
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                inner[row][col].mulAssign(scalar);
            }
        }
    }
    /**
     * Divides one matrix by another and returns the result.\
     * 
     * @param other the matrix to divide by
     * @return the result of dividing one matrix by the other, or <code>null</code> if matrix division between them is not well-defined
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Matrix<T> div(Matrix<T> other) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        if (colNum != other.rowNum) {
            return null;
        }
        T[][] newArr = (T[][]) (maker.newInstance()).new2DArray(rowNum, other.colNum);
        Matrix<T> newOther = ((Matrix<T>) other.clone()); newOther.invertThis();
        other = null; // Shouldn't actually change other, but prevents us from doing so.
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < newOther.colNum; col++) {
                T init = maker.newInstance(); init.makeThisZero();
                for (int otherRow = 0; otherRow < newOther.rowNum; otherRow++) {
                    for (int otherCol = 0; otherCol < colNum; otherCol++) {
                        init.addAssign((T) inner[row][otherCol].add(newOther.inner[otherRow][col]));
                    }
                }
                newArr[row][col].assign(init);
            }
        }
        return new Matrix<T>(newArr, maker);
    }

    public static void main(String[] args) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException  
    {
        Matrix<MatDouble> mat = new Matrix<MatDouble>(2, 2, MatDouble.class);
        System.out.println(mat.det());
        mat = identity(2, MatDouble.class);
        System.out.println(mat.det());

        Matrix<MatDouble> answer = new Matrix<MatDouble>( new MatDouble[][] {
            new MatDouble[] {new MatDouble(1.0), new MatDouble(-1.0), new MatDouble(-2.0), new MatDouble(4.0)},
            new MatDouble[] {new MatDouble(2.0), new MatDouble(-1.0), new MatDouble(-1.0), new MatDouble(2.0)},
            new MatDouble[] {new MatDouble(2.0), new MatDouble(1.0), new MatDouble(4.0), new MatDouble(16.0)}
        }, MatDouble.class);
        mat = new Matrix<MatDouble>( new MatDouble[][] {
            new MatDouble[] {new MatDouble(1.0), new MatDouble(), new MatDouble(), new MatDouble(24.0)},
            new MatDouble[] {new MatDouble(), new MatDouble(1.0), new MatDouble(), new MatDouble(72.0)},
            new MatDouble[] {new MatDouble(), new MatDouble(), new MatDouble(1.0), new MatDouble(-26.0)}
        }, MatDouble.class);
        mat.to_rref();
        System.out.println(mat.equals(answer));

        answer = new Matrix<MatDouble>(new MatDouble[][] {
            new MatDouble[] {new MatDouble(2.0), new MatDouble()},
            new MatDouble[] {new MatDouble(), new MatDouble(2.0)},
        }, MatDouble.class);
        mat = Matrix.identity(2, MatDouble.class);
        Matrix<MatDouble> otherMat = (Matrix<MatDouble>) mat.clone();
        System.out.println(answer.equals(mat.add(otherMat)));
    }
}
