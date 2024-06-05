package io.proj3ct.SpringNaumenBot.services;

import io.proj3ct.SpringNaumenBot.domains.User;
import io.proj3ct.SpringNaumenBot.domains.UserRating;
import io.proj3ct.SpringNaumenBot.repositories.UserRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRatingService {

    private final UserRatingRepository userRatingRepository;
    private final UserService userService;

    @Autowired
    public UserRatingService(UserRatingRepository userRatingRepository, UserService userService) {
        this.userRatingRepository = userRatingRepository;
        this.userService = userService;
    }

    public void updateUserRating(Long userId, int score) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        UserRating userRating = userRatingRepository.findByUser(user);
        if (userRating == null) {
            userRating = new UserRating();
            userRating.setUser(user);
        }
        userRating.setRating(userRating.getRating() + score);
        userRatingRepository.save(userRating);
    }

    public int getUserRating(Long userId) {
        User user = userService.getUserById(userId);
        UserRating userRating = userRatingRepository.findByUser(user);
        return userRating != null ? userRating.getRating() : 0;
    }

    public List<UserRating> getTopRatings(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return userRatingRepository.findTopRatings(pageable);
    }
}
