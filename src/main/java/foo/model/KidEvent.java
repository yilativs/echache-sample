package foo.model;

import java.io.Serializable;

public class KidEvent implements Serializable {
	private static final long serialVersionUID = 1810149628222255805L;
	final long dateTime;
	final int eventType;
	final long kidId;

	public KidEvent(long kidId, long dateTime, int eventType) {
		this.kidId = kidId;
		this.dateTime = dateTime;
		this.eventType = eventType;
	}
}