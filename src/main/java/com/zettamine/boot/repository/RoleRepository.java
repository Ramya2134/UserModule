package com.zettamine.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zettamine.boot.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
