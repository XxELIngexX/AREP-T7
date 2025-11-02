// package com.noxvodia.twitteer.repository;
// import com.noxvodia.twitteer.model.Stream;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.repository.JpaRepository;

// import org.springframework.stereotype.Repository;

// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// @Repository
// public interface StreamRepository extends JpaRepository<Stream, UUID> {
    
//     Optional<Stream> findByName(String name);
    
//     boolean existsByName(String name);
    
//     List<Stream> findByIsPublicTrue();
    
//     Page<Stream> findByIsPublicTrue(Pageable pageable);
    
// }