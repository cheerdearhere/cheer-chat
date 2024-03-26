package com.cheer.cheerchat.repository;

import com.cheer.cheerchat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByEmail(String email);

    @Query("select u from User u where u.useYn='1' and u.fullName Like %:query% or u.email Like %:query%")
    public List<User> searchUser(String query);
}
