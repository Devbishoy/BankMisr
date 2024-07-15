package banquemisr.challenge05.mock;

import banquemisr.challenge05.domain.User;

/**
 * @author Bishoy Samir
 * Mock user data  class
 */
public class MockUser {

    public User getUser() {
        User user = new User();
        user.setEmail("Dummy@user.com");
        user.setFirstName("dummy");
        user.setLastName("dummy2");
        user.setActivated(true);
        user.setActivated(true);
        return user;
    }
}
