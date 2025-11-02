// package com.noxvodia.twitteer.post;

// import com.noxvodia.twitteer.model.*;
// import com.noxvodia.twitteer.service.*;

// import com.noxvodia.twitteer.repository.PostRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class PostServiceTest {

//     @Mock
//     private PostRepository postRepository;

//     @Mock
//     private UserService userService;

//     @Mock
//     private StreamService streamService;

//     @InjectMocks
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
//                 .displayName("Test User")
//                 .build();

//         testStream = Stream.builder()
//                 .name("Test Stream")
//                 .description("Test Description")
//                 .isPublic(true)
//                 .postsCount(0)
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
//     void createPost_Success() {
//         String content = "This is a test post";

//         when(userService.getUserById(userId)).thenReturn(testUser);
//         when(streamService.getStreamById(streamId)).thenReturn(testStream);
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);
//         doNothing().when(streamService).incrementPostsCount(streamId);

//         Post result = postService.createPost(userId, streamId, content);

//         assertNotNull(result);
//         assertEquals(content, result.getContent());
//         assertEquals(testUser, result.getUser());
//         assertEquals(testStream, result.getStream());
//         assertEquals(0, result.getLikesCount());
//         assertEquals(false, result.getIsDeleted());

//         verify(postRepository, times(1)).save(any(Post.class));
//         verify(streamService, times(1)).incrementPostsCount(streamId);
//     }

//     @Test
//     void createPost_EmptyContent_ThrowsException() {
//         String emptyContent = "";

//         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//             postService.createPost(userId, streamId, emptyContent);
//         });

//         assertEquals("Post content cannot be empty", exception.getMessage());
//         verify(postRepository, never()).save(any(Post.class));
//     }

//     @Test
//     void createPost_NullContent_ThrowsException() {
//         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//             postService.createPost(userId, streamId, null);
//         });

//         assertEquals("Post content cannot be empty", exception.getMessage());
//         verify(postRepository, never()).save(any(Post.class));
//     }

//     @Test
//     void createPost_ContentTooLong_ThrowsException() {
//         String longContent = "a".repeat(141);

//         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//             postService.createPost(userId, streamId, longContent);
//         });

//         assertEquals("Post content exceeds maximum length of 140 characters", exception.getMessage());
//         verify(postRepository, never()).save(any(Post.class));
//     }

//     @Test
//     void createPost_ContentExactly140Characters_Success() {
//         String exactContent = "a".repeat(140);

//         when(userService.getUserById(userId)).thenReturn(testUser);
//         when(streamService.getStreamById(streamId)).thenReturn(testStream);
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);
//         doNothing().when(streamService).incrementPostsCount(streamId);

//         Post result = postService.createPost(userId, streamId, exactContent);

//         assertNotNull(result);
//         verify(postRepository, times(1)).save(any(Post.class));
//     }

//     @Test
//     void updatePost_Success() {
//         String newContent = "Updated content";

//         when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);

//         Post result = postService.updatePost(postId, newContent);

//         assertNotNull(result);
//         assertTrue(result.getIsEdited());
//         verify(postRepository, times(1)).save(any(Post.class));
//     }

//     @Test
//     void updatePost_EmptyContent_ThrowsException() {
//         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//             postService.updatePost(postId, "");
//         });

//         assertEquals("Post content cannot be empty", exception.getMessage());
//     }

//     @Test
//     void updatePost_ContentTooLong_ThrowsException() {
//         String longContent = "a".repeat(141);

//         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//             postService.updatePost(postId, longContent);
//         });

//         assertEquals("Post content exceeds maximum length of 140 characters", exception.getMessage());
//     }

//     @Test
//     void updatePost_SameContent_DoesNotMarkAsEdited() {
//         String sameContent = "This is a test post";
//         testPost.setIsEdited(false);

//         when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);

//         Post result = postService.updatePost(postId, sameContent);

//         assertNotNull(result);
//         assertFalse(result.getIsEdited());
//     }

//     @Test
//     void getPostById_Success() {
//         when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));

//         Post result = postService.getPostById(postId);

//         assertNotNull(result);
//         assertEquals(testPost, result);
//         verify(postRepository, times(1)).findById(postId);
//     }

//     @Test
//     void getPostById_NotFound_ThrowsException() {
//         when(postRepository.findById(postId)).thenReturn(Optional.empty());

//         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//             postService.getPostById(postId);
//         });

//         assertEquals("Post not found with ID: " + postId, exception.getMessage());
//     }

//     @Test
//     void getPostsByUserId_Success() {
//         List<Post> posts = Arrays.asList(testPost);

//         when(userService.getUserById(userId)).thenReturn(testUser);
//         when(postRepository.findByUserAndIsDeletedFalseOrderByCreatedAtDesc(testUser)).thenReturn(posts);

//         List<Post> result = postService.getPostsByUserId(userId);

//         assertNotNull(result);
//         assertEquals(1, result.size());
//         assertEquals(testPost, result.get(0));
//         verify(postRepository, times(1)).findByUserAndIsDeletedFalseOrderByCreatedAtDesc(testUser);
//     }

//     @Test
//     void getPostsByStreamId_Success() {
//         List<Post> posts = Arrays.asList(testPost);

//         when(streamService.getStreamById(streamId)).thenReturn(testStream);
//         when(postRepository.findByStreamAndIsDeletedFalseOrderByCreatedAtDesc(testStream)).thenReturn(posts);

//         List<Post> result = postService.getPostsByStreamId(streamId);

//         assertNotNull(result);
//         assertEquals(1, result.size());
//         assertEquals(testPost, result.get(0));
//         verify(postRepository, times(1)).findByStreamAndIsDeletedFalseOrderByCreatedAtDesc(testStream);
//     }


//     @Test
//     void incrementLikesCount_Success() {
//         testPost.setLikesCount(5);

//         when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);

//         postService.incrementLikesCount(postId);

//         verify(postRepository, times(1)).save(testPost);
//     }

//     @Test
//     void decrementLikesCount_Success() {
//         testPost.setLikesCount(5);

//         when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);

//         postService.decrementLikesCount(postId);

//         verify(postRepository, times(1)).save(testPost);
//     }

//     @Test
//     void decrementLikesCount_DoesNotGoBelowZero() {
//         testPost.setLikesCount(0);

//         when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);

//         postService.decrementLikesCount(postId);

//         verify(postRepository, times(1)).save(testPost);
//     }

//     @Test
//     void incrementRetweetsCount_Success() {
//         testPost.setRetweetsCount(3);

//         when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);

//         postService.incrementRetweetsCount(postId);

//         verify(postRepository, times(1)).save(testPost);
//     }

//     @Test
//     void decrementRetweetsCount_Success() {
//         testPost.setRetweetsCount(3);

//         when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);

//         postService.decrementRetweetsCount(postId);

//         verify(postRepository, times(1)).save(testPost);
//     }

//     @Test
//     void incrementRepliesCount_Success() {
//         testPost.setRepliesCount(2);

//         when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);

//         postService.incrementRepliesCount(postId);

//         verify(postRepository, times(1)).save(testPost);
//     }

//     @Test
//     void decrementRepliesCount_Success() {
//         testPost.setRepliesCount(2);

//         when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
//         when(postRepository.save(any(Post.class))).thenReturn(testPost);

//         postService.decrementRepliesCount(postId);

//         verify(postRepository, times(1)).save(testPost);
//     }
// }