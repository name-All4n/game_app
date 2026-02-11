package br.com.gameapp.GameApp.model;

import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "biblioteca de jogos")
public class Jogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String genero;
    private Integer ano;
    private Double nota;
    private Integer numIdentificacao;

    public Jogo() {}

    public Jogo(DadosJogo dadosJogo) {
        this.nome = dadosJogo.nome();
        this.genero = dadosJogo.genero();
        this.ano = Optional.ofNullable(dadosJogo.ano())
                .map(Double::intValue).orElse(null);
        this.nota = Optional.ofNullable(dadosJogo.nota())
                .map(DadosRating::mean)
                .orElse(0.0);
        this.numIdentificacao = dadosJogo.numIdentificacao();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Integer getNumIdentificacao() {
        return numIdentificacao;
    }

    public void setNumIdentificacao(Integer numIdentificacao) {
        this.numIdentificacao = numIdentificacao;
    }

    @Override
    public String toString() {
        return "Jogo: " + nome +
                " | Gênero: " + genero +
                " | Ano: " + ano +
                " | Nota: " + nota +
                " | ID: " + numIdentificacao;
    }
}
