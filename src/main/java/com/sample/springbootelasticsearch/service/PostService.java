package com.sample.springbootelasticsearch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.springbootelasticsearch.model.Post;
import com.sample.springbootelasticsearch.repository.PostRepository;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;
	
	public Post save(Post post){
		return postRepository.save(post);
	}
	
	public void deleteAll(){
		postRepository.deleteAll();
	}
	
	public List<Post> findByContentIgnoringCase(String content){
		return postRepository.findByContentIgnoringCase(content);
	}
	
	public List<Post> findByContent(String content){
		return postRepository.findByContent(content);
	}
	
	public List<Post> findByContentOrderByTitleAsc(String content){
		return postRepository.findByContentOrderByTitleAsc(content);
	}
	
	public List<Post> findByTitleAndContentOrderByTitleAsc(String title,String content){
		return postRepository.findByTitleAndContentOrderByTitleAsc(title,content);
	}

	public Iterable<Post> findAll() {
		// TODO Auto-generated method stub
		return postRepository.findAll();
	}
}
