package br.com.fpnbr.springbootmvc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "seq_profissao", sequenceName = "seq_profissao", allocationSize = 1, initialValue = 1)
public class Profissao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_profissao")
    private Long id;

    private String nome;
}
