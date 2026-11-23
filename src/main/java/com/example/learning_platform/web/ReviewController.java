package com.example.learning_platform.web;

import com.example.learning_platform.dto.CreateReviewRequest;
import com.example.learning_platform.dto.RatingResponse;
import com.example.learning_platform.dto.ReviewResponse;
import com.example.learning_platform.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse add(@PathVariable Long courseId, @Valid @RequestBody CreateReviewRequest r) {
        return ReviewResponse.from(reviewService.addReview(courseId, r.studentId(), r.rating(), r.comment()));
    }

    @GetMapping("/reviews")
    public List<ReviewResponse> list(@PathVariable Long courseId) {
        return reviewService.getReviews(courseId);
    }

    @GetMapping("/rating")
    public RatingResponse rating(@PathVariable Long courseId) {
        return reviewService.getRating(courseId);
    }
}