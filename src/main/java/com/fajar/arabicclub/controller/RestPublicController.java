package com.fajar.arabicclub.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fajar.arabicclub.annotation.Authenticated;
import com.fajar.arabicclub.annotation.CustomRequestInfo;
import com.fajar.arabicclub.dto.WebRequest;
import com.fajar.arabicclub.dto.WebResponse;
import com.fajar.arabicclub.service.LogProxyFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/public")
public class RestPublicController extends BaseController {
 

	@PostConstruct
	public void init() {
		LogProxyFactory.setLoggers(this);
	}

	public RestPublicController() {
		log.info("----------------------Rest Public Controller-------------------");
	}

	 
	@PostMapping(value = "/requestid", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse getRequestId(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		
		log.info("generate or update requestId }");
		WebResponse response = userSessionService.generateRequestId(httpRequest, httpResponse);
		return response;
	}
	 

	
}
