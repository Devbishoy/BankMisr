package banquemisr.challenge05.service;

import banquemisr.challenge05.domain.User;
import banquemisr.challenge05.domain.dto.PartiallyUpdate;
import banquemisr.challenge05.domain.dto.UserDTO;

import java.util.List;

public interface UserService {

    User createUser(UserDTO userDTO);

    User updateUser(UserDTO userDTO);

    User findUserById(Long id);

    List<User> findAllUsers();

    List<User> findAllUsersByRole(String role);

     User partiallyUpdateUser(PartiallyUpdate partiallyUpdateUser);

     void deleteById(Long id);
}
