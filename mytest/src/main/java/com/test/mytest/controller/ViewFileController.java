package com.test.mytest.controller;

import com.test.mytest.config.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ViewFileController {

	private final JwtTokenUtil jwtTokenUtil;

	@Autowired
	public ViewFileController(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	/**
	 * webssocet 頁面 發現使用org.webjars 的套件會有一個info 不曉得怎麼處理所以 只攔截該畫面
	 */
	@GetMapping("/showWebSocketExample")
	public String showWebSocketExample(Model model, @RequestParam(name = "token", required = false) String token) {
		// Your existing logic here...

		// Pass the token as a model attribute to the Thymeleaf template
		model.addAttribute("token", token);
		System.out.println("token " + token);

		if (token != null) {
			// Extract account information from the token
			String account = jwtTokenUtil.getUsername(token);
			model.addAttribute("account", account);
		}

		return "webSocketExample"; // This should be the name of your Thymeleaf template file (without .html
		// extension)
	}

	/**
	 * 處理權限
	 */
	@GetMapping("/authorizationProcessingPage")
	private String authorization(Model model) {

		return "authorizationProcessingPage";
	}

	/**
	 * 處理登入
	 */
	@GetMapping("/loginPage")
	private String login(Model model) {
		return "loginPage";
	}

}