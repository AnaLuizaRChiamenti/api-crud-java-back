package br.com.apiNotes.apinotes.repositories;

import br.com.apiNotes.apinotes.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    User getReferenceByEmail(String email);
}
