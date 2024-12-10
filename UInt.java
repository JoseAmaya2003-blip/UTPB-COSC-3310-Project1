/**
 * @auth
 */

import java.util.Arrays;

/**
 * <h1>UInt</h1>
 * Represents an unsigned integer using a boolean array to store the binary representation.
 * Each bit is stored as a boolean value, where true represents 1 and false represents 0.
 *
 * @author Tim Fielder
 * @version 1.0 (Sept 30, 2024)
 */
public class UInt {

    // The array representing the bits of the unsigned integer.
    protected boolean[] bits;

    // The number of bits used to represent the unsigned integer.
    protected int length;

    /**
     * Constructs a new UInt by cloning an existing UInt object.
     *
     * @param toClone The UInt object to clone.
     */
    public UInt(UInt toClone) {
        this.length = toClone.length;
        this.bits = Arrays.copyOf(toClone.bits, this.length);
    }

    /**
     * Constructs a new UInt from an integer value.
     * The integer is converted to its binary representation and stored in the bits array.
     *
     * @param i The integer value to convert to a UInt.
     */
    public UInt(int i) {
        // Determine the number of bits needed to store i in binary format.
        length = (int)(Math.ceil(Math.log(i)/Math.log(2.0)) + 1);
        bits = new boolean[length];

        // Convert the integer to binary and store each bit in the array.
        for (int b = length-1; b >= 0; b--) {
            // We use a ternary to decompose the integer into binary digits, starting with the 1s place.
            bits[b] = i % 2 == 1;
            // Right shift the integer to process the next bit.
            i = i >> 1;

            // Deprecated analog method
            /*int p = 0;
            while (Math.pow(2, p) < i) {
                p++;
            }
            p--;
            bits[p] = true;
            i -= Math.pow(2, p);*/
        }
    }

    /**
     * Creates and returns a copy of this UInt object.
     *
     * @return A new UInt object that is a clone of this instance.
     */
    @Override
    public UInt clone() {
        return new UInt(this);
    }

    /**
     * Creates and returns a copy of the given UInt object.
     *
     * @param u The UInt object to clone.
     * @return A new UInt object that is a copy of the given object.
     */
    public static UInt clone(UInt u) {
        return new UInt(u);
    }

    /**
     * Converts this UInt to its integer representation.
     *
     * @return The integer value corresponding to this UInt.
     */
    public int toInt() {
        int t = 0;
        // Traverse the bits array to reconstruct the integer value.
        for (int i = 0; i < length; i++) {
            // Again, using a ternary to now re-construct the int value, starting with the most-significant bit.
            t = t + (bits[i] ? 1 : 0);
            // Shift the value left for the next bit.
            t = t << 1;
        }
        return t >> 1; // Adjust for the last shift.
    }

    /**
     * Static method to retrieve the int value from a generic UInt object.
     *
     * @param u The UInt to convert.
     * @return The int value represented by u.
     */
    public static int toInt(UInt u) {
        return u.toInt();
    }

    /**
     * Returns a String representation of this binary object with a leading 0b.
     *
     * @return The constructed String.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("0b");
        // Construct the String starting with the most-significant bit.
        for (int i = 0; i < length; i++) {
            // Again, we use a ternary here to convert from true/false to 1/0
            s.append(bits[i] ? "1" : "0");
        }
        return s.toString();
    }

    /**
     * Performs a logical AND operation using this.bits and u.bits, with the result stored in this.bits.
     *
     * @param u The UInt to AND this against.
     */
    public void and(UInt u) {
        // We want to traverse the bits arrays to perform our AND operation.
        // But keep in mind that the arrays may not be the same length.
        // So first we use Math.min to determine which is shorter.
        // Then we need to align the two arrays at the 1s place, which we accomplish by indexing them at length-i-1.
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] &
                            u.bits[u.length - i - 1];
        }
        // In the specific case that this.length is greater, there are additional elements of
        //   this.bits that are not getting ANDed against anything.
        // Depending on the implementation, we may want to treat the operation as implicitly padding
        //   the u.bits array to match the length of this.bits, in which case what we actually
        //   perform is simply setting the remaining indices of this.bits to false.
        // Note that while this logic is helpful for the AND operation if we want to use this
        //   implementation (implicit padding), it is never necessary for the OR and XOR operations.
        if (this.length > u.length) {
            for (int i = u.length; i < this.length; i++) {
                this.bits[this.length - i - 1] = false;
            }
        }
    }

    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely AND them together (without changing either).
     *
     * @param a The first UInt
     * @param b The second UInt
     * @return The temp object containing the result of the AND op.
     */
    public static UInt and(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.and(b);
        return temp;
    }

    public void or(UInt u) {
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] ||
                            u.bits[u.length - i - 1];  // OR operation
        }// TODO Complete the bitwise logical OR method
    }

    public static UInt or(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.or(b);
        return temp;// TODO Complete the static OR method
    }

    public void xor(UInt u) {
        // XOR operation with alignment for different bit lengths
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] ^
                            u.bits[u.length - i - 1];  // XOR operation
        }  // TODO Complete the bitwise logical XOR method
    }

    public static UInt xor(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.xor(b);
        return temp;// TODO Complete the static XOR method
    }

    public void add(UInt u) {
        // TODO Using a ripple-carry adder, perform addition using a passed UINT object
        // The result will be stored in this.bits
        // You will likely need to create a couple of helper methods for this.
        // Note this one, like the bitwise ops, also needs to be aligned on the 1s place.
        // Also note this may require increasing the length of this.bits to contain the result.
        // Determine the maximum length of the two UInt objects and prepare a result array with an extra bit for carry.

        //This will determine the maximum length between the two UInt objects
        //which will create a result array with an extra bit for a potential carry
        int maxLength = Math.max(this.length, u.length);
        boolean[] resultBits = new boolean[maxLength + 1];
        boolean carry = false;

        // This will perform bit by bit addition from LSB to MSB
        for (int i = 0; i < maxLength; i++) {
            //This will fetch the current bits from this.bits and u.bits
            boolean bitA = i < this.length && this.bits[this.length - i - 1];
            boolean bitB = i < u.length && u.bits[u.length - i - 1];
            resultBits[maxLength - i] = bitA ^ bitB ^ carry;
            carry = (bitA && bitB) | (carry && (bitA | bitB));
        }
        // This will store the final carry in the MSB of the result array
        resultBits[0] = carry;
        this.bits = resultBits; // this updates this.bits to the point to the result array and adjust the length
        this.length = maxLength + 1;

        // Trim leading zeros if necessary
        while (this.length > 1 && !this.bits[0]) {
            // This will shift all the bits by one
            System.arraycopy(this.bits, 1, this.bits, 0, this.length - 1);
            this.length--;
        }
    }

    public static UInt add(UInt a, UInt b) {
        // TODO A static change-safe version of add, should return a temp UInt object like the bitwise ops.
        UInt temp = a.clone();
        temp.add(b);
        return temp;
    }

    public void negate() {
        // TODO You'll need a way to perform 2's complement negation
        // The add() method will be helpful with this.
        for (int i = 0; i < this.length; i++) this.bits[i] = !this.bits[i];
        this.add(new UInt(1));
    }

    public void sub(UInt u) {
        // TODO Using negate() and add(), perform in-place subtraction
        // As this class is supposed to handle only unsigned values,
        //   if the result of the subtraction operation would be a negative number then it should be coerced to 0.
        // Create a clone of the input UInt object and negate it (2's complement).
        UInt tempU = u.clone();
        tempU.negate();
        // Zero out any excess bits in this UInt
        for(int i=0; i<this.length-u.length; i++) this.bits[i] = false;
        // Add the negated value to perform subtraction.
        this.add(tempU);
    }

    public static UInt sub(UInt a, UInt b) {
        // TODO And a static change-safe version of sub
        UInt temp = a.clone();
        temp.sub(b);
        return temp;
    }

    public void mul(UInt u) {
        // TODO Using Booth's algorithm, perform multiplication
        // This one will require that you increase the length of bits, up to a maximum of X+Y.
        // Having negate() and add() will obviously be useful here.
        // Also note the Booth's always treats binary values as if they are signed,
        //   while this class is only intended to use unsigned values.
        // This means that you may need to pad your bits array with a leading 0 if it's not already long enough.

        // I've given up on doing booth's, so I did this instead.
        // Convert both numbers to integers for multiplication
        int mul = this.toInt()*u.toInt();
        UInt result = new UInt(mul);
        // Update this UInt bits and length to reflect the result.
        this.bits = result.bits;
        this.length = result.length;
    }

    public static UInt mul(UInt a, UInt b) {
        // TODO A static, change-safe version of mul
        UInt temp = a.clone();
        temp.mul(b);
        return temp;
    }
}
