package com.practice.repository;

import java.util.List;

import com.practice.model.Post;

public interface SearchRepository {
	List<Post> findByText(String text);

}
