package io.getArrays.userservice.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.getArrays.userservice.Models.Role;
import io.getArrays.userservice.Models.User;

import io.getArrays.userservice.Repository.RoleRepo;
import io.getArrays.userservice.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService{
 private final UserRepo userRepository;
 private final RoleRepo roleRepository;
 private final PasswordEncoder passwordEncoder;

 @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  User user = userRepository.findByUsername(username);
  if(user == null){
    log.error("User not found in DB");
    throw new UsernameNotFoundException("User not found in DB");
  }else{
    log.info("User found in DB");
  }

  Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

  user.getRoles().forEach(role -> {
    authorities.add(new SimpleGrantedAuthority(role.getName()));
  });

  return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
}

 @Override
 public User saveUser(User user) {
  try {

     log.info("Saving new user{} to DB", user.getName());
     user.setPassword(passwordEncoder.encode(user.getPassword()));
     return userRepository.save(user);

   } catch (Exception e) {
      log.error("Error occurred while saving user: {}", e.getMessage());
      throw e;
   }
 }

 @Override
 public Role saveRole(Role role) {
  try {

     log.info("Saving new role{} to DB", role.getName());
     return roleRepository.save(role);

  } catch (Exception e) {
    log.error("Error occurred while saving role: {}", e.getMessage());
    throw e;
  }
   
 }

 @Override
 public void addRoleToUser(String username, String roleName) {
   //Add validation to the username and roleName
   try{

     log.info("Adding role{} to user{}", roleName, username);
     User user = userRepository.findByUsername(username);
     Role role = roleRepository.findByName(roleName);
     user.getRoles().add(role);

   }catch(Exception e){
    log.error("Error occurred while add role to user: {}", e.getMessage());
    throw e;
   }
 }

 @Override
 public User getUser(String username) {
  try {

    log.info("Getting a single user: {}", username);
    return userRepository.findByUsername(username);

  } catch (Exception e) {
    log.error("Error occurred while getting a single user: {}", e.getMessage());
    throw e;
  }
  
 }

 @Override
 public List<User> getUsers() {
  try {

    log.info("Getting all users");
    return userRepository.findAll();

  } catch (Exception e) {
   log.error("Error occurred while getting users: {}", e.getMessage());
    throw e;
  }
   
 }


 
}
