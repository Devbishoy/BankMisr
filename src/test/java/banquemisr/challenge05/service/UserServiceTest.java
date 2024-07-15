package banquemisr.challenge05.service;


import banquemisr.challenge05.util.Validator;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


/**
 * @author Bishoy Samir test user service
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {


    private final Validator validator=new Validator();

    @Test
    void testValidatePassword() {
        String password = "wdcff";
        Assert.isTrue(!validator.isValidPassword(password));
    }

    @Test
    void testValidateEmail() {
        String email = "test.com";
        Assert.isTrue(!validator.isValidPassword(email));
    }


}
