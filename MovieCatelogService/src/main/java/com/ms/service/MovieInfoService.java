package com.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ms.model.CatelogItem;
import com.ms.model.Movie;
import com.ms.model.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class MovieInfoService {
	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod="getFallBackCatelogItem")
	public CatelogItem getCatelogItem(Rating rating) {
		
		Movie movie= restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
		return new CatelogItem(movie.getName(),movie.getDescription(),rating.getRating());	
	}
	
	
public CatelogItem getFallBackCatelogItem(Rating rating) {
		
		return new CatelogItem("Movie Name Not Found","",rating.getRating());	
	}

}
