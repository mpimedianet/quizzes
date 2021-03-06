package com.fajar.arabicclub.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fajar.arabicclub.annotation.Authenticated;
import com.fajar.arabicclub.annotation.CustomRequestInfo;
import com.fajar.arabicclub.dto.WebRequest;
import com.fajar.arabicclub.dto.WebResponse;
import com.fajar.arabicclub.service.LogProxyFactory;
import com.fajar.arabicclub.service.config.DefaultCategoriesService;
import com.fajar.arabicclub.service.config.DefaultUserService;
import com.fajar.arabicclub.service.publicmenus.GalleryService;
import com.fajar.arabicclub.service.publicmenus.LessonService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/public")
public class RestPublicController extends BaseController {
 
	@Autowired
	private DefaultCategoriesService defaultCategoriesService;
	@Autowired
	private DefaultUserService defaultUserService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private GalleryService galleryService;
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
	@PostMapping(value = "/categories/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse getCategories(@PathVariable(name = "code") String code, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		
		log.info("getCategories: {}", code);
		WebResponse response = defaultCategoriesService.getCategories(code);
		return response;
	}
	@PostMapping(value = "/lessons/{categoryCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse getLessons(@PathVariable(name = "categoryCode") String categoryCode, @RequestBody WebRequest webRequest) throws IOException {
		
		log.info("getLessons");
		WebResponse response = lessonService.getLessons(categoryCode, webRequest);
		return response;
	}
	
	@PostMapping(value = "/gallery/pictures", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse getPictures(@RequestBody WebRequest webRequest, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		 
		WebResponse response = galleryService.getPictures(webRequest);
		return response;
	}
	@PostMapping(value = "/gallery/videos", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CustomRequestInfo(withRealtimeProgress = true)
	public WebResponse getVideos(@RequestBody WebRequest webRequest, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		
		WebResponse response = galleryService.getVideos(webRequest, httpRequest);
		return response;
	}
	@PostMapping(value = "/gallery/documents", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse getDocuments(@RequestBody WebRequest webRequest, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		 
		WebResponse response = galleryService.getDocuments(webRequest);
		return response;
	}
	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse register(@RequestBody WebRequest webRequest, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		
		log.info("register }");
		WebResponse response = defaultUserService.register(webRequest);
		return response;
	}
	
}
