package br.com.gameapp.GameApp.repository;

import br.com.gameapp.GameApp.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JogoRepository extends JpaRepository<Jogo, Long> {
    Object findByNumIdentificacao( Integer identificacao );

    Optional<Jogo> findByIdentificationNumber(int numIdentificacao);
}
