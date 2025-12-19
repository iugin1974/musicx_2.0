package notation;

import musicEvent.NoteEvent;

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
