package br.com.alura.literalura.service;

import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Scanner scanner = new Scanner(System.in);

    public void buscarLivroApiSalvar() {
        System.out.print("Digite o título do livro para buscar e salvar: ");
        String tituloBuscado = scanner.nextLine();

        try {
            // Monta a URL com o título buscado
            String url = "https://gutendex.com/books/?search=" + tituloBuscado.replace(" ", "%20");

            // Faz a requisição HTTP e lê o JSON
            JsonNode root = objectMapper.readTree(new URL(url));
            JsonNode resultados = root.path("results");

            // Verifica se há resultados
            if (resultados.isArray() && resultados.size() > 0) {
                JsonNode livroJson = resultados.get(0);

                String titulo = livroJson.path("title").asText("Sem título");
                String idioma = livroJson.path("languages").get(0).asText("desconhecido");

                // Pega o nome do autor
                JsonNode autoresJson = livroJson.path("authors");
                String nomeAutor = "Desconhecido";
                if (autoresJson.isArray() && autoresJson.size() > 0) {
                    nomeAutor = autoresJson.get(0).path("name").asText("Desconhecido");
                }

                final String nomeAutorFinal = nomeAutor;

                // Busca o autor no banco ou cria um novo
                Autor autor = autorRepository.findByNomeIgnoreCase(nomeAutorFinal)
                        .orElseGet(() -> autorRepository.save(new Autor(nomeAutorFinal)));

                // Cria o livro e salva no banco
                Livro livro = new Livro(titulo, idioma, autor);
                livroRepository.save(livro);

                System.out.println("Livro salvo com sucesso no banco!");

            } else {
                System.out.println("Nenhum livro encontrado com esse título.");
            }

        } catch (IOException e) {
            System.out.println("Erro ao acessar a API: " + e.getMessage());
        }
    }


    public void listarLivros() {
        List<Livro> livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            System.out.println("\n=== Lista de Livros Cadastrados ===");
            livros.forEach(System.out::println);
        }
    }

    public void listarLivrosPorAutor() {
        System.out.print("Digite o nome do autor: ");
        String nomeAutor = scanner.nextLine();

        Optional<Autor> autorOptional = autorRepository.findByNomeIgnoreCase(nomeAutor);

        if (autorOptional.isEmpty()) {
            System.out.println("Autor não encontrado.");
            return;
        }

        Autor autor = autorOptional.get();
        List<Livro> livros = livroRepository.findByAutor(autor);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o autor: " + autor.getNome());
        } else {
            System.out.println("\nLivros do autor " + autor.getNome() + ":");
            livros.forEach(System.out::println);
        }
    }

    public void listarAutores() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor cadastrado.");
        } else {
            System.out.println("\n=== Lista de Autores Cadastrados ===");
            autores.forEach(autor -> System.out.println("• " + autor.getNome()));
        }
    }

    public void listarLivrosPorIdioma() {
        System.out.print("Digite o código do idioma (ex: en, pt, fr): ");
        String idioma = scanner.nextLine();

        List<Livro> livros = livroRepository.findByIdiomaIgnoreCase(idioma);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma " + idioma);
        } else {
            System.out.println("\nLivros no idioma '" + idioma + "':");
            livros.forEach(System.out::println);
        }
    }

    public void deletarLivro() {
        System.out.print("Digite o título do livro que deseja deletar: ");
        String titulo = scanner.nextLine();

        Optional<Livro> livroOptional = livroRepository.findByTituloIgnoreCase(titulo);

        if (livroOptional.isPresent()) {
            Livro livro = livroOptional.get();
            livroRepository.delete(livro);
            System.out.println("✅ Livro \"" + livro.getTitulo() + "\" deletado com sucesso!");
        } else {
            System.out.println("❌ Livro com o título \"" + titulo + "\" não foi encontrado.");
        }
    }
}
