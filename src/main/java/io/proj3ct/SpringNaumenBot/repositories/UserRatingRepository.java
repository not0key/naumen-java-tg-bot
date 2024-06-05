package io.proj3ct.SpringNaumenBot.repositories;

import io.proj3ct.SpringNaumenBot.domains.User;
import io.proj3ct.SpringNaumenBot.domains.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

    UserRating findByUser(User user);

    @Query("SELECT ur FROM UserRating ur ORDER BY ur.rating DESC")
    List<UserRating> findTopRatings(org.springframework.data.domain.Pageable pageable);
}
