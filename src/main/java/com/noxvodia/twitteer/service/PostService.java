package com.noxvodia.twitteer.service;

import org.springframework.stereotype.Service;

import com.noxvodia.twitteer.model.Post;
import com.noxvodia.twitteer.model.User;
import com.noxvodia.twitteer.repository.PostRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    private static final int MAX_CONTENT_LENGTH = 140;

    public Post createPost(UUID userId, String content) {
        System.out.println("Creating new post for user ID: " + userId);

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }

        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("Post content exceeds maximum length of " + MAX_CONTENT_LENGTH + " characters");
        }

        User user = userService.getUserById(userId);

        Post post = Post.builder()
                .content(content.trim())
                .user(user)
                .likesCount(0)
                .retweetsCount(0)
                .repliesCount(0)
                .isDeleted(false)
                .isEdited(false)
                .build();

        Post savedPost = postRepository.save(post);
        //userService.incrementPostsCount(userId);
        return savedPost;
    }

    public Post updatePost(UUID postId, String newContent) {
        System.out.println("Updating post with ID: " + postId);

        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }

        if (newContent.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("Post content exceeds maximum length of " + MAX_CONTENT_LENGTH + " characters");
        }

        Post post = getPostById(postId);

        if (!post.getContent().equals(newContent.trim())) {
            post.setContent(newContent.trim());
            post.setIsEdited(true);
        }

        return postRepository.save(post);
    }

    public Post getPostById(UUID postId) {
        System.out.println("Fetching post with ID: " + postId);
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));
    }

    public List<Post> getPostsByUserId(UUID userId) {
        System.out.println("Fetching posts for user ID: " + userId);
        User user = userService.getUserById(userId);
        return user.getPosts();
    }

    // public List<Post> getPostsByStreamId(UUID streamId) {
    //     System.out.println("Fetching posts for stream ID: " + streamId);
    //     Stream stream = streamService.getStreamById(streamId);
    //     return postRepository.findByStreamAndIsDeletedFalseOrderByCreatedAtDesc(stream);
    // }

    public void deletePost(UUID postId) {
        System.out.println("Soft deleting post with ID: " + postId);
        Post post = getPostById(postId);
        post.setIsDeleted(true);
        postRepository.save(post);
    }

    // public void permanentlyDeletePost(UUID postId) {
    //     System.out.println("Permanently deleting post with ID: " + postId);
    //     Post post = getPostById(postId);
    //     UUID streamId = post.getStream().getId();

    //     postRepository.deleteById(postId);
    //     streamService.decrementPostsCount(streamId);
    // }

    public void incrementLikesCount(UUID postId) {
        Post post = getPostById(postId);
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    public void decrementLikesCount(UUID postId) {
        Post post = getPostById(postId);
        post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
        postRepository.save(post);
    }

    public void incrementRetweetsCount(UUID postId) {
        Post post = getPostById(postId);
        post.setRetweetsCount(post.getRetweetsCount() + 1);
        postRepository.save(post);
    }

    public void decrementRetweetsCount(UUID postId) {
        Post post = getPostById(postId);
        post.setRetweetsCount(Math.max(0, post.getRetweetsCount() - 1));
        postRepository.save(post);
    }

    public void incrementRepliesCount(UUID postId) {
        Post post = getPostById(postId);
        post.setRepliesCount(post.getRepliesCount() + 1);
        postRepository.save(post);
    }

    public void decrementRepliesCount(UUID postId) {
        Post post = getPostById(postId);
        post.setRepliesCount(Math.max(0, post.getRepliesCount() - 1));
        postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}