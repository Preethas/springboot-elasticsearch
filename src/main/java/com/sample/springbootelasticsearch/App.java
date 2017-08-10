package com.sample.springbootelasticsearch;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.transaction.annotation.Transactional;
import static org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND;
import static org.elasticsearch.index.query.MatchQueryBuilder.Operator.OR;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

import com.sample.springbootelasticsearch.model.Post;
import com.sample.springbootelasticsearch.service.PostService;

@SpringBootApplication
public class App implements CommandLineRunner{
	@Autowired
	private PostService postSvc;
	
	 @Autowired
	 private ElasticsearchTemplate esTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
	private void loadData(){
		URL url = App.class.getClassLoader().
				getResource("com/sample/springbootelasticsearch/data");
		try (Stream<Path> paths = Files.walk(Paths.get(url.getFile()))) {
			//List<String> data = new ArrayList<String>();
		    List<String> list1 = paths
		        .map(path -> path.toString())
		        .collect(Collectors.toList());
		    
		    for (String fileName : list1){
		     if (!Files.isDirectory(Paths.get(fileName))) {
		      Stream<String> stream = Files.lines(Paths.get(fileName));

		      List<String> dataList = stream.collect(Collectors.toList());
              for (int i=0;i<dataList.size();i++){
            	String data = dataList.get(i);  
            	String[] tokens = data.split(":");
            	Post p = new Post(i+"",tokens[0],tokens[1],tokens[2]);
            	postSvc.save(p);
              }
			
		    }
		  }  
	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void seedRepository(){
		loadData();
	}
	
	/**
	 * Searches the content field in a case insensitive way
	 */
	void basicSearch(){
		List<Post> posts = postSvc.findByContentIgnoringCase("java");
		posts.forEach(post->{
			System.out.println(post);
		});
	}
	
	/**
	 * Searches the content field and returns the result ordering them by title
	 */
	void searchWithOrdering(){
		List<Post> posts = postSvc.findByContentOrderByTitleAsc("java");
		posts.forEach(post->{
			System.out.println(post);
		});
	}
	
	/**
	 * Searches the title and content field and orders them by title
	 */
	void searchMultipleFields(){
		List<Post> posts = postSvc.findByTitleAndContentOrderByTitleAsc("java","java");
		posts.forEach(post->{
			System.out.println(post);
		});
	}
	
	/**
	 * Fuzzy Search with edit distance 1
	 */
	void searchUsingFuzzyQuery(){
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				  .withQuery(matchQuery("title", "kaba")
				  .fuzziness(Fuzziness.TWO))
				  .build();
		esTemplate.queryForList(searchQuery, Post.class).forEach(System.out::println);
		
	}
	
	/**
	 * Searches the content field for words scripting or client 
	 * Returns the posts that have scripting or client in their content field
	 */
	void searchCombinationOfTerms(){
		// searches in the content for words scripting and client
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				  .withQuery(matchQuery("content", "scripting client")
				  )
				  .build();
		esTemplate.queryForList(searchQuery, Post.class).forEach(System.out::println);
	}
	
	/**
	 * Searches for words programming and language in the content field
	 */
	void searchUsingAnd(){
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				  .withQuery(matchQuery("content", "programming language").operator(AND)
				  )
				  .build();
		esTemplate.queryForList(searchQuery, Post.class).forEach(System.out::println);
	}
	
	/**
	 * Returns the posts that have syntax or notable in the content field
	 */
	void searchUsingOr(){
		// searches in the content for words scripting and client
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				  .withQuery(matchQuery("content", "syntax notable").operator(OR)
				  )
				  .build();
		esTemplate.queryForList(searchQuery, Post.class).forEach(System.out::println);
	}
	
	/**
	 * 
	 */
	void phraseSearch() {
		// Results in terms with start & programming which have a word inbetween
		// as the slop is 1
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				  .withQuery(matchPhraseQuery("content", "start programming").slop(1))
				  .build();
		esTemplate.queryForList(searchQuery, Post.class).forEach(System.out::println);
	}
	
	/**
	 * Searches for the word java in both title and content fields
	 */
	void searchUsingMultiMatchQuery(){
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				  .withQuery(multiMatchQuery("java","title","content"))
				  .build();
		esTemplate.queryForList(searchQuery, Post.class).forEach(System.out::println);
	}
	
	 @Override
	 @Transactional
	 public void run(String... strings) throws Exception {
		 esTemplate.deleteIndex(Post.class);
		 esTemplate.createIndex(Post.class);
	     esTemplate.putMapping(Post.class);
	     esTemplate.refresh(Post.class);
		 seedRepository();
		 basicSearch();
		 searchWithOrdering();
		 searchMultipleFields();
		 searchUsingFuzzyQuery();
		 searchCombinationOfTerms();
		 searchUsingAnd();
		 searchUsingOr();
		 phraseSearch();
		 searchUsingMultiMatchQuery();
	 }
}
