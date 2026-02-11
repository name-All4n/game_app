package br.com.gameapp.GameApp;

import br.com.gameapp.GameApp.principal.main;
import br.com.gameapp.GameApp.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameAppApplication implements CommandLineRunner {

    @Autowired
    private JogoRepository repository;

    @Override
    public void run(String... args) throws Exception {
        main main = new main(repository);
        main.menu();
    }

    public static void main(String[] args) {
		SpringApplication.run(GameAppApplication.class, args);
	}

}
