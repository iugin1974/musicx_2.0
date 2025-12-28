package notation;

import musicInterface.MusicObject;

public class CompareTick implements java.util.Comparator<MusicObject> {
	@Override
	public int compare(MusicObject o1, MusicObject o2) {
		if (o1.getTick() > o2.getTick())
			return 1;
		if (o1.getTick() < o2.getTick())
			return -1;
		return 0;
	}
}