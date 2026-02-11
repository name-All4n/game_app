package br.com.gameapp.GameApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Results(
        List<DadosJogo> results
) {
}
