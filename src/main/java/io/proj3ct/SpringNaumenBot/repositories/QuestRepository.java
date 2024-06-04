package io.proj3ct.SpringNaumenBot.repositories;

import io.proj3ct.SpringNaumenBot.domains.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {
}
