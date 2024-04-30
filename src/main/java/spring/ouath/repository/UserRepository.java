package spring.ouath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ouath.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
