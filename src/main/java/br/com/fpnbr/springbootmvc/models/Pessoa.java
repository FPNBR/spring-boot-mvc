package br.com.fpnbr.springbootmvc.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SequenceGenerator(name = "seq_pessoa", sequenceName = "seq_pessoa", allocationSize = 1, initialValue = 1)
public class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pessoa")
    private Long id;

    @NotNull(message = "O nome não pode ser nulo!")
    @NotEmpty(message = "O nome não pode estar vazio!")
    private String nome;

    @NotNull(message = "O sobrenome não pode ser nulo!")
    @NotEmpty(message = "O sobrenome não pode estar vazio!")
    private String sobrenome;

    @NotNull(message = "A idade não pode ser nula!")
    @Min(value = 18, message = "Você deve ter mais de 18 anos para se cadastrar!")
    private int idade;

    private String cep;

    private String rua;

    private String bairro;

    private String cidade;

    private String uf;

    private String sexopessoa;

    private String ibge;

    @ManyToOne
    private Profissao profissao;

    @Enumerated(EnumType.STRING)
    private Senioridade senioridade;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @Lob
    private byte[] curriculo;

    @OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Telefone> telefones;
}
