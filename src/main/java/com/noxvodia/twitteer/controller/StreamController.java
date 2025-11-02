// package com.noxvodia.twitteer.controller;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.noxvodia.twitteer.model.Stream;
// import com.noxvodia.twitteer.service.StreamService;

// import lombok.RequiredArgsConstructor;

// import java.util.List;
// import java.util.UUID;

// @RestController
// @RequestMapping("/api/streams")
// @RequiredArgsConstructor
// @CrossOrigin(origins = "*")
// public class StreamController {

//     private final StreamService streamService;

//     @PostMapping
//     public ResponseEntity<Stream> createStream(@RequestBody Stream stream) {
//         try {
//             Stream createdStream = streamService.createStream(stream);
//             return new ResponseEntity<>(createdStream, HttpStatus.CREATED);
//         } catch (IllegalArgumentException e) {
//             return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//         }
//     }

//     @GetMapping("/{streamId}")
//     public ResponseEntity<Stream> getStreamById(@PathVariable UUID streamId) {
//         try {
//             Stream stream = streamService.getStreamById(streamId);
//             return new ResponseEntity<>(stream, HttpStatus.OK);
//         } catch (IllegalArgumentException e) {
//             return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//         }
//     }

//     @GetMapping("/name/{name}")
//     public ResponseEntity<Stream> getStreamByName(@PathVariable String name) {
//         try {
//             Stream stream = streamService.getStreamByName(name);
//             return new ResponseEntity<>(stream, HttpStatus.OK);
//         } catch (IllegalArgumentException e) {
//             return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//         }
//     }

//     @GetMapping("/public")
//     public ResponseEntity<List<Stream>> getAllPublicStreams() {
//         List<Stream> streams = streamService.getAllPublicStreams();
//         return new ResponseEntity<>(streams, HttpStatus.OK);
//     }

//     @PutMapping("/{streamId}")
//     public ResponseEntity<Stream> updateStream(@PathVariable UUID streamId, @RequestBody Stream updatedStream) {
//         try {
//             Stream stream = streamService.updateStream(streamId, updatedStream);
//             return new ResponseEntity<>(stream, HttpStatus.OK);
//         } catch (IllegalArgumentException e) {
//             return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//         }
//     }

//     @DeleteMapping("/{streamId}")
//     public ResponseEntity<Void> deleteStream(@PathVariable UUID streamId) {
//         try {
//             streamService.deleteStream(streamId);
//             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//         } catch (IllegalArgumentException e) {
//             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//         }
//     }

//     @PostMapping("/{streamId}/posts/increment")
//     public ResponseEntity<Void> incrementPosts(@PathVariable UUID streamId) {
//         try {
//             streamService.incrementPostsCount(streamId);
//             return new ResponseEntity<>(HttpStatus.OK);
//         } catch (IllegalArgumentException e) {
//             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//         }
//     }

//     @PostMapping("/{streamId}/posts/decrement")
//     public ResponseEntity<Void> decrementPosts(@PathVariable UUID streamId) {
//         try {
//             streamService.decrementPostsCount(streamId);
//             return new ResponseEntity<>(HttpStatus.OK);
//         } catch (IllegalArgumentException e) {
//             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//         }
//     }
// }