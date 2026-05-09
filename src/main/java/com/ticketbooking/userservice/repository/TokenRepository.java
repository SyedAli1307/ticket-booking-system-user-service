package com.ticketbooking.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketbooking.userservice.model.TokenModel;

import com.ticketbooking.userservice.model.UserModel;

@Repository
public interface TokenRepository extends JpaRepository<TokenModel, Integer> {
    void deleteByUser(UserModel user);
}
