package minicurso.todolist.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data // bota automaticamente os getters e setters para todos os atributos
@Entity(name="tb_users") // criar tabela no BD
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;
    @Column(unique = true)
    private String username;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
