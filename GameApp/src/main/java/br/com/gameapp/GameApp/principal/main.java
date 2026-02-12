package br.com.gameapp.GameApp.principal;

import br.com.gameapp.GameApp.model.Jogo;
import br.com.gameapp.GameApp.model.Results;
import br.com.gameapp.GameApp.repository.JogoRepository;
import br.com.gameapp.GameApp.service.ConsumoAPI;
import br.com.gameapp.GameApp.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                    3 - Remover jogo da biblioteca
                    4 - Buscar jogo similar
                 
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
                    removeGame();
                    break;
                case 4:
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

    private void removeGame() {
        System.out.println("====Jogos na sua biblioteca atualmente====");
        listGames();
        System.out.println("Qual o id do jogo deseja remover?");
        var numIdentificacao = sc.nextInt();
        Optional<Jogo> jogoDelete = repository.findByNumIdentificacao(numIdentificacao);

        if (jogoDelete.isPresent()) {
            repository.delete(jogoDelete.get());
            System.out.println("Jogo removido!");
        } else {
            System.out.println("Jogo não encontrado na sua biblioteca.");
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
            if (sc.hasNextInt()) {
                int id = sc.nextInt();
                sc.nextLine();

                var jogoEncontrado = gameList.stream()
                        .filter(j -> j.getNumIdentificacao()
                                .equals(id))
                        .findFirst();

                if (jogoEncontrado.isPresent()) {
                    var jogoBanco = repository.findByNumIdentificacao(id);

                    if (jogoBanco.isEmpty()) {
                        repository.save(jogoEncontrado.get());
                        System.out.println("Jogo adicionado com sucesso!");
                    } else {
                        System.out.println("Este jogo já está na sua biblioteca.");
                    }
                } else {
                    System.out.println("Não encontramos um jogo com o ID " + id + " na lista de busca.");
                }
            } else {
                System.out.println("Entrada inválida! Você deve digitar o número do ID.");
                sc.nextLine();
            }
        } else if (response.equalsIgnoreCase("N")) {
            System.out.println("Entendido. Retornando ao menu principal...");
        } else {
            System.out.println("Opção inválida. Por favor, responda com 'S' para Sim ou 'N' para Não.");
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
            if (sc.hasNextInt()) {
                int id = sc.nextInt();
                sc.nextLine();
                var jogoEncontrado = similarGames.stream()
                        .filter(j -> j.getNumIdentificacao()
                                .equals(id))
                        .findFirst();

                if (jogoEncontrado.isPresent()) {
                    var jogoBanco = repository.findByNumIdentificacao(id);

                    if (jogoBanco.isEmpty()) {
                        repository.save(jogoEncontrado.get());
                        System.out.println("Jogo adicionado com sucesso!");
                    } else {
                        System.out.println("Este jogo já está na sua biblioteca.");
                    }
                } else {
                    System.out.println("Não encontramos um jogo com o ID " + id + " na lista de busca.");
                }
            } else {
                System.out.println("Entrada inválida! Você deve digitar o número do ID.");
                sc.nextLine();
            }
        } else if (response.equalsIgnoreCase("N")) {
            System.out.println("Entendido. Retornando ao menu principal...");
        } else {
            System.out.println("Opção inválida. Por favor, responda com 'S' para Sim ou 'N' para Não.");
        }
    }
}
