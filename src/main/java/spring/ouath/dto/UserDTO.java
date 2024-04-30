package spring.ouath.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String name;
    private String role;

    @Builder
    public UserDTO(String username, String name, String role) {
        this.username = username;
        this.name = name;
        this.role = role;
    }
}
