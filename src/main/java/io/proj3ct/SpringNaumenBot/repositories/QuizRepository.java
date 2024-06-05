package io.proj3ct.SpringNaumenBot.repositories;

import io.proj3ct.SpringNaumenBot.domains.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
