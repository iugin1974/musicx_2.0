package musicEvent;

public class ArraysUtils {

	public static <T> T getElementAt(T[] array, int pos) {
		return array[pos%array.length];
	}
	public static int getElementAt(int[] array, int pos) {
		return array[pos%array.length];
	}
	
	public static int containsElement(int[] array, int element) {
		for (int i=0; i<array.length; i++) {
			if (array[i] == element) return i;
		}
		return -1;
	}
}
