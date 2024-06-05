package io.proj3ct.SpringNaumenBot.services;

import io.proj3ct.SpringNaumenBot.domains.User;
import io.proj3ct.SpringNaumenBot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId).orElse(null);
    }

    public User registerUser(Long chatId, String username) {
        User user = userRepository.findByChatId(chatId).orElse(null);
        if (user == null) {
            user = new User(username, chatId);
            userRepository.save(user);
        }
        return user;
    }
}
