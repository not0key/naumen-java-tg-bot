package io.proj3ct.SpringNaumenBot.domains;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tasks")
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "v")
    private String quest_name;

    @Column(name = "answer")
    private String answer;

    @Column(name = "wrong_answers")
    private String[] wrong_answers;

    public Quest(String quest_name, String answer, String[] wrong_answers){
        this.quest_name = quest_name;
        this.answer = answer;
        this.wrong_answers = wrong_answers;
    }
}
