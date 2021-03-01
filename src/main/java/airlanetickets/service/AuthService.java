package airlanetickets.service;


import airlanetickets.model.User;

public interface AuthService {

    User login(String username, String password);

}
