package com.artkaba.artkaba.repository;

import com.artkaba.artkaba.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    // Récupère les messages du plus récent au plus ancien
    List<Message> findByEtudiantIdOrderByDateEnvoiDesc(Long etudiantId);
}