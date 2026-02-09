package Tests;

import musicEvent.Modus;
import musicEvent.ScaleType;
import musicEvent.Note;

public class TestScaleType {

	public static void main(String[] args) {
		System.out.println(ScaleType.getScaleAlterationType(
				new Note(62), Modus.MINOR_SCALE));

	}

}
