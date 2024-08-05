package com.example.withdb.repository;


import com.example.withdb.entity.ERole;
import com.example.withdb.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(ERole Name);
}
