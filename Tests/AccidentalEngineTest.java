package Tests;

import musicEvent.Modus;
import musicEvent.Note;
import musicEvent.NoteEvent;
import notation.AccidentalContext;
import notation.AccidentalEngine;
import notation.KeySignature;
import notation.Score;
import notation.ScoreEvent;
import notation.ScoreEvent.Type;

import java.util.Arrays;

public class AccidentalEngineTest {

    public static void main(String[] args) {
        // Simuliamo un piccolo staff
        Score score = new Score();
        score.addStaff();
        AccidentalContext ae = new AccidentalContext(score);
        KeySignature ks = new KeySignature(2, 1, Modus.MAJOR_SCALE);
        ks.setTick(0);
        score.addObject(ks, 0, 0);
        
        score.addListener(ae);
        // Nota originale: C4, posInStaff = 0, alterazione = 0
        NoteEvent n1 = new Note(60, 0); // MIDI, alterazione
        n1.setTick(10);
        n1.setStaffPosition(0); // importante per la propagazione

        // Nota successiva nella stessa posizione, dovrebbe ereditare
        NoteEvent n2 = new Note(61, 1);
        n2.setTick(20);
        n2.setStaffPosition(0);

        // Aggiungiamo le note allo score
        score.addObject(n1, 0, 1);
        score.addObject(n2, 0, 1);

        // Creiamo l'engine

        System.out.println("=== Primo calcolo ===");
        printNoteState(n1, "n1");
        printNoteState(n2, "n2");

        // Inserimento di una nuova nota nella stessa posizione
        NoteEvent n3 = new Note(61, 1);
        n3.setTick(30);
        n3.setStaffPosition(0);
        score.addObject(n3, 0, 1);

        System.out.println("\n=== Inserimento n3 ===");
        
        printNoteState(n1, "n1");
        printNoteState(n2, "n2");
        printNoteState(n3, "n3");
    }

    private static void printNoteState(NoteEvent n, String name) {
        System.out.println(name + ": posInStaff=" + n.getStaffPosition() +
                ", alteration=" + n.getAlteration() +
                ", MIDI=" + n.getMidiNumber()+", need alteration: "+((Note)n).needsAccidental());
    }
}
