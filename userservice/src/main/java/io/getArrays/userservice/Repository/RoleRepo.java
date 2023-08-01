package io.getArrays.userservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.getArrays.userservice.Models.Role;

public interface RoleRepo extends JpaRepository<Role, Long>{
 Role findByName(String name);
}
