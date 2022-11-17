import java.util.Arrays;

public class Matrix {
    private double[][] inner;
    private int n; 
    private int m;
    public Matrix(int n, int m) {
        inner = new double[n][m];
        this.n = n;
        this.m = m;
    }
    public Matrix(double initVal, int n, int m) {
        inner = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                inner[i][j] = initVal;
            }
        }
        this.n = n;
        this.m = m;
    }
    public Matrix(double[][] initArr) {
        inner = initArr.clone();
        n = initArr.length;
        m = initArr[0].length;
        for (double[] arr : initArr) {
            if (this.m != arr.length); {
                inner = null;
                n = 0;
                m = 0;
                return;
            }
        }
    }
    public static Matrix identity(int n) {
        double[][] arr = new double[n][n];
        for (int i = 0; i < n; i++) {
            arr[i][i] = 1.0;
        }
        return new Matrix(arr);
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
        Matrix other = (Matrix) obj;
        if (!Arrays.deepEquals(inner, other.inner))
            return false;
        return true;
    }
    public double[] getRow(int row) {
        if (row < 0 || row > inner.length) {
            return null;
        } else {
            return inner[row];
        }
    }
    public Double getVal(int row, int col) {
        double[] r = getRow(row);
        if (r == null) {
            return null;
        }
        if (col < 0 || col > r.length) {
            return null;
        }
        return r[col];
    }
    @Override
    public String toString() {
        return "Matrix [inner=" + Arrays.toString(inner) + "]";
    }
    public Double det() {
        if (n != m) {
            return null;
        }
        else if (n == 1) {
            return inner[0][0];
        }
        else if (n == 2) {
            return inner[0][0] * inner[1][1] - inner[1][0] * inner[0][1];
        }
        else {
            Double det = 0.0;
            for (int i = 0; i < n; i++) {
                double[][] new_arr = new double[n - 1][n - 1];
                for (int row = 1; row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        if (col == i) {
                            continue;
                        }
                        int col_idx = col;
                        if (col > i) {
                            col_idx--;
                        }
                        int row_idx = row - 1;
                        new_arr[row_idx][col_idx] = inner[row][col];
                    }
                }
                Matrix new_mat = new Matrix(new_arr);
                double scalar = inner[0][i];
                if (i % 2 == 1) {
                    scalar *= -1;
                }
                det += scalar * new_mat.det();
            }
            return det;
        }
    }
    public void swapRows(int r1, int r2) {
        if (r1 > n || r1 < 0 || r2 > n || r2 < 0) {
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
        double[] temp_row = inner[r1].clone();
        inner[r1] = inner[r2];
        inner[r2] = temp_row;

    }
    public void mul_row(int r, double d) {
        
    }
}   