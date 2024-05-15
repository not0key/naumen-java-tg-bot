package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import domain.Quest;

public interface QuestRepository extends JpaRepository<Quest, Long> {
}