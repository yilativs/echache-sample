package foo.cache.hazelcast;

import java.io.IOException;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import foo.model.User;

public class UserIdentifiedDataSerializable extends User implements IdentifiedDataSerializable {

	private static final long serialVersionUID = 5840862083005975744L;

	public UserIdentifiedDataSerializable(int id, String login) {
		super(id, login);
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeInt(userId);// we had to make id NOT final and expose to derived classes for perfomance
		out.writeUTF(login);// we had to make login NOT final and expose to derived classes for perfomance
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		userId = in.readInt();
		login = in.readUTF();
	}

	@Override
	public int getFactoryId() {
		return FooDataSerializableFactory.FACTORY_ID_IN_CONFIG;
	}

	@Override
	//FIXME THIS IS NOT a user ID this ugly name for instance creation in factory
	public int getId() {
		return FooDataSerializableFactory.USER_IDENTIFIED_DATAS_SERIALIZABLE_ID;
	}

}
