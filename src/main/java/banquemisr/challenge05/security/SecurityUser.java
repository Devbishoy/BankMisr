package banquemisr.challenge05.security;

import banquemisr.challenge05.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.ZonedDateTime;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SecurityUser implements UserDetails {
    private Long id;
    private String password;
    private String username;
    private Long tokenValidityInSeconds;
    private Collection<BaseGrantedAuthority> authorities;
    private boolean enabled = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonExpired = true;

    public SecurityUser() {
    }

    public SecurityUser(String username, String password, Collection<BaseGrantedAuthority> authorities, Long id, Long tokenValidityInSeconds) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }

    public SecurityUser(String username, String password, Collection<BaseGrantedAuthority> authorities, Long id) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static SecurityUser from(User user, List<String> profiles) {
        List<BaseGrantedAuthority> grantedAuthorities =
                user.getAuthorities().stream().map(authority -> new BaseGrantedAuthority(authority.getName())).collect(Collectors.toList());
        SecurityUser securityUser = new SecurityUser(user.getEmail(), user.getPassword(), grantedAuthorities,
                user.getId());
        securityUser.setEnabled(user.isActivated());
        boolean accountLockedByFailedTries =
                user.getFailedTriesAttempts() > 3 && user.getLastFailedLogin() != null && ZonedDateTime.now().minusMinutes(15).isBefore(user.getLastFailedLogin());
        securityUser.setAccountNonLocked(user.isActivated() && !accountLockedByFailedTries);
        return securityUser;
    }
}
