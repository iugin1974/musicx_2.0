package notation;

import java.util.HashMap;
import java.util.Map;

import Measure.Bar;
import musicEvent.Alteration;
import musicEvent.Note;
import musicInterface.MusicObject;

/**
 * Gestisce il contesto degli accidentali in uno spartito musicale.
 * <p>
 * Tiene traccia delle alterazioni già applicate alle note all'interno di una misura
 * e determina se una nota necessita di un accidental secondo la chiave corrente.
 * </p>
 */
public class AccidentalContext {

    /**
     * Classe interna che memorizza lo stato di una nota: midiNumber e alterazione.
     */
    private static class NoteState {
        int midi;
        Alteration alteration;

        NoteState(int midi, Alteration alt) {
            this.midi = midi;
            this.alteration = alt;
        }
    }

    /** Mappa che associa ogni nota al suo stato corrente */
    private Map<Note, NoteState> noteState;
    private AccidentalEngine engine;
    /** Lo spartito musicale a cui questo contesto si riferisce */
    private Score score;

    /**
     * Costruisce un contesto di accidentali per lo spartito dato.
     *
     * @param score lo spartito musicale da monitorare
     * @engine il motore di calcolo delle alterazioni nella battuta
     */
    public AccidentalContext(Score score) {
        this.score = score;
        engine = new AccidentalEngine(this.score);
        noteState = new HashMap<>();
    }
    /**
     * Verifica se una nota necessita di un accidental.
     * <p>
     * La logica segue questi passaggi:
     * <ul>
     *   <li>Recupera la key signature corrente per lo staff e il tick della nota</li>
     *   <li>Chiede alla key signature se la nota necessita di un accidental</li>
     *   <li>Controlla se la nota è già stata processata con gli stessi valori</li>
     *   <li>Aggiorna lo stato della nota nella mappa</li>
     *   <li>Imposta lo stato di accidental sulla nota</li>
     * </ul>
     * </p>
     *
     * @param n la nota da verificare
     */
    private void checkForAlteration(Note n) {
        int midi = n.getMidiNumber();
        Alteration alt = n.getAlteration();

        // Controlla se la nota è già stata processata con gli stessi valori
        NoteState prevState = noteState.get(n);
        if (prevState != null && prevState.midi == midi && prevState.alteration == alt) {
            // La nota è già stata processata nella misura, quindi non serve cambiare nulla
            return;
        }

        // Aggiorna lo stato della nota nella mappa
        noteState.put(n, new NoteState(midi, alt));

        // Il flag needsAccidental è già calcolato dall'engine
        // Qui non facciamo altro, la nota mantiene il flag impostato
    }

    /**
     * Resetta lo stato delle alterazioni all'inizio di una nuova misura.
     * Deve essere chiamato quando arriva una stanghetta.
     */
    public void reset() {
        noteState.clear();
    }

}
