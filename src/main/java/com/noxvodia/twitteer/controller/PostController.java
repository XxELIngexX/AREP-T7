package com.noxvodia.twitteer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.noxvodia.twitteer.model.Post;
import com.noxvodia.twitteer.model.User;
import com.noxvodia.twitteer.service.PostService;
import com.noxvodia.twitteer.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Map<String, String> request) {
        try {
            UUID userId = UUID.fromString(request.get("userId"));
            String content = request.get("content");

            Post createdPost = postService.createPost(userId, content);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // @GetMapping("/{postId}")
    // public ResponseEntity<Post> getPostById(@PathVariable UUID postId) {
    //     try {
    //         Post post = postService.getPostById(postId);
    //         return new ResponseEntity<>(post, HttpStatus.OK);
    //     } catch (IllegalArgumentException e) {
    //         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    //     }
    // }

    @GetMapping("/me")
    public List<Post> getMyPosts(OAuth2AuthenticationToken authToken) {
        String email = authToken.getPrincipal().getAttribute("email"); // O "sub" según tu token
        User user = userService.getUserByEmail(email);
        return postService.getPostsByUserId(user.getId());
    }

    // @GetMapping("/stream/{streamId}")
    // public ResponseEntity<List<Post>> getPostsByStreamId(@PathVariable UUID
    // streamId) {
    // try {
    // List<Post> posts = postService.getPostsByStreamId(streamId);
    // return new ResponseEntity<>(posts, HttpStatus.OK);
    // } catch (IllegalArgumentException e) {
    // return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    // }
    // }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable UUID postId, @RequestBody Map<String, String> request) {
        try {
            String newContent = request.get("content");
            Post updatedPost = postService.updatePost(postId, newContent);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID postId) {
        try {
            postService.deletePost(postId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // @DeleteMapping("/{postId}/permanent")
    // public ResponseEntity<Void> permanentlyDeletePost(@PathVariable UUID postId)
    // {
    // try {
    // postService.permanentlyDeletePost(postId);
    // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // } catch (IllegalArgumentException e) {
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }
    // }

    @PostMapping("/{postId}/likes/increment")
    public ResponseEntity<Void> incrementLikes(@PathVariable UUID postId) {
        try {
            postService.incrementLikesCount(postId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{postId}/likes/decrement")
    public ResponseEntity<Void> decrementLikes(@PathVariable UUID postId) {
        try {
            postService.decrementLikesCount(postId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{postId}/retweets/increment")
    public ResponseEntity<Void> incrementRetweets(@PathVariable UUID postId) {
        try {
            postService.incrementRetweetsCount(postId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{postId}/retweets/decrement")
    public ResponseEntity<Void> decrementRetweets(@PathVariable UUID postId) {
        try {
            postService.decrementRetweetsCount(postId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{postId}/replies/increment")
    public ResponseEntity<Void> incrementReplies(@PathVariable UUID postId) {
        try {
            postService.incrementRepliesCount(postId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{postId}/replies/decrement")
    public ResponseEntity<Void> decrementReplies(@PathVariable UUID postId) {
        try {
            postService.decrementRepliesCount(postId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}