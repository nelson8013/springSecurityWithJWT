package io.getArrays.userservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.getArrays.userservice.Models.User;



public interface UserRepo extends JpaRepository<User, Long>{
  User  findByUsername(String username);
}  