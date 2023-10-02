package com.test.mytest.config;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenRepository {

	private final RedisTemplate<String, Object> redisTemplate;
	private final JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.token.expiration-seconds}") // 从配置文件中读取过期时间
	private long expirationInSeconds;

	public RedisTokenRepository(RedisTemplate<String, Object> redisTemplate, JwtTokenUtil jwtTokenUtil) {
		this.redisTemplate = redisTemplate;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	/**
	 * 添加前缀以生成 Redis Key
	 */
	private String generateRedisKey(String subject) {
		return "user:" + subject;
	}

	/**
	 * 检查令牌是否有效
	 */
	public boolean isTokenValid(String token) {
		String subject = jwtTokenUtil.getUsername(token);
		if (subject != null) {
			String redisKey = generateRedisKey(subject);
			String storedToken = (String) redisTemplate.opsForValue().get(redisKey);
			return storedToken != null && storedToken.equals(token);
		} else {
			return false;
		}
	}

	/**
	 * 保存令牌到 Redis
	 */
	public void saveToken(String token) {
		String subject = jwtTokenUtil.getUsername(token);
		if (subject != null) {
			String redisKey = generateRedisKey(subject);
			redisTemplate.opsForValue().set(redisKey, token, expirationInSeconds, TimeUnit.SECONDS);
		} else {
			throw new IllegalArgumentException("Invalid JWT token");
		}
	}
}
