package com.test.mytest.controller;


import com.test.mytest.config.AccountLockManager;
import com.test.mytest.config.JwtTokenUtil;
import com.test.mytest.config.RedisTokenRepository;
import com.test.mytest.dto.LoginResponse;
import com.test.mytest.model.Project;
import com.test.mytest.model.User;
import com.test.mytest.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private RedisTokenRepository redisTokenRepository;

	@Autowired
	public UserController(UserService userService, RedisTokenRepository redisTokenRepository) {
		this.userService = userService;
		this.redisTokenRepository = redisTokenRepository;
	}

	@PostMapping("/register")
	public ResponseEntity<LoginResponse> registerUser(@RequestBody User user) {
		// 創建一個響應對象
		LoginResponse response = new LoginResponse();

		try {
			// 設置用戶的角色、帳號和密碼
			user.setRole("ROLE_USER");
			user.setAccount(user.getAccount());
			user.setPassword(user.getPassword());

			// 創建新的專案並關聯到用戶
			Set<Project> projects = new HashSet<>();
			for (String projectName : user.getProjectNames()) {
				Project project = new Project();
				project.setName(projectName);
				project.setUser(user);
				projects.add(project);
			}

			user.setProjects(projects);

			userService.save(user); // 保存新用戶和關聯的專案

			// 設置成功的響應
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("註冊成功");
			response.setLoginSuccess(false);
			response.setToken(null);

			// 將插入的數據放入data屬性中
			Map<String, Object> data = new LinkedHashMap<>();
			data.put("user_id", user.getId()); // 假設你想插入用戶ID
			data.put("account", user.getAccount()); // 假設你想插入用戶帳號
			data.put("password", user.getPassword()); // 假設你想插入用戶密碼
			data.put("role", user.getRole()); // 假設你想插入用戶權限
			data.put("project_name", user.getProjectNames()); // 假設你想插入專案名稱
			response.setData(data);

			return ResponseEntity.ok(response);
		} catch (DataIntegrityViolationException e) {
			// 捕獲外鍵異常，處理相關邏輯
			e.printStackTrace();
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage("帳號已存在: ");
			response.setLoginSuccess(false);
			response.setToken(null);
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			// 處理其他異常
			e.printStackTrace();
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage("內部服務器錯誤");
			response.setLoginSuccess(false);
			response.setToken(null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}


	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody User user) {
		String account = user.getAccount();
		String password = user.getPassword();

		User userContainer = userService.findByAccountAndPassword(account, password);
		if (userContainer == null) {
			// 登錄失敗的處理邏輯
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setStatusCode(401);
			loginResponse.setMessage("登錄失敗: 無效的帳號或密碼。");
			loginResponse.setLoginSuccess(false);
			loginResponse.setToken(null); // 登錄失敗時，將User設為null
			return ResponseEntity.ok(loginResponse);
		} else {


			String token = jwtTokenUtil.createToken(user.getAccount(), "ROLE_USER", true);

			String tokenBody = jwtTokenUtil.getUsername(token);

			// 登錄成功的處理邏輯
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setStatusCode(200);
			loginResponse.setMessage("登錄成功");
			loginResponse.setLoginSuccess(true);
			loginResponse.setToken(token); // 登錄成功時，設置User屬性為登錄用戶

			// Redis
			redisTokenRepository.saveToken(token);

			return ResponseEntity.ok(loginResponse);
		}
	}

	/**
	 * 檢查token
	 * @param token
	 * @return
	 */
	@GetMapping("/gettoken")
	public ResponseEntity<String> getToken(@RequestParam String token) {
		boolean isTokenValid = redisTokenRepository.isTokenValid(token);

		if (isTokenValid) {
			// 从Redis中获取到了令牌，直接返回它
			return ResponseEntity.ok("true");
		} else {
			// 从Redis中未获取到令牌，尝试解析JWT令牌
			Claims claims = jwtTokenUtil.getTokenBody(token);

			if (claims != null) {
				// 解析成功，将令牌存储到Redis中，以便下次使用
				redisTokenRepository.saveToken(token);
				return ResponseEntity.ok(token);
			} else {
				// 解析失败，返回错误响应
				return ResponseEntity.badRequest().body("Invalid token");
			}
		}
	}


	/**
	 * 用來檢查帳號是否重複
	 *
	 */
	@GetMapping("/accountcheck")
	public ResponseEntity<LoginResponse> accountcheck(@RequestParam String account) {
		if (AccountLockManager.isAccountLocked(account)) {
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setStatusCode(403); // 403: 禁止訪問
			loginResponse.setMessage("帳號已鎖定");
			loginResponse.setLoginSuccess(false);
			loginResponse.setToken(null);
			return ResponseEntity.ok(loginResponse);
		} else {
			// 首先進行帳號檢查，如果存在則鎖定帳號
			User userContainer = userService.findByAccount(account);
			if (userContainer != null) {
				LoginResponse loginResponse = new LoginResponse();
				loginResponse.setStatusCode(409);
				loginResponse.setMessage("帳號已存在");
				loginResponse.setLoginSuccess(false);
				loginResponse.setToken(null);
				return ResponseEntity.ok(loginResponse);
			} else {
				AccountLockManager.lockAccount(account); // 鎖定帳號
				LoginResponse loginResponse = new LoginResponse();
				loginResponse.setStatusCode(200);
				loginResponse.setMessage("帳號尚未使用");
				loginResponse.setLoginSuccess(false);
				loginResponse.setToken(null);
				return ResponseEntity.ok(loginResponse);
			}
		}
	}

	@GetMapping("/getaccount")
	public ResponseEntity<LoginResponse> getAccount(@RequestParam String account) {
		User userContainer = userService.findByAccount(account);
		LoginResponse loginResponse = new LoginResponse();

		if (userContainer != null) {
			loginResponse.setStatusCode(HttpStatus.OK.value()); // 使用HttpStatus.OK
			loginResponse.setMessage("成功");
			loginResponse.setLoginSuccess(true);
			loginResponse.setToken(null);
			Map<String, Object> data = new LinkedHashMap<>();
			data.put("user_id", userContainer.getId()); // 假設你想插入用戶ID
			data.put("account", userContainer.getAccount()); // 假設你想插入用戶帳號
			data.put("password", userContainer.getPassword()); // 假設你想插入用戶密碼
			data.put("role", userContainer.getRole()); // 假設你想插入用戶權限
			data.put("project_name", userContainer.getProjectNames()); // 假設你想插入專案名稱
			loginResponse.setData(data);
		} else {
			loginResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 使用HttpStatus.NOT_FOUND
			loginResponse.setMessage("未找到用户");
			loginResponse.setLoginSuccess(false);
			loginResponse.setToken(null);
			loginResponse.setData(null);
		}

		return ResponseEntity.ok(loginResponse);
	}

}
