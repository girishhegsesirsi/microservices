package com.ms.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.ms.model.CatelogItem;
import com.ms.model.Movie;
import com.ms.model.Rating;
import com.ms.model.UserRating;
import com.ms.service.MovieInfoService;
import com.ms.service.UserRatingInfoService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/catelog")
public class MovieCatelogResource {
	

	/*
	 * @Autowired private WebClient.Builder webClientBuilder;
	 */
	
	@Autowired
	MovieInfoService movieInfoService;
	
	@Autowired
	UserRatingInfoService userRatingInfoService;
	
	/*@RequestMapping("/{userId}")
	@HystrixCommand(fallbackMethod="getFallBackCatelog")
	public List<CatelogItem> getCatelog(@PathVariable("userId")String userId){
		
		
		//List<Rating> ratings = restTemplate.getForObject("http://localhost:8083/users/"+userId, ParameterizedType<List<Rating>>);
		UserRating ratings = restTemplate.getForObject("http://rating-data-service/ratingsData/users/"+userId, UserRating.class);
		
		return ratings.getUserRating().stream().map(rating ->{
			
			//reactive way of calling rest API
			Movie movie1=	webClientBuilder.build()
			.get()
			.uri("http://localhost:8082/movies/"+rating.getMovieId())
			.retrieve()
			.bodyToMono(Movie.class)
			.block();
		System.out.println("Movie received from reactive rest API call: "+movie1);
		
			// Legacy way of calling rest API
		Movie movie= restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
	return new CatelogItem(movie.getName(),movie.getDescription(),rating.getRating());
		})
		.collect(Collectors.toList());
	}*/
/*public List<CatelogItem> getFallBackCatelog(@PathVariable("userId")String userId){
	
	return Arrays.asList(new CatelogItem("No Movie","",0));
}*/

	@RequestMapping("/{userId}")
	public List<CatelogItem> getCatelog(@PathVariable("userId")String userId){

		UserRating ratings = userRatingInfoService.getUserRating(userId);
		
		return ratings.getUserRating().stream().map(rating ->movieInfoService.getCatelogItem(rating))
		.collect(Collectors.toList());
	}
		
}
