package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notation.Score;
import notation.Staff;
import notation.Voice;
import musicEvent.Note;
import musicEvent.NoteEvent;

public class VoiceTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
        score.addStaff();
    }

    @Test
    void testAddAndGetNote() {
        NoteEvent n1 = new Note(0, 4); // Do4
        NoteEvent n2 = new Note(2, 4); // Re4
        score.addObject(n1,0,1);
        score.addObject(n2,0,1);
        
        assertEquals(n1, score.getObjects(0).get(0));
        assertEquals(n2, score.getObjects(0).get(1));
    }

    @Test
    void testGetNextNote() {
        NoteEvent n1 = new Note(0, 4); // Do4
        NoteEvent n2 = new Note(2, 4); // Re4
        score.addObject(n1,0,1);
        score.addObject(n2,0,1);

        assertEquals(n2, score.getNextNote(n1));
        assertNull(score.getNextNote(n2));
    }
}
