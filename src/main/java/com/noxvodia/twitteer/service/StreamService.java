// package com.noxvodia.twitteer.service;

// import org.springframework.stereotype.Service;

// import com.noxvodia.twitteer.model.Stream;
// import com.noxvodia.twitteer.repository.StreamRepository;

// import lombok.RequiredArgsConstructor;

// import java.util.List;
// import java.util.UUID;

// @RequiredArgsConstructor
// @Service
// public class StreamService {

//     private final StreamRepository streamRepository;

//     public Stream createStream(Stream stream) {
//         System.out.println("Creating new stream with name: " + stream.getName());

//         if (streamRepository.existsByName(stream.getName())) {
//             throw new IllegalArgumentException("Stream already exists with name: " + stream.getName());
//         }

//         stream.setPostsCount(0);

//         return streamRepository.save(stream);
//     }

//     public Stream updateStream(UUID streamId, Stream updatedStream) {
//         System.out.println("Updating stream with ID: " + streamId);

//         Stream existingStream = getStreamById(streamId);

//         if (updatedStream.getName() != null && !updatedStream.getName().equals(existingStream.getName())) {
//             if (streamRepository.existsByName(updatedStream.getName())) {
//                 throw new IllegalArgumentException("Stream name already exists: " + updatedStream.getName());
//             }
//             existingStream.setName(updatedStream.getName());
//         }

//         if (updatedStream.getDescription() != null) {
//             existingStream.setDescription(updatedStream.getDescription());
//         }

//         if (updatedStream.getIsPublic() != null) {
//             existingStream.setIsPublic(updatedStream.getIsPublic());
//         }

//         return streamRepository.save(existingStream);
//     }

//     public Stream getStreamById(UUID streamId) {
//         System.out.println("Fetching stream with ID: " + streamId);
//         return streamRepository.findById(streamId)
//                 .orElseThrow(() -> new IllegalArgumentException("Stream not found with ID: " + streamId));
//     }

//     public Stream getStreamByName(String name) {
//         System.out.println("Fetching stream with name: " + name);
//         return streamRepository.findByName(name)
//                 .orElseThrow(() -> new IllegalArgumentException("Stream not found with name: " + name));
//     }

//     public List<Stream> getAllPublicStreams() {
//         System.out.println("Fetching all public streams");
//         return streamRepository.findByIsPublicTrue();
//     }

//     public void deleteStream(UUID streamId) {
//         System.out.println("Deleting stream with ID: " + streamId);
//         streamRepository.deleteById(streamId);
//     }

//     public void incrementPostsCount(UUID streamId) {
//         Stream stream = getStreamById(streamId);
//         stream.setPostsCount(stream.getPostsCount() + 1);
//         streamRepository.save(stream);
//     }

//     public void decrementPostsCount(UUID streamId) {
//         Stream stream = getStreamById(streamId);
//         stream.setPostsCount(Math.max(0, stream.getPostsCount() - 1));
//         streamRepository.save(stream);
//     }
// }