package com.artkaba.artkaba.repository;

import com.artkaba.artkaba.entity.Oeuvre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OeuvreRepository extends JpaRepository<Oeuvre, Long> {
    
    // LA LIGNE MAGIQUE QUI MANQUE :
    List<Oeuvre> findByEtudiantId(Long etudiantId);
    
}