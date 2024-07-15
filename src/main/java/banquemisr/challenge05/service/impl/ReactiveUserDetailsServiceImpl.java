package banquemisr.challenge05.service.impl;

import banquemisr.challenge05.repository.UserRepository;
import banquemisr.challenge05.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Environment environment;


    @Transactional
    @Override
    public Mono<UserDetails> findByUsername(String s) {
        return Optional.ofNullable(userRepository.findOneWithAuthoritiesByEmail(s))
            .map(it -> SecurityUser.from(it, Arrays.asList(environment.getActiveProfiles())))
            .map(Mono::just).orElse(Mono.empty()).cast(UserDetails.class);
    }

}
