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
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (dateTime ^ (dateTime >>> 32));
		result = prime * result + eventType;
		result = prime * result + (int) (kidId ^ (kidId >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KidEvent other = (KidEvent) obj;
		if (dateTime != other.dateTime)
			return false;
		if (eventType != other.eventType)
			return false;
		if (kidId != other.kidId)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "KidEvent [dateTime=" + dateTime + ", eventType=" + eventType + ", kidId=" + kidId + "]";
	}
	
}