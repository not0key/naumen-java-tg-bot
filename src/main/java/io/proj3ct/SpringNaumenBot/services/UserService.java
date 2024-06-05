package io.proj3ct.SpringNaumenBot.services;

import io.proj3ct.SpringNaumenBot.domains.User;
import io.proj3ct.SpringNaumenBot.repositories.UserRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }
}
