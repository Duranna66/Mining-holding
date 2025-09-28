package ru.holding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.holding.enums.Roles;

import java.io.Serializable;

@Table(name = "role")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "role_id", updatable = false, nullable = false)
    private Long roleID;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Roles roles;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "role_user_fk")
    )
    private User user;

    public Role(Roles roles) {
        this.roles = roles;
    }

}
