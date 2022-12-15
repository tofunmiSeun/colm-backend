package com.tofunmi.mitri.webservice.post;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created By tofunmi on 15/07/2022
 */
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByParentPostId(String parentPostId, Sort sort);

    List<Post> findAllByAuthor(String author, Sort sort);

    Long countAllByAuthor(String author);
}
