package com.example.learning_platform.repository;
import com.example.learning_platform.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {}