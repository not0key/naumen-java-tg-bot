package services;

import domain.Quest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.QuestRepository;


@Service
public class QuestService {
    private QuestRepository questRepository;
    @Autowired
    public QuestService(QuestRepository questRepository){
        this.questRepository=questRepository;
    }

    public void saveQuest(String quest_name, String answer, String[] wrong_answers){
        Quest quest = new Quest(quest_name, answer, wrong_answers);
        questRepository.save(quest);
    }
}
