package foo.model;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -5103830073654986565L;
	private final int id;
	private final String login;

	public User(int id, String login) {
		super();
		this.id = id;
		this.login = login;
	}

	public int getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
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
		User other = (User) obj;
		if (id != other.id)
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

}
