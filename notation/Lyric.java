package notation;

import musicEvent.NoteEvent;

/**
 * Rappresenta una singola sillaba di testo (lyric) associata a una nota in una partitura musicale.
 * <p>
 * Ogni Lyric è collegata a una {@link NoteEvent} specifica (la nota "padre") e contiene informazioni
 * sullo staff, la voce e la strofa (stanza) a cui appartiene.
 * </p>
 * <p>
 * Quando viene creata, la Lyric si registra automaticamente presso la nota padre, in modo che
 * la nota possa gestire tutte le lyric ad essa associate.
 * </p>
 * 
 * @author 
 */
public class Lyric {
	    private Syllable syllable;
	    private NoteEvent parentNote;
	    private int staff, voice, stanza;

	    public Lyric(Syllable syllable, NoteEvent note, int staff, int voice, int stanza) {
	        this.syllable = syllable;
	        this.parentNote = note;
	        this.staff = staff;
	        this.voice = voice;
	        this.stanza = stanza;
	        note.addLyric(this);
	    }

	    public Syllable getSyllable() { return syllable; }
	    public NoteEvent getParentNote() { return parentNote; }
	    public int getStaff() { return staff; }
	    public int getVoice() { return voice; }
	    public int getStanza() { return stanza; }
}
