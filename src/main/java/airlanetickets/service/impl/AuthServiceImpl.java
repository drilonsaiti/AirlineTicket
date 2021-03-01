package airlanetickets.service.impl;

import airlanetickets.model.User;
import airlanetickets.model.exceptions.InvalidArgumentsException;
import airlanetickets.model.exceptions.InvalidUserCredentialsException;
import airlanetickets.repository.UserRepository;

import airlanetickets.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByUsernameAndPassword(username,
                password).orElseThrow(InvalidUserCredentialsException::new);
    }

}
