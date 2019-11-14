package ie.gmit.ds;

import java.util.Collection;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserApiResource {
	
	private HashMap<Integer, User> usersMap = new HashMap<>();

	public UserApiResource() {
		
		User testUser = new User(1, "Neil Kyne", "neil.kyne@gmail.com", "l1br@ry");
		
		usersMap.put(testUser.getUserId(), testUser);
	}

	@GET
	public Collection<User> getUsers() {
		// usersMap.values() returns Collection<User>
		// Collection is the interface implemented by Java collections like ArrayList,
		// LinkedList etc.
		// It's basically a generic list.
		// https://docs.oracle.com/javase/7/docs/api/java/util/Collection.html

		return usersMap.values();
	}
}
