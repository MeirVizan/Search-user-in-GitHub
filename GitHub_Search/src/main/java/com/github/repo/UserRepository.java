package com.github.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/* default scope of this Bean is "singleton" */

public interface UserRepository extends JpaRepository<User, Long> {


    /* add here the queries you need - in addition to CRUD operations */
    User findByUsername(String username);
    //List<User> findByFollowers(int follow);
    List<User> findFirst10ByOrderByCountDesc();
}

