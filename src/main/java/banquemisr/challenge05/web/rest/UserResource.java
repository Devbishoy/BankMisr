package banquemisr.challenge05.web.rest;

import banquemisr.challenge05.domain.User;
import banquemisr.challenge05.domain.dto.PartiallyUpdate;
import banquemisr.challenge05.domain.dto.UserDTO;
import banquemisr.challenge05.security.SecurityCheck;
import banquemisr.challenge05.security.SecurityUser;
import banquemisr.challenge05.service.UserService;
import banquemisr.challenge05.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Bishoy samir
 * @apiNote @PreAuthorize("hasRole('ADMIN')") @Secured() annotations both not working in service layer or controller layer -> seems error with webflux and spring security 6
 */
@RestController
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityCheck securityCheck;

    /**
     * POST request create new user
     *
     * @param userDTO      user data holder
     * @param securityUser current login user
     * @return 200 status code and user updated data - access denied  if user role not allowed to access api
     * @throws 409 conflict when id exist while create new user
     *             400    bad request if any request data missing from payload
     */
    @PostMapping
    @JsonView(View.BasicUser.class)
    public Mono<User> createUser(@Valid @RequestBody UserDTO userDTO, @AuthenticationPrincipal SecurityUser securityUser) {
        // manually security check work around check @apiNote  above
        if (securityCheck.checkIfAdminRole(securityUser))
            return Mono.just(userService.createUser(userDTO));
        return Mono.empty();
    }

    /**
     * PUT request update exist user
     *
     * @param userDTO      user data holder
     * @param securityUser current login user
     * @return 200 status code and user updated data - access denied  if user role not allowed to access api
     * @throws 409 conflict when id not exist while update new user
     *             400 bad request if any request data missing from payload
     *             404 entity not found or id not exist in DP
     */
    @PutMapping
    @JsonView(View.BasicUser.class)
    public Mono<User> updateUser(@Valid @RequestBody UserDTO userDTO, @AuthenticationPrincipal SecurityUser securityUser) {
        if (securityCheck.checkIfAdminRole(securityUser))
            return Mono.just(userService.updateUser(userDTO));
        return Mono.empty();
    }

    /**
     * GET request to get user data by Id
     *
     * @param id user id
     * @return user data  or 404 not found in case id wrong
     */
    @GetMapping("/{id}")
    @JsonView(View.BasicUser.class)
    public Mono<User> findUserById(@PathVariable(name = "id") Long id) {
        return Mono.just(userService.findUserById(id));
    }

    /**
     * GET request to get all user data
     *
     * @return all user data
     */
    @GetMapping("/all")
    @JsonView(View.BasicUser.class)
    public Mono<List<User>> findAllUsers() {
        return Mono.just(userService.findAllUsers());
    }

    /**
     * GET request to filter users based on role
     *
     * @param role authority value oro role
     * @return return all users having the same role
     */
    @GetMapping("/role/{role}")
    @JsonView(View.BasicUser.class)
    public Mono<List<User>> findAllUsersByRole(@PathVariable(name = "role") String role) {
        return Mono.just(userService.findAllUsersByRole(role));
    }

    /**
     * PATCH request to upfdate role and disable / active user
     *
     * @param partiallyUpdateUser used for updating role and deactivate / activate user only
     * @param securityUser        attribute role and value for update user with new role
     * @return User updated data
     */
    @PatchMapping
    @JsonView(View.BasicUser.class)
    public Mono<User> partiallyUpdateUser(@Valid @RequestBody PartiallyUpdate partiallyUpdateUser, @AuthenticationPrincipal SecurityUser securityUser) {
        if (securityCheck.checkIfAdminRole(securityUser))
            return Mono.just(userService.partiallyUpdateUser(partiallyUpdateUser));
        return Mono.empty();
    }

    /**
     * DELETE request to remove user using id
     *
     * @param id           user id
     * @param securityUser
     * @return 200 in case of success deleting user
     */
    @DeleteMapping("/{id}")
    @JsonView(View.BasicUser.class)
    public Mono<Void> deleteById(@PathVariable(name = "id") Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        if (securityCheck.checkIfAdminRole(securityUser))
            userService.deleteById(id);
        return Mono.empty();
    }

}
