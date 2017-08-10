package com.sample.springbootelasticsearch.repository;
import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.sample.springbootelasticsearch.model.Post;

public interface PostRepository extends ElasticsearchRepository<Post, String> {
	 List<Post> findByContentIgnoringCase(String content);
	 List<Post> findByContent(String content);
	 List<Post> findByContentOrderByTitleAsc(String content);
	 List<Post> findByTitleAndContentOrderByTitleAsc(String title,String content);
}
