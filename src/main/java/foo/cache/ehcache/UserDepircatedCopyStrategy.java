package foo.cache.ehcache;

import foo.model.User;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.compound.CopyStrategy;

public class UserDepircatedCopyStrategy implements CopyStrategy {

	private static final long serialVersionUID = 1L;

	@Override
	public <T> T copy(T obj) {
		Element element = (Element) obj;
		User user = (User) element.getObjectValue();
		return (T) new Element(element.getObjectKey(), new User(user.getId(), user.getLogin()));
	}

}
