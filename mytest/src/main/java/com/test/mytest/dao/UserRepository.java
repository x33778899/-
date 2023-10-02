package com.test.mytest.dao;


import com.test.mytest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByAccountAndPassword(String account, String password);
	User findByAccount(String account);
	boolean existsByAccount(String account);
}
