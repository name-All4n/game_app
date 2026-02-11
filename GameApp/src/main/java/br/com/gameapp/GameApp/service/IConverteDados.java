package br.com.gameapp.GameApp.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
