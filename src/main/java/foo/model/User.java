package foo.model;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -5103830073654986565L;
	protected int userId;
	protected String login;

	public User(int userId, String login) {
		this.userId = userId;
		this.login = login;
	}

	public int getUserId() {
		return userId;
	}

	public String getLogin() {
		return login;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
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
		if (userId != other.userId)
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

}
