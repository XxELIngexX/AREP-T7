// package com.noxvodia.twitteer.post;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.noxvodia.twitteer.controller.PostController;
// import com.noxvodia.twitteer.model.*;
// import com.noxvodia.twitteer.service.PostService;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.UUID;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(PostController.class)
// class PostControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @MockitoBean
//     private PostService postService;

//     private User testUser;
//     private Stream testStream;
//     private Post testPost;
//     private UUID userId;
//     private UUID streamId;
//     private UUID postId;

//     @BeforeEach
//     void setUp() {
//         userId = UUID.randomUUID();
//         streamId = UUID.randomUUID();
//         postId = UUID.randomUUID();

//         testUser = User.builder()
//                 .username("testuser")
//                 .email("test@example.com")
//                 .build();

//         testStream = Stream.builder()
//                 .name("Test Stream")
//                 .isPublic(true)
//                 .build();

//         testPost = Post.builder()
//                 .content("This is a test post")
//                 .user(testUser)
//                 .stream(testStream)
//                 .likesCount(0)
//                 .retweetsCount(0)
//                 .repliesCount(0)
//                 .isDeleted(false)
//                 .isEdited(false)
//                 .build();
//     }

//     @Test
//     void createPost_Success() throws Exception {
//         Map<String, String> request = new HashMap<>();
//         request.put("userId", userId.toString());
//         request.put("streamId", streamId.toString());
//         request.put("content", "This is a test post");

//         when(postService.createPost(any(UUID.class), any(UUID.class), any(String.class)))
//                 .thenReturn(testPost);

//         mockMvc.perform(post("/api/posts")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.content").value("This is a test post"))
//                 .andExpect(jsonPath("$.likesCount").value(0))
//                 .andExpect(jsonPath("$.isDeleted").value(false));

//         verify(postService, times(1)).createPost(any(UUID.class), any(UUID.class), any(String.class));
//     }

//     @Test
//     void createPost_InvalidContent_ReturnsBadRequest() throws Exception {
//         Map<String, String> request = new HashMap<>();
//         request.put("userId", userId.toString());
//         request.put("streamId", streamId.toString());
//         request.put("content", "");

//         when(postService.createPost(any(UUID.class), any(UUID.class), any(String.class)))
//                 .thenThrow(new IllegalArgumentException("Post content cannot be empty"));

//         mockMvc.perform(post("/api/posts")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isBadRequest());

//         verify(postService, times(1)).createPost(any(UUID.class), any(UUID.class), any(String.class));
//     }

//     @Test
//     void getPostById_Success() throws Exception {
//         when(postService.getPostById(postId)).thenReturn(testPost);

//         mockMvc.perform(get("/api/posts/" + postId))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.content").value("This is a test post"));

//         verify(postService, times(1)).getPostById(postId);
//     }

//     @Test
//     void getPostById_NotFound_ReturnsNotFound() throws Exception {
//         when(postService.getPostById(postId))
//                 .thenThrow(new IllegalArgumentException("Post not found"));

//         mockMvc.perform(get("/api/posts/" + postId))
//                 .andExpect(status().isNotFound());

//         verify(postService, times(1)).getPostById(postId);
//     }

//     @Test
//     void getPostsByUserId_Success() throws Exception {
//         List<Post> posts = Arrays.asList(testPost);
//         when(postService.getPostsByUserId(userId)).thenReturn(posts);

//         mockMvc.perform(get("/api/posts/user/" + userId))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].content").value("This is a test post"));

//         verify(postService, times(1)).getPostsByUserId(userId);
//     }

//     @Test
//     void getPostsByStreamId_Success() throws Exception {
//         List<Post> posts = Arrays.asList(testPost);
//         when(postService.getPostsByStreamId(streamId)).thenReturn(posts);

//         mockMvc.perform(get("/api/posts/stream/" + streamId))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].content").value("This is a test post"));

//         verify(postService, times(1)).getPostsByStreamId(streamId);
//     }

//     @Test
//     void updatePost_Success() throws Exception {
//         Map<String, String> request = new HashMap<>();
//         request.put("content", "Updated content");

//         testPost.setContent("Updated content");
//         testPost.setIsEdited(true);

//         when(postService.updatePost(eq(postId), any(String.class))).thenReturn(testPost);

//         mockMvc.perform(put("/api/posts/" + postId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.content").value("Updated content"))
//                 .andExpect(jsonPath("$.isEdited").value(true));

//         verify(postService, times(1)).updatePost(eq(postId), any(String.class));
//     }

//     @Test
//     void deletePost_Success() throws Exception {
//         doNothing().when(postService).deletePost(postId);

//         mockMvc.perform(delete("/api/posts/" + postId))
//                 .andExpect(status().isNoContent());

//         verify(postService, times(1)).deletePost(postId);
//     }

//     @Test
//     void permanentlyDeletePost_Success() throws Exception {
//         doNothing().when(postService).permanentlyDeletePost(postId);

//         mockMvc.perform(delete("/api/posts/" + postId + "/permanent"))
//                 .andExpect(status().isNoContent());

//         verify(postService, times(1)).permanentlyDeletePost(postId);
//     }

//     @Test
//     void incrementLikes_Success() throws Exception {
//         doNothing().when(postService).incrementLikesCount(postId);

//         mockMvc.perform(post("/api/posts/" + postId + "/likes/increment"))
//                 .andExpect(status().isOk());

//         verify(postService, times(1)).incrementLikesCount(postId);
//     }

//     @Test
//     void decrementLikes_Success() throws Exception {
//         doNothing().when(postService).decrementLikesCount(postId);

//         mockMvc.perform(post("/api/posts/" + postId + "/likes/decrement"))
//                 .andExpect(status().isOk());

//         verify(postService, times(1)).decrementLikesCount(postId);
//     }

//     @Test
//     void incrementRetweets_Success() throws Exception {
//         doNothing().when(postService).incrementRetweetsCount(postId);

//         mockMvc.perform(post("/api/posts/" + postId + "/retweets/increment"))
//                 .andExpect(status().isOk());

//         verify(postService, times(1)).incrementRetweetsCount(postId);
//     }

//     @Test
//     void decrementRetweets_Success() throws Exception {
//         doNothing().when(postService).decrementRetweetsCount(postId);

//         mockMvc.perform(post("/api/posts/" + postId + "/retweets/decrement"))
//                 .andExpect(status().isOk());

//         verify(postService, times(1)).decrementRetweetsCount(postId);
//     }

//     @Test
//     void incrementReplies_Success() throws Exception {
//         doNothing().when(postService).incrementRepliesCount(postId);

//         mockMvc.perform(post("/api/posts/" + postId + "/replies/increment"))
//                 .andExpect(status().isOk());

//         verify(postService, times(1)).incrementRepliesCount(postId);
//     }

//     @Test
//     void decrementReplies_Success() throws Exception {
//         doNothing().when(postService).decrementRepliesCount(postId);

//         mockMvc.perform(post("/api/posts/" + postId + "/replies/decrement"))
//                 .andExpect(status().isOk());

//         verify(postService, times(1)).decrementRepliesCount(postId);
//     }
// }