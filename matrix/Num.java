// Num
// May Neelon
// 
// A class representing a generic number
// I agree to abide by the Acamedic Honesty Agreement

package matrix;

/**
 * An interface representing a generic number.
 */
public interface Num {
    /**
     * Adds two <code>Num</code>s together, returning the result.
     * Should return <code>null</code> if <code>other</code> is not the same type as <code>this</code>.
     * @param other The number to be added
     * @return The result of adding <code>this</code> to <code>other</code>
     */
    public Num add(Num other);
    /**
     * Subtracts one <code>Num</code> from another, returning the result.
     * Should return <code>null</code> if <code>other</code> is not the same type as <code>this</code>.
     * @param other The number to subtract
     * @return The result of subtracting <code>other</code> from <code>this</code>
     */
    public Num sub(Num other);
    /**
     * Multiplies two <code>Num</code>s together, returning the result.
     * Should return <code>null</code> if <code>other</code> is not the same type as <code>this</code>.
     * @param other The number to be multiplied by
     * @return The result of multiplying <code>this</code> by <code>other</code>
     */
    public Num mul(Num other);
    /**
     * Divides one <code>Num</code> by another, returning the result.
     * Should return <code>null</code> if <code>other</code> is not the same type as <code>this</code>.
     * @param other The number to be divided by
     * @return The result of dividing <code>this</code> by <code>other</code>
     */
    public Num div(Num other);  

    /**
     * Inverts <code>this</code>. Should be equivalent to dividing 1 by <code>this</code>.
     * Should make the fields of <code>this</code> <code>null</code> if inversion is not well-defined for <code>this</code>.  
     */
    public void invertThis();
    /**
     * Changes <code>this</code> into this class' representation of "1".
     * This representation should be the multiplicative identity of this class.
     * i.e. <code>someOtherNum.mul(this.makeThisOne())</code> and <code>someOtherNum.div(this.makeThisOne())</code> should both equal <code>someOtherNum</code>.
     */
    public void makeThisOne();
    /**
     * Changes <code>this</code> into this class' representation of "0".
     * This representation should be the additive identity of this class.
     * i.e. <code>someOtherNum.add(this.makeThisZero())</code> and <code>someOtherNum.sub(this.makeThisZero())</code> should both equal <code>someOtherNum</code>.
     */
    public void makeThisZero();
    /**
     * Assigns <code>this</code> the value of <code>other</code>. 
     * From other classes, this should be equivalent to <code>thisVar = other.clone()</code>.
     */
    public void assign(Num other);
    /**
     * Adds two <code>Num</code>s together and stores the result in <code>this</code>.
     * Should make the fields of <code>this</code> <code>null</code> if <code>other</code> is not the same type as <code>this</code>.
     * @param other The number to be added
     */
    public void addAssign(Num other);
    /**
     * Subtracts one <code>Num</code> from another and stores the result in <code>this</code>.
     * Should make the fields of <code>this</code> <code>null</code> if <code>other</code> is not the same type as <code>this</code>.
     * @param other The number to subtract
     */
    public void subAssign(Num other);
    /**
     * Multiplies two <code>Num</code>s together and stores the result in <code>this</code>.
     * Should make the fields of <code>this</code> <code>null</code> if <code>other</code> is not the same type as <code>this</code>.
     * @param other The number to be multiplied by
     */
    public void mulAssign(Num other);
    /**
     * Divides one <code>Num</code> by another and stores the result in <code>this</code>.
     * Should make the fields of <code>this</code> <code>null</code> if <code>other</code> is not the same type as <code>this</code>.
     * @param other The number to be divided by
     */
    public void divAssign(Num other);

    /**
     * Clones <code>this</code>. Should be equivalent to <code>Object.clone()</code>, except returns as a <code>Num</code>.
     * @return A deep copy of <code>this</code>
     */
    public Num clone();

    /**
     * Compares <code>this</code> against this class' representation of 0.
     * @return <code>true</code> if <code>this.makeThisZero()</code> is the same as <code>this</code>, otherwise <code>false</code>
     */
    public boolean isZero();

    /**
     * Returns a 2d <code>n</code> by <code>m</code> array.
     * Every object in the array should be initialized (i.e. not <code>null</code>). 
     * @param rows The number of rows that the new array should have
     * @param cols The number of columns that the new array should have
     * @return A new initalized array
     */
    public Num[][] new2DArray(int rows, int cols);
}
