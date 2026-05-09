package com.ticketbooking.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketbooking.userservice.model.UserModel;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Integer>{
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);
}
