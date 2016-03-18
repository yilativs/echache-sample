package foo.cache.hazelcast;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import foo.model.User;

public class UserExternalizable extends User implements Externalizable {

	public UserExternalizable(int id, String login) {
		super(id, login);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(userId);// we had to make id NOT final and expose to derived classes for perfomance
		out.writeUTF(login);// we had to make login NOT final and expose to derived classes for perfomance
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		userId = in.readInt();
		login = in.readUTF();
	}

}
