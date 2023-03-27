package br.com.fpnbr.springbootmvc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@SequenceGenerator(name = "seq_role", sequenceName = "seq_role", allocationSize = 1, initialValue = 1)
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
    private Long id;

    @Getter
    @Setter
    private String nameRole;

    @Override
    public String getAuthority() {
        return this.nameRole;
    }
}
