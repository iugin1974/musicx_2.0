package Tests;

import java.util.ArrayList;

import musicEvent.KeyAlteration;
import musicEvent.Modus;
import musicEvent.NamedNote;
import musicEvent.Note;
import musicEvent.Scale;

public class ScaleStressTest {


    // Sequenze delle tonalità in base al numero di diesis/bemolli
    private static final int[] MAJOR_SHARPS = {0, 7, 2, 9, 4, 11, 6}; // C, G, D, A, E, B, F#
    private static final int[] MAJOR_FLATS  = {0, 5, 10, 3, 8, 1, 6}; // C, F, Bb, Eb, Ab, Db, Gb
    private static final int[] MINOR_SHARPS = {9, 4, 11, 6, 1, 8, 3}; // A, E, B, F#, C#, G#, D#
    private static final int[] MINOR_FLATS  = {9, 2, 7, 0, 5, 10, 3}; // A, D, G, C, F, Bb, Eb

    public static int getTonicMidi(int numberOfAlterations, int typeOfAlterations, Modus modus) {
        int tonic = 0;

        if (typeOfAlterations == 1) { // diesis
            tonic = (modus == Modus.MAJOR_SCALE) ? MAJOR_SHARPS[numberOfAlterations] : MINOR_SHARPS[numberOfAlterations];
        } else if (typeOfAlterations == -1) { // bemolle
            tonic = (modus == Modus.MAJOR_SCALE) ? MAJOR_FLATS[numberOfAlterations] : MINOR_FLATS[numberOfAlterations];
        } else { // nessuna alterazione
            tonic = (modus == Modus.MAJOR_SCALE) ? 0 : 9; // C major o A minor
        }

        // MIDI della tonica nella quarta ottava
        return 60 + tonic; // tonic in semitoni sopra C
    }


    public static void main(String[] args) {
        // Test principali
        System.out.println(getTonicMidi(0, 0, Modus.MAJOR_SCALE) + " : " + 60);   // C Major
        System.out.println(getTonicMidi(1, 1, Modus.MAJOR_SCALE) + " : " + 67);   // G Major
        System.out.println(getTonicMidi(2, -1, Modus.MAJOR_SCALE) + " : " + 70);  // Bb Major
        System.out.println(getTonicMidi(3, 1, Modus.MAJOR_SCALE) + " : " + 69);   // A Major
        System.out.println(getTonicMidi(4, -1, Modus.MAJOR_SCALE) + " : " + 68);  // Ab Major
        System.out.println(getTonicMidi(5, 1, Modus.MAJOR_SCALE) + " : " + 71);   // B Major
        System.out.println(getTonicMidi(6, -1, Modus.MAJOR_SCALE) + " : " + 66);  // Gb Major
        System.out.println(getTonicMidi(0, 0, Modus.MINOR_SCALE) + " : " + 69);   // A minor
        System.out.println(getTonicMidi(1, 1, Modus.MINOR_SCALE) + " : " + 64);   // E minor
        System.out.println(getTonicMidi(2, -1, Modus.MINOR_SCALE) + " : " + 67);  // G minor
        System.out.println(getTonicMidi(3, 1, Modus.MINOR_SCALE) + " : " + 66);   // F# minor
        System.out.println(getTonicMidi(4, -1, Modus.MINOR_SCALE) + " : " + 65);  // F minor
        System.out.println(getTonicMidi(5, 1, Modus.MINOR_SCALE) + " : " + 68);   // G# minor
        System.out.println(getTonicMidi(6, -1, Modus.MINOR_SCALE) + " : " + 63);  // Eb minor
    }


}
