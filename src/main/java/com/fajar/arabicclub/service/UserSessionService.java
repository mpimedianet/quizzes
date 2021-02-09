package com.fajar.arabicclub.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fajar.arabicclub.config.security.JWTUtils;
import com.fajar.arabicclub.dto.WebResponse;
import com.fajar.arabicclub.entity.User;
import com.fajar.arabicclub.service.config.DefaultApplicationProfileService;
import com.fajar.arabicclub.util.StringUtil;

@Service
public class UserSessionService {

	@Autowired
	private JWTUtils jwtUtils;
	@Autowired
	private SessionValidationService sessionValidationService;
	@Autowired
	private DefaultApplicationProfileService defaultApplicationProfileService;
	
	public String generateJwt() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String jwt = jwtUtils.generateJwtToken(authentication);
			return jwt;
		} catch (Exception e) {
			return null;
		}
		
	}

	public WebResponse generateRequestId(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		User loggedUser = sessionValidationService.getLoggedUser(httpRequest);
		WebResponse response = new WebResponse();
		if (null != loggedUser) {
			response.setUser(loggedUser.toModel());
			response.setLoggedIn(true);
		}
		response.setApplicationProfile(defaultApplicationProfileService.getApplicationProfile().toModel());
		response.setRequestId(randomRequestId());
		return response;
	}

	private String randomRequestId() {
		return StringUtil.generateRandomNumber(15);
	}
}
