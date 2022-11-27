package matrix;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public class Matrix<T extends Num> implements Cloneable {
    private T[][] inner;
    private int rowNum; 
    private int colNum;
    private Constructor<T> maker;

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
    public static <T extends Num> Matrix<T> identity(int rowNum, Class<T> clazz)  throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        T[][] arr = (T[][]) (clazz.getConstructor().newInstance()).new2DArray(rowNum, rowNum);
        for (int i = 0; i < rowNum; i++) {
            arr[i][i].makeThisOne();
        }

        return new Matrix<T>(arr, clazz);
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(inner);
        return result;
    }
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
    @Override
    public Object clone() {
        try {
            return (Object) new Matrix<T>(inner, maker);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString() + " when attempting to clone Matrix");
            return null;
        }
    }
    public T[] getRow(int row) {
        if (row < 0 || row > inner.length) {
            return null;
        } else {
            return inner[row];
        }
    }
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
    @Override
    public String toString() {
        String s = "";
        for (T[] arr : inner) {
            s += "[ " + Arrays.toString(arr) + " ]\n";
        }
        return s;
    }
    public T det() throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        if (rowNum != colNum) {
            return null;
        }
        else if (rowNum == 0) {
            return null;
        }
        else if (rowNum == 1) {
            return inner[0][0];
        }
        else if (rowNum == 2) {
            return (T) inner[0][0].mul(inner[1][1]).sub(inner[1][0].mul(inner[0][1]));
        }
        else {
            T det = (T) inner[0][0].clone();
            det.makeThisZero();
            for (int j = 0; j < colNum; j++) { 
                T scalar = inner[0][j];
                if (j % 2 == 1) {
                    scalar.invertThis();
                }
                det.addAssign(scalar.mul(minor(1, j)));
            }
            return det;
        }
    }
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
    public void mul_row(int r, T m) {
        if (rowNum < 0 || this.rowNum > r) {
            return;
        } 
        for (int i = 0; i < inner[r].length; i++) {
            inner[r][i].mulAssign(m);
        }
        return;
    }
    public void div_row(int r, T d) {
        if (rowNum < 0 || this.rowNum > r) {
            return;
        } 
        for (int i = 0; i < inner[r].length; i++) {
            inner[r][i].mulAssign(d);
        }
        return;
    }
    public void add_row_by_ref(int r1, T[] r2) {
        if (r1 < 0 || r1 > rowNum) {
            return;
        } 
        for (int i = 0; i < inner[r1].length; i++) {
            inner[r1][i].addAssign(r2[i]);
        }
        return;
    }
    public void sub_row_by_ref(int r1, T[] r2) {
        if (r1 < 0 || r1 > rowNum) {
            return;
        } 
        for (int i = 0; i < inner[r1].length; i++) {
            inner[r1][i].subAssign(r2[i]); 
        }
        return;
    }
    public void to_rref() {
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
    public T minor(int i, int j) throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
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
    public void transpose() throws NoSuchMethodException, InstantiationException,
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
    public void invertThis() throws NoSuchMethodException, InstantiationException,
    IllegalAccessException, InvocationTargetException {
        if (rowNum != colNum) {
            return;
        }
        if (rowNum == 0) {
            return;
        }
        Matrix<T> newThis = cofactor(); newThis.transpose();
        T scalar = maker.newInstance(); scalar.makeThisOne();
        scalar.divAssign(det());
        newThis.scale(scalar);
        inner = newThis.inner;
    }
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
    public void scale(T scalar) {
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                inner[row][col].mulAssign(scalar);
            }
        }
    }
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
