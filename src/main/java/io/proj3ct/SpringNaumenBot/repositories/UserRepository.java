package io.proj3ct.SpringNaumenBot.repositories;

import io.proj3ct.SpringNaumenBot.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);
}
