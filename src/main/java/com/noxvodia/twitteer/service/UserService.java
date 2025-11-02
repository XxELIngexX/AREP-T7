package com.noxvodia.twitteer.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.noxvodia.twitteer.model.User;
import com.noxvodia.twitteer.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public User createUser(User user) {
        System.out.println("Creating new user with username: {}" + user.getUsername());
        
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        
        user.setFollowersCount(0);
        user.setFollowingCount(0);
        user.setIsVerified(false);
        user.setIsActive(true);
        
        return userRepository.save(user);
    }
    
    public User updateUser(UUID userId, User updatedUser) {
        System.out.println("Updating user with ID: {}"+ userId);
        
        User existingUser = getUserById(userId);
        
        if (updatedUser.getDisplayName() != null) {
            existingUser.setDisplayName(updatedUser.getDisplayName());
        }
        if (updatedUser.getBio() != null) {
            existingUser.setBio(updatedUser.getBio());
        }
        if (updatedUser.getProfileImageUrl() != null) {
            existingUser.setProfileImageUrl(updatedUser.getProfileImageUrl());
        }
        if (updatedUser.getBannerImageUrl() != null) {
            existingUser.setBannerImageUrl(updatedUser.getBannerImageUrl());
        }
        
        return userRepository.save(existingUser);
    }
    
    
    public User getUserById(UUID userId) {
        System.out.println("Fetching user with ID: {}"+ userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }
    

    public User getUserByEmail(String email) {
        System.out.println("Fetching user with email: {}"+ email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public void deleteUser(UUID userId) {
        System.out.println("Deleting user with ID: {}"+ userId);
        userRepository.deleteById(userId);
    }
    
    public void incrementFollowersCount(UUID userId) {
        User user = getUserById(userId);
        user.setFollowersCount(user.getFollowersCount() + 1);
        userRepository.save(user);
    }
    
    public void decrementFollowersCount(UUID userId) {
        User user = getUserById(userId);
        user.setFollowersCount(Math.max(0, user.getFollowersCount() - 1));
        userRepository.save(user);
    }
    
    public void incrementFollowingCount(UUID userId) {
        User user = getUserById(userId);
        user.setFollowingCount(user.getFollowingCount() + 1);
        userRepository.save(user);
    }
    
    public void decrementFollowingCount(UUID userId) {
        User user = getUserById(userId);
        user.setFollowingCount(Math.max(0, user.getFollowingCount() - 1));
        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

}
