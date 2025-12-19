package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import musicEvent.KeyAlteration;
import musicEvent.Modus;
import musicEvent.Note;
import notation.Score;
import notation.Staff;

public class MusicTest {

    private KeyAlteration keyAlteration;
    private Note cNote;
    private Score score;

    @BeforeEach
    void setUp() {
        keyAlteration = new KeyAlteration();
        cNote = new Note(0, 0); // Do naturale
        score = new Score();
        score.addStaff();
    }

    @Test
    void testKeyAlterationC_Major() {
        ArrayList<Integer> altered = keyAlteration.getAlteredNoteNumbers(cNote, Modus.MAJOR_SCALE);
        assertTrue(altered.isEmpty(), "C major should have no sharps/flats");
    }

    @Test
    void testKeyAlterationG_Major() {
        Note gNote = new Note(7, 0); // Sol
        ArrayList<Integer> altered = keyAlteration.getAlteredNoteNumbers(gNote, Modus.MAJOR_SCALE);
        assertEquals(1, altered.size(), "G major should have one sharp");
        assertEquals(6, altered.get(0), "The sharp should be F#");
    }

    @Test
    void testScoreStaffAddition() {
        assertEquals(1, score.getStaffCount(), "Score should have one staff initially");
        score.addStaff();
        assertEquals(2, score.getStaffCount(), "Score should have two staffs after addition");
    }

    @Test
    void testStaffGetStaff() {
        Staff s = score.getStaff(0);
        assertNotNull(s, "Should return the first staff");
    }

}
