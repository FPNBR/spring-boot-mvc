package br.com.fpnbr.springbootmvc.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum Senioridade {
    JUNIOR("Júnior"),
    PLENO("Pleno"),
    SENIOR("Sênior");

    @Getter
    @Setter
    private String descricao;

    @Override
    public String toString() {
        return this.name();
    }
}
