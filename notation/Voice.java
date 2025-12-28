package notation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import musicEvent.Note;
import musicEvent.NoteEvent;
import musicInterface.MusicObject;

public class Voice implements Iterable<MusicObject> {

    private final int voiceNumber;
    private final List<MusicObject> objects;

    private static final Comparator<MusicObject> BY_TICK =
            Comparator.comparingInt(MusicObject::getTick);

    
    protected Voice(int voiceNumber) {
        this.voiceNumber = voiceNumber;
        this.objects = new ArrayList<>();
    }

    /** Restituisce il tipo di voice */
    protected int getVoiceType() {
        return voiceNumber;
    }

    /** Aggiunge un oggetto al layer */
    protected void addObject(MusicObject o) {
        if (o == null) return;

        objects.add(o);
        objects.sort(BY_TICK);
    }

    /** Rimuove un oggetto dal layer */
    protected boolean removeObject(MusicObject o) {
        return objects.remove(o);
    }

    /** Restituisce tutti gli oggetti del layer (modificabile) */
    protected List<MusicObject> getObjects() {
        return objects;
    }
    
    /** Restituisce solo le note **/
    protected List<NoteEvent> getNotes() {
        List<NoteEvent> notes = new ArrayList<>();
        for (MusicObject object : objects) {
            if (object instanceof NoteEvent)
                notes.add((NoteEvent) object);
        }
        return notes;
    }
    
    protected MusicObject get(int i) {
    	return objects.get(i);
    }

    /** Pulisce tutti gli oggetti dal layer */
    protected void clear() {
        objects.clear();
    }
    
	protected boolean isEmpty() {
		return objects.isEmpty();
	}
	
	protected int size() {
		return objects.size();
	}

	@Override
	public Iterator<MusicObject> iterator() {
		return objects.iterator();
	}
	
	protected void changeTick(MusicObject o, int t) {
		o.setTick(t);
		objects.sort(BY_TICK);
	}
	
	
}
