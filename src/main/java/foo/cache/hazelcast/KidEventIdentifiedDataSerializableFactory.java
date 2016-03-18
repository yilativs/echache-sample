package foo.cache.hazelcast;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class KidEventIdentifiedDataSerializableFactory implements DataSerializableFactory {

	public static final int FACTORY_ID_IN_CONFIG = 777;
	public static final int KIDEVENT_IDENTIFIED_DATAS_SERIALIZABLE_ID = 0;

	@Override
	public IdentifiedDataSerializable create(int typeId) {
		return KIDEVENT_IDENTIFIED_DATAS_SERIALIZABLE_ID == typeId ? new KidEventIdentifiedDataSerializable(0, 0, 0) : null;
	}

}
