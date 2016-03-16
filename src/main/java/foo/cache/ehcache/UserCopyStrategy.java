package foo.cache.ehcache;

import foo.model.User;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.compound.ReadWriteCopyStrategy;

public class UserCopyStrategy implements ReadWriteCopyStrategy<Element> {

	private static final long serialVersionUID = 1L;

	@Override
	public Element copyForRead(Element userElement, ClassLoader classLoader) {
		User user = (User) userElement.getObjectValue();
		return new Element(userElement.getObjectKey(),new User(user.getId(),user.getLogin()));
	}

	@Override
	public Element copyForWrite(Element userElement, ClassLoader classLoader) {
		User user = (User) userElement.getObjectValue();
		return new Element(userElement.getObjectKey(),new User(user.getId(),user.getLogin()));
	}

}
