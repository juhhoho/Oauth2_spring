package spring.ouath.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor
@Table(name= "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String name;
    private String email;
    private String role;

    @Builder
    public UserEntity(Long id, String username, String name, String email, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public void updateEmailAndName(String email, String name){
        this.email = email;
        this.name = name;
    }
}
