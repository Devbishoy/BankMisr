package banquemisr.challenge05.service.impl;

import banquemisr.challenge05.domain.dto.JWTToken;
import banquemisr.challenge05.domain.dto.LoginUserDTO;
import banquemisr.challenge05.security.jwt.TokenProvider;
import banquemisr.challenge05.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class AuthorityImpl implements AuthorityService {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ReactiveAuthenticationManager manager;

    @Override
    public Mono<ResponseEntity<JWTToken>> creatToken(LoginUserDTO userLoginData, ServerHttpRequest request) {
        return tokenProvider.createToken(userLoginData, manager, request)
                .map(it ->
                        ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + it).body(new JWTToken(it)));
    }
}
