package br.com.gameapp.GameApp.repository;

import br.com.gameapp.GameApp.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogoRepository extends JpaRepository<Jogo, Long> {
    Object findByNumIdentificacao( Integer identificacao );
}
