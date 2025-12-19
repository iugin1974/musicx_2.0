package Tests;

import musicEvent.NamedNote;

public class TestNamedNote {

	public static void main(String[] args) {
		for (int i=0; i<100; i++) {
	NamedNote n = new NamedNote(i, 2);
	if (n.getName() == null) continue;
	System.out.println(n);
		}
	}

}
