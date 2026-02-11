package br.com.gameapp.GameApp.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosJogo(
        @JsonAlias("name") String nome,
        @JsonAlias("genre") String genero,
        @JsonAlias("year") Double ano,
        @JsonAlias("rating") DadosRating nota,
        @JsonAlias("id") Integer numIdentificacao
) {
}
