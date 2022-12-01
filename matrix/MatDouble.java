// MatDouble
// May Neelon
// 
// A class representing a generic implementation of a double
// I agree to abide by the Acamedic Honesty Agreement

package matrix;

public class MatDouble implements Num {
    // The actual wrapped double
    private double inner;
    public MatDouble(double d) {
        inner = d;
    }
    public MatDouble() {
        inner = 0.0;
    }

    public Num add(Num other) {
        return (Num) new MatDouble(inner + ((MatDouble) other).inner);
    }
    public Num sub(Num other) {
        return (Num) new MatDouble(inner - ((MatDouble) other).inner);
    }
    public Num mul(Num other) {
        return (Num) new MatDouble(inner * ((MatDouble) other).inner);
    }
    public Num div(Num other) {
        return (Num) new MatDouble(inner / ((MatDouble) other).inner);
    }

    public void invertThis() {
        inner = 1 / inner;
    }
    public void makeThisOne() {
        inner = 1.0;
    }
    public void makeThisZero() {
        inner = 0.0;
    }

    public void assign(Num other) {
        inner = ((MatDouble) other).inner;
    }
    public void addAssign(Num other) {
        inner += ((MatDouble) other).inner;
    }
    public void subAssign(Num other) {
        inner -= ((MatDouble) other).inner;
    }
    public void mulAssign(Num other) {
        inner *= ((MatDouble) other).inner;
    }
    public void divAssign(Num other) {
        inner /= ((MatDouble) other).inner;
    }

    public Num clone() {
        return (Num) new MatDouble(inner);
    }

    public boolean isZero() {
        return inner == 0.0;
    }

    public Num[][] new2DArray(int n, int m) {
        MatDouble[][] arr =  new MatDouble[n][m];
        for (MatDouble[] mda : arr) {
            for(int i = 0; i < m; i++) {
                mda[i] = new MatDouble(0.0);
            }
        }
        return (Num[][]) arr;
    }
    @Override
    public String toString() {
        return "MatDouble [inner=" + inner + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(inner);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        MatDouble other = (MatDouble) obj;
        if (Double.doubleToLongBits(inner) != Double.doubleToLongBits(other.inner))
            return false;
        return true;
    }
}