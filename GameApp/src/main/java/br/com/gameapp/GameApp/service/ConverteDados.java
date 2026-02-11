package br.com.gameapp.GameApp.service;

import org.springframework.boot.json.JsonParseException;
import tools.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados{
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        }catch (JsonParseException e){
            throw new RuntimeException(e);
        }
    }
}
