package foo.cache.hazelcast;

import java.io.IOException;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import foo.model.KidEvent;

public class KidEventIdentifiedDataSerializable extends KidEvent implements IdentifiedDataSerializable {

	public KidEventIdentifiedDataSerializable(long kidId, long dateTime, int eventType) {
		super(kidId, dateTime, eventType);
	}

	private static final long serialVersionUID = 5941588721280057371L;

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeLong(kidId);
		out.writeLong(dateTime);
		out.writeInt(eventType);
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		kidId = in.readLong();
		dateTime = in.readLong();
		eventType = in.readInt();
	}

	@Override
	public int getFactoryId() {
		return KidEventIdentifiedDataSerializableFactory.FACTORY_ID_IN_CONFIG;
	}

	@Override
	public int getId() {
		return KidEventIdentifiedDataSerializableFactory.KIDEVENT_IDENTIFIED_DATAS_SERIALIZABLE_ID;
	}

}
