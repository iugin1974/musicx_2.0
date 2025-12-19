package notation;

import java.util.ArrayList;
import java.util.List;

import musicEvent.Note;
import musicEvent.NoteEvent;
import musicInterface.MusicObject;

public class Voice {

    private final int voiceNumber;
    private final List<MusicObject> objects;

    public Voice(int voiceNumber) {
        this.voiceNumber = voiceNumber;
        this.objects = new ArrayList<>();
    }

    /** Restituisce il tipo di voice */
    public int getVoiceType() {
        return voiceNumber;
    }

    /** Aggiunge un oggetto al layer */
    public void addObject(MusicObject o) {
        if (o != null) {
            objects.add(o);
        }
    }

    /** Rimuove un oggetto dal layer */
    public boolean removeObject(MusicObject o) {
        return objects.remove(o);
    }

    /** Restituisce tutti gli oggetti del layer (modificabile) */
    public List<MusicObject> getObjects() {
        return objects;
    }
    
    /** Restituisce solo le note **/
    public List<NoteEvent> getNotes() {
    	List<NoteEvent> notes = new ArrayList<>();
    	for (MusicObject object : objects) {
    		if (object instanceof NoteEvent)
    			notes.add((Note)object);
    	}
    	return notes;
    }
    
    public MusicObject get(int i) {
    	return objects.get(i);
    }

    /** Pulisce tutti gli oggetti dal layer */
    public void clear() {
        objects.clear();
    }
    
	public boolean isEmpty() {
		return objects.isEmpty();
	}
	
	public int size() {
		return objects.size();
	}
}
