package banquemisr.challenge05.security;

import banquemisr.challenge05.util.AuthoritiesConstants;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class SecurityCheck {
    public boolean checkIfAdminRole(SecurityUser securityUser) {
        if (securityUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equalsIgnoreCase(AuthoritiesConstants.ADMIN)))
            throw new AccessDeniedException(" AccessDenied  ");
        return true;
    }

}
