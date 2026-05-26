package com.artkaba.artkaba.repository;

import com.artkaba.artkaba.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByEmail(String email); // <--- CETTE LIGNE EST OBLIGATOIRE
    Optional<Etudiant> findByMatricule(String matricule); // <--- OPTIONNEL, MAIS PRATIQUE SI TU VEUX RECHERCHER PAR MATRICULE
    boolean existsByEmail(String email);

    
}