package banquemisr.challenge05.service.impl;

import banquemisr.challenge05.domain.Authority;
import banquemisr.challenge05.domain.User;
import banquemisr.challenge05.domain.dto.PartiallyUpdate;
import banquemisr.challenge05.domain.dto.UserDTO;
import banquemisr.challenge05.repository.UserRepository;
import banquemisr.challenge05.service.UserService;
import banquemisr.challenge05.util.Messages;
import banquemisr.challenge05.util.Validator;
import banquemisr.challenge05.util.errors.CustomParameterizedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private Validator validator;

    @Override
    public User createUser(UserDTO userDTO) {
        User user = new User();

        //validations
        if (null != userDTO.getId())
            throw new ConcurrencyFailureException(Messages.ID_VALUE);

        if (null == userDTO.getAuthoriy())
            throw new ConcurrencyFailureException(Messages.AUTHORITY_VALUE);

        if (validator.isValidPassword(userDTO.getPassword()))
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        else
            throw new CustomParameterizedException(Messages.PASSWORD_VALID);

        if (validator.isValidEmail(userDTO.getEmail()))
            user.setEmail(userDTO.getEmail());
        else
            throw new CustomParameterizedException(Messages.EMAIL_VALID);

        //create
        user.setActivated(true);
        user.setAuthorities(new HashSet<>(Set.of(new Authority(userDTO.getAuthoriy()))));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        return userRepository.save(user);
    }


    @Override
    public User updateUser(UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(userDTO.getId());
        User user = null;

        //validations
        if (userOptional.isEmpty())
            throw new EntityNotFoundException(Messages.ID_NOT_FOUND);
        else
            user = userOptional.get();

        if (null == userDTO.getId())
            throw new ConcurrencyFailureException(Messages.ID_VALUE);

        if (null != userDTO.getPassword())
            if (validator.isValidPassword(userDTO.getPassword()))
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            else
                throw new CustomParameterizedException(Messages.PASSWORD_VALID);

        if(null!=userDTO.getEmail())
            if (validator.isValidEmail(userDTO.getEmail()))
                user.setEmail(userDTO.getEmail());
            else
                throw new CustomParameterizedException(Messages.EMAIL_VALID);

        //update
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        //validations
        if (userOptional.isEmpty())
            throw new EntityNotFoundException(Messages.ID_NOT_FOUND);
        return userOptional.get();
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllUsersByRole(String role) {
        return userRepository.findByAuthoritiesName(role);
    }

    @Override
    public User partiallyUpdateUser(PartiallyUpdate partiallyUpdateUser) {
        Optional<User> userOptional = userRepository.findById(partiallyUpdateUser.getId());
        //validations
        if (userOptional.isEmpty())
            throw new EntityNotFoundException(Messages.ID_NOT_FOUND);

        User user = userOptional.get();
        if (partiallyUpdateUser.getAttribute().equalsIgnoreCase("role"))
            user.setAuthorities(new HashSet<>(Set.of(new Authority(partiallyUpdateUser.getValue()))));
        else if (partiallyUpdateUser.getAttribute().equalsIgnoreCase("activated")) {
            user.setActivated(Boolean.parseBoolean(partiallyUpdateUser.getValue().toLowerCase()));
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
         userRepository.deleteById(id);
    }
}
