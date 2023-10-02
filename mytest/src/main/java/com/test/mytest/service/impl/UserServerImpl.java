package com.test.mytest.service.impl;


import com.test.mytest.model.User;
import com.test.mytest.service.UserService;
import com.test.mytest.service.impl.tx.UserTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // 添加這個標記
public class UserServerImpl implements UserService {

	@Autowired
	private UserTx userTx;

	@Override
	public boolean existsByAccount(String account) {
		return userTx.existsByAccount(account);
	}

	@Override
	public User save(User user) {
		User result = null;
		result = userTx.save(user);
		return result;
	}

	@Override
	public User findByAccountAndPassword(String account, String password) {
		User result = null;
		result = userTx.findByAccountAndPassword(account, password);
		return result;
	}

	@Override
	public User findByAccount(String account) {
		User result = null;
		result = userTx.findByAccount(account);
		return result;
	}

	@Override
	public User findByAccountWithLock(String account) {
		User result = null;
		result = userTx.findByAccountWithLock(account);
		return result;
	}
}
