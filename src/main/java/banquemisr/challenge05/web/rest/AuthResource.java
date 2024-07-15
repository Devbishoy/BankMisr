package banquemisr.challenge05.web.rest;

import banquemisr.challenge05.domain.dto.JWTToken;
import banquemisr.challenge05.domain.dto.LoginUserDTO;
import banquemisr.challenge05.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Bishoy Samir
 * @implNote missing logout impl , adding new roles  api rest
 */
@RestController
@RequestMapping("/authenticate")
public class AuthResource {


    @Autowired
    private AuthorityService authorityService;

    /**
     *
     * @param userLoginData  email and password  for login
     * @param request POST request
     * @return JWT security token
     */
    @PostMapping
    public Mono<ResponseEntity<JWTToken>> login(@RequestBody LoginUserDTO userLoginData, ServerHttpRequest request) {
        return authorityService.creatToken(userLoginData, request);
    }

    /**
     *
     * @return it should return NotImplementedException
     */
    @PostMapping("/logout")
    public Mono<Void> logout() {
        //TODO:implement logout in security config
        return Mono.empty();
    }

}
