package io.getArrays.userservice.Service;

import java.util.List;

import io.getArrays.userservice.Models.Role;
import io.getArrays.userservice.Models.User;

public interface UserService {
 User saveUser(User user);
 Role saveRole(Role role);
 void addRoleToUser(String username,String roleName);
 User getUser(String username);
 List<User> getUsers();
}
 