package banquemisr.challenge05.service;

import banquemisr.challenge05.domain.dto.JWTToken;
import banquemisr.challenge05.domain.dto.LoginUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface AuthorityService {
    Mono<ResponseEntity<JWTToken>> creatToken(LoginUserDTO userLoginData, ServerHttpRequest request);
}
