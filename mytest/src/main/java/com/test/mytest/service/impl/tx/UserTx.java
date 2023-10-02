package com.test.mytest.service.impl.tx;


import com.test.mytest.dao.UserRepository;
import com.test.mytest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
@Service
public class UserTx {

	@Autowired
	UserRepository userRepository;
	@Transactional(readOnly = true)
	public boolean existsByAccount(String account) {
		return userRepository.existsByAccount(account);
	}

	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public User findByAccountAndPassword(String account, String password) {
		return userRepository.findByAccountAndPassword(account, password);
	}
	@Transactional(readOnly = true)
	public User findByAccount(String account) {
		return userRepository.findByAccount(account);
	}
	@Transactional(readOnly = true)
	public User findByAccountWithLock(String account) {
		return userRepository.findByAccount(account);
	}
}