package br.com.gameapp.GameApp.principal;

import br.com.gameapp.GameApp.model.Jogo;
import br.com.gameapp.GameApp.model.Results;
import br.com.gameapp.GameApp.repository.JogoRepository;
import br.com.gameapp.GameApp.service.ConsumoAPI;
import br.com.gameapp.GameApp.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class main {
    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://api.gamebrain.co/v1/games?query=";
    private final String API_KEY = "&api-key="+System.getenv("APY_KEY_GAMEBRAIN");
    private JogoRepository repository;

    public main(JogoRepository repository) {
        this.repository = repository;
    }

    public void menu() {
        var option = -1;

        while (option != 0) {
            var menu = """
                    1 - Buscar jogo 
                    2 - Listar jogos da biblioteca
                    3 - Buscar jogo similar
                 
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    searchGame();
                    break;
                case 2:
                    listGames();
                    break;
                case 3:
                    searchSimilarGames();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private Results getGamesData() {
        System.out.println("Digite o nome do jogo: ");
        String nomeJogo = sc.nextLine();
        var json = consumo
                .obterDados(ENDERECO + nomeJogo
                        .replace(" ", "+")
                        + API_KEY);
        Results dados = conversor.obterDados(json, Results.class);
        return dados;
    }

    private void searchGame() {
        Results dados = getGamesData();
        List<Jogo> gameList = dados.results().stream()
                .map(dadosJogo -> new Jogo(dadosJogo))
                .collect(Collectors.toList());
        System.out.println("\n--- Jogos Encontrados ---");
        gameList.forEach(System.out::println);
        System.out.println("Gostaria de adiconar algum desses jogos a sua biblioteca? (S/N)");
        var response = sc.nextLine();
        if (response.equalsIgnoreCase("S")) {
            System.out.println("Digite o ID do Jogo: ");
            var id = sc.nextInt();
            var jogoEncontrado = gameList.stream()
                    .filter(j -> j.getNumIdentificacao()
                            .equals(id))
                    .findFirst()
                    .orElse(null);
            var jogoBanco = repository.findByNumIdentificacao(id);
            if (jogoEncontrado != null && jogoBanco == null) {
                repository.save(jogoEncontrado);
            }
        }
    }

    private void listGames() {
        List<Jogo> jogos = repository.findAll();
        jogos.stream().forEach(System.out::println);
    }

    private void searchSimilarGames() {
        List<Jogo> jogos = repository.findAll();
        List<Jogo> similarGames = new ArrayList<>();
        for  (Jogo jogo : jogos) {
            var id = jogo.getNumIdentificacao();
            var json = consumo
                    .obterDados("https://api.gamebrain.co/v1/games/" + id + "/similar?limit=3&api-key=" + System.getenv("APY_KEY_GAMEBRAIN"));
            Results dados = conversor.obterDados(json, Results.class);
            List<Jogo> listaJogos = dados.results().stream()
                    .map(dadosJogo -> new Jogo(dadosJogo))
                    .collect(Collectors.toList());
            for (Jogo jogoSimilar : listaJogos) {
                similarGames.add(jogoSimilar);
            }
        }
        similarGames.forEach(System.out::println);
        System.out.println("Gostaria de adiconar algum desses jogos a sua biblioteca? (S/N)");
        var response = sc.nextLine();
        if (response.equalsIgnoreCase("S")) {
            System.out.println("Digite o ID do Jogo: ");
            var id = sc.nextInt();
            var jogoEncontrado = similarGames.stream()
                    .filter(j -> j.getNumIdentificacao()
                            .equals(id))
                    .findFirst()
                    .orElse(null);
            var jogoBanco = repository.findByNumIdentificacao(id);
            if (jogoEncontrado != null && jogoBanco == null) {
                repository.save(jogoEncontrado);
            }
        }
    }
}
