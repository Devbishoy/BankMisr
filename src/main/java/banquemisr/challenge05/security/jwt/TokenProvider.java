package banquemisr.challenge05.security.jwt;

import banquemisr.challenge05.config.BankProperties;
import banquemisr.challenge05.domain.dto.LoginUserDTO;
import banquemisr.challenge05.repository.UserRepository;
import banquemisr.challenge05.security.SecurityUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Collectors;
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private final BankProperties bankProperties;
    private String secretKey;
    private long tokenValidityInMilliseconds;
    private long tokenValidityInMillisecondsForRememberMe;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Environment environment;
    @Autowired
    private UserRepository userRepository;


    public TokenProvider(BankProperties bankProperties) {
        this.bankProperties = bankProperties;
    }

    @PostConstruct
    public void init() {
        this.secretKey = bankProperties.getSecurity().getAuthentication().getJwt().getSecret();

        this.tokenValidityInMilliseconds =
                1000 * bankProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
                1000 * bankProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
    }

    public Mono<String> createToken(LoginUserDTO userLoginData, ReactiveAuthenticationManager manager, ServerHttpRequest request) {
        return authenticateUser(userLoginData, manager, request).map(it -> createToken(it, userLoginData.getRememberMe()));
    }

    public String createToken(Authentication authentication, Boolean rememberMe) {
        String authorities =
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(
                        ","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe != null && rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        JwtBuilder claim =
                Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities).claim("user",
                        authentication.getPrincipal());
        return claim.signWith(SignatureAlgorithm.HS256, secretKey).setExpiration(validity).compact();
    }

    public Mono<Authentication> getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            SecurityUser user = objectMapper.convertValue(claims.get("user"), SecurityUser.class);
            return Mono.just(new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities()));
        } catch (Exception e) {
            return Mono.empty();
        }
    }

    public boolean validateToken(String authToken) {
        try {
            if (authToken != null && !authToken.equals("undefined")) {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
                return true;
            }
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.error("Invalid JWT signature trace: {}",e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.error("Invalid JWT token trace: {}",e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token. " + e.getMessage());
            log.error("Expired JWT token trace: {}",e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.error("Unsupported JWT token trace: {}",e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.error("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    public Mono<Authentication> authenticateUser(LoginUserDTO userData, ReactiveAuthenticationManager manager,
                                                 ServerHttpRequest request) {
        return this.authenticateUser(userData.getEmail(), userData.getPassword(), manager, request);
    }

    public Mono<Authentication> authenticateUser(String email, String password,
                                                 ReactiveAuthenticationManager manager, ServerHttpRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return manager.authenticate(authenticationToken).doOnSuccess(authentication -> resetTriesAttempts(email, request)).doOnError(throwable -> incrementTriesAttempts(email, request));
    }


    @Transactional
    public void resetTriesAttempts(String email, ServerHttpRequest request) {
        userRepository.resetFailedTriesAttempts(email);
    }

    @Transactional
    public void incrementTriesAttempts(String email, ServerHttpRequest request) {
        userRepository.incrementTriesAttempts(email, ZonedDateTime.now());
    }

}
