package com.test.mytest.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AccountLockManager {

	private static final long LOCK_DURATION = 10000; // 鎖定時長：10秒
	private static final Map<String, Long> lockedAccounts = new HashMap<>(); // 鎖定帳號的映射

	/**
	 * 檢查帳號是否被鎖定
	 * 
	 * @param account 帳號
	 * @return 是否被鎖定
	 */
	public static boolean isAccountLocked(String account) {
		Long lockTime = lockedAccounts.get(account);
		if (lockTime == null) {
			return false;
		}
		System.out.println("lockedAccounts:" + lockedAccounts);
		long currentTime = System.currentTimeMillis();
		return currentTime - lockTime < LOCK_DURATION;
	}

	/**
	 * 鎖定帳號
	 * 
	 * @param account 帳號
	 */
	public static void lockAccount(String account) {
		lockedAccounts.put(account, System.currentTimeMillis());
		System.out.println("lockedAccounts	" + lockedAccounts);
		scheduleUnlock(account);
	}

	/**
	 * 定時解鎖帳號
	 * 
	 * @param account 帳號
	 */
	private static void scheduleUnlock(String account) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				unlockAccount(account);
			}
		}, LOCK_DURATION);
	}

	/**
	 * 解鎖帳號
	 * 
	 * @param account 帳號
	 */
	private static void unlockAccount(String account) {
		lockedAccounts.remove(account);
	}
}
