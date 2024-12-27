package com.zettamine.boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zettamine.boot.entity.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User>findByEmail(String email);
	
}
