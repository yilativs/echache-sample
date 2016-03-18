package foo.cache.hazelcast;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class FooDataSerializableFactory implements DataSerializableFactory {
	
	public static final int FACTORY_ID_IN_CONFIG = 555;
	public static final int USER_IDENTIFIED_DATAS_SERIALIZABLE_ID = 1;

	@Override
	public IdentifiedDataSerializable create(int typeId) {
		switch (typeId) {
		case USER_IDENTIFIED_DATAS_SERIALIZABLE_ID:
			return new UserIdentifiedDataSerializable(0, null);
		default:
			return null;
		}
	}

}
