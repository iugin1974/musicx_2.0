package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import musicEvent.Note;

class CurvedConnectionTest {

	private Note n1, n2;
	
	@BeforeEach
    void setUp() {
		 n1 = new Note(10,0);
		 n2 = new Note(11,1);
		 n1.tieStart();
		 n2.tieEnd();
	}
	
	@Test
	void testConnectPossible() {
		assertTrue(n1.isTiedStart(), "Tied");
	}

}
