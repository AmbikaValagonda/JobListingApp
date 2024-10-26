package com.practice.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.practice.model.Post;

@Component
public class SearchRepositoryImpl implements SearchRepository {

	@Autowired
	MongoClient mongoClient;
	
	@Autowired
	MongoConverter converter;
	
	@Autowired
	Environment env;
	
	@Override
	public List<Post> findByText(String text) {
		
		final List<Post> posts = new ArrayList<>();
		//System.out.println("Inside findByText ... " + env.getProperty("spring.data.mongodb.database"));
		
		MongoDatabase database = mongoClient.getDatabase("telusko");
		MongoCollection<Document> collection = database.getCollection("JobPost");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search", 
		    new Document("text", 
		    new Document("query", text)
		                .append("path", Arrays.asList("techs", "desc")))), 
		    new Document("$sort", 
		    new Document("exp", 1L)), 
		    new Document("$limit", 5L)));
		
		result.forEach(doc -> System.out.println(doc));
		result.forEach(doc -> posts.add(converter.read(Post.class, doc)));
		//System.out.println(posts);
		return posts;
	}

}
