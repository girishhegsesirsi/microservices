package com.ms.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ms.model.Rating;
import com.ms.model.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class UserRatingInfoService {
	
	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod="getFallBackUserRating")
	public UserRating getUserRating(String userId) {
		return restTemplate.getForObject("http://rating-data-service/ratingsData/users/"+userId, UserRating.class);	
	}
	
	
	
	
	
	public UserRating getFallBackUserRating(String userId) {
		UserRating userRating= new UserRating();
		userRating.setUserRating(Arrays.asList(new Rating("0",0)));
		return userRating;
	}
	
}
