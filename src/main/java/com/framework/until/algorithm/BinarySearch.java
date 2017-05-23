package com.framework.until.algorithm;

/**
 * @author apple
 *
 */
public class BinarySearch {

	public static void main(String[] args) {

		int[] odds = { 1, 2, 3, 4, 5 };
		int[] evens = { 1, 2, 3, 4, 5, 6 };

		//binarySearch(odds, 0, odds.length, 2);
		binarySearch(evens, 0, evens.length, 2);

	}

	public static int binarySearch(int[] a, int fromIndex, int toIndex, int key) {
		int low = fromIndex;
		int high = toIndex - 1;
		while (low <= high) {
			int mid = (low + high) >>> 1;
			int midVal = a[mid];
			if (midVal < key)
				low = mid + 1;
			else if (midVal > key)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}

}
