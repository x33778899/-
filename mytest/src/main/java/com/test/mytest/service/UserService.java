package com.test.mytest.service;


import com.test.mytest.model.User;

public interface UserService {

	boolean existsByAccount(String account);
	User save(User user);

	User findByAccountAndPassword(String account, String password);

	User findByAccount(String account);

	User findByAccountWithLock(String account);

}
