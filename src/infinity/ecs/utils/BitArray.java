package infinity.ecs.utils;

/**
 * Class that can be used to store the value of an arbitrary number of bits.
 * <p>
 * <b>Notes:</b><br>
 * Integers are used internally to store the saved bit values. All bits are
 * assumed to be zero by default, even bits that are outside of the current
 * capacity of the array. The BitArray is dynamically resized, if a one is set
 * somewhere outside of the current capacity.
 *
 * @author preip
 *
 * ToDo: Implement hashCode in an appropriate manner.
 */
public class BitArray {

	/**
	 * The internal array which is used to store the bits. Because its an
	 * integer array, the number of bits that can be stored is the length of the
	 * array multiplied with 32.
	 */
	private int[] _array;

	/**
	 * Creates a new instance of the BitArray class.
	 */
	public BitArray() {
		_array = new int[1];
		_array[0] = 0;
	}

	/**
	 * Gets the bit on the specified index. The index must be a positive number.
	 *
	 * @param index The index of the desired bit.
	 * @return The current state of the desired bit.
	 */
	public boolean get(int index) {
		// the required array index is just the index divided by the size of the
		// datatype, which is 32 bit for integers.
		// since shifts are faster than division, a right shift of 5 is used 
		// instead, which gets the same result
		int arrayIndex = index >>> 5;
		// if the desired array index is above the current capacity, 
		// simply return false, since every bit is false
		// by default
		if (arrayIndex >= _array.length) {
			return false;
		}
		// create the search mask by setting all bits above the 31 bit to zero, 
		// so only the index bits for the current
		// array cell remain. The value is then shifted to the left by the 
		//number of places determined by that index 
		int mask = 1 << (index & 0x1F);
		// the corresponding integer is conjuncted by a binary and, which 
		// results in the value zero, if the bit was not set, or nine zero, 
		//if it was set, which is exactly what is returned as the result
		return (_array[arrayIndex] & mask) != 0;
	}

	/**
	 * Set the bit with the specified index to the specified value. The index
	 * must be a positive number.
	 *
	 * @param index The index of the bit that should be set.
	 * @param value The value which the bit should be set to.
	 */
	public void set(int index, boolean value) {
		// the required array index is just the index divided by the size of the
		//datatype, which is 32 bit for integers.
		// since shifts are faster than division, a right shift of 5 is used 
		//instead, which gets the same result.
		int arrayIndex = index >>> 5;
		// if the array is to short, it needs to resized
		if (arrayIndex > _array.length - 1) {
			// if the value the bit should be set to is false and it's assumed, 
			// that every bit is false by default, than there is no reason to 
			//resize the array.
			if (!value) {
				return;
			}
			// otherwise the array gets resized to match the required size
			ResizeArray(arrayIndex + 1);
		}
		// create the search mask by setting all bits above the 31 bit to zero,
		// so only the index bits for the current array cell remain.
		//The value is then shifted to the left by the number of places 
		//determined by that index.
		int mask = 1 << (index & 0x1F);
		// if the bit needs to be set to one, the current value is OR conjuncted
		//with the mask.
		if (value) {
			_array[arrayIndex] |= mask;
		}
		// if the bit needs to be set to zero, the current value is AND 
		//conjuncted with the inverted mask.
		else {
			_array[arrayIndex] &= ~mask;
		}
	}

	public boolean contains(BitArray bitArray) {
		int i = 0;
		int l = Math.min(_array.length, bitArray._array.length);
		// to check, if all set bits from the other array are also set in this 
		// array, each integer which is index is part of both arrays is AND 
		// conjuncted. If the result of the conjunction is the same as the 
		// corresponding integer from the other array,this array contains all 
		// the set bits.
		for (; i < l; i++) {
			int t = bitArray._array[i];
			if ((_array[i] & t) != t) {
				return false;
			}
		}
		// if this array is the same length or longer than the other array, 
		// and the above comparisons were all true, than the comparison is 
		// finished.
		if (_array.length >= bitArray._array.length) {
			return true;
		}
		// otherwise all integers of the other array that are above the size 
		// of this array need to be checked. If they contain any set bits, 
		// the whole comparison is false. 
		l = bitArray._array.length;
		for (; i < l; i++) {
			if (bitArray._array[i] != 0) {
				return false;
			}
		}
		// if this comparison is also true, this array contains the other array
		return true;
	}

	/**
	 * Gets the current number of bits that can be stored in this bit array.
	 *
	 * @return The capacity of this bit array.
	 */
	public int getCapacity() {
		// the capacity is the length of the array multiplied by 32, which is 
		//equal to a five bit left shift
		return _array.length << 5;
	}

	/**
	 * Checks if this bit array is equal to the specified object.
	 *
	 * @param obj The object this BitArray is compared with.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!(obj instanceof BitArray))
			return false;
		BitArray that = (BitArray) obj;
		// check all bits that both arrays have in common
		int commonLength = Math.min(this._array.length, that._array.length);
		// if one of the bits doesn't match, the bit arrays can't be equal
		for (int i = 0; i < commonLength; i++)
			if (this._array[i] != that._array[i])
				return false;
		// if the common bits of both arrays were equal, check the remaining
		// bits of the bigger array
		BitArray max = this._array.length > that._array.length ? this : that;
		int maxLength = max._array.length;
		// if one of the remaining bits is not zero, the two arrays can't be equal
		for (int i = commonLength; i < maxLength; i++)
			if (max._array[i] != 0)
				return false;
		// if all remaining bits were zero, both arrays are equal
		return true;
	}

	/**
	 * Creates a clone of this BitArray instance.
	 */
	public BitArray clone() {
		BitArray newBa = new BitArray();
		newBa.ResizeArray(this._array.length);
		System.arraycopy(this._array, 0, newBa._array, 0, this._array.length);
		return newBa;
	}

	/**
	 * Resizes the array to the new size. There is no error checking, because
	 * this is an internal method. Therefore make sure that the size parameter
	 * is at least the same as the current size and also not negative.
	 *
	 * @param size The new size of the array
	 */
	private void ResizeArray(int size) {
		// NOTE: could System.arraycopy, but doing it manually is probably 
		// faster because of all the special cases which arraycopy contains,
		// but which are irrelevant here - This is just an untested assumption

		int[] newArray = new int[size];
		int i = 0;
		int l = _array.length;
		// first copy all fields common fields from the old to the new array
		for (; i < l; i++) {
			newArray[i] = _array[i];
		}
		// than set all remaining fields to zero
		for (; i < size; i++)
			newArray[i] = 0;
		_array = newArray;
	}
}
