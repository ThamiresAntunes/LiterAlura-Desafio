package br.com.alura.literalura.principal;


import br.com.alura.literalura.service.LivroService;

import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final LivroService livroService;

    public Menu(Scanner scanner, LivroService livroService) {
        this.scanner = scanner;
        this.livroService = livroService;
    }

    public void exibir() {
        while (true) {
            System.out.println(
                    "\n=== Catálogo LiterAlura ===\n" +
                            "1. Buscar e Salvar livro por título\n" +
                            "2. Listar todos os livros\n" +
                            "3. Listar livros de um autor desejado\n" +
                            "4. Listar todos os autores\n" +
                            "5. Listar todos os livros por idioma\n" +
                            "6. Deletar um livro do banco\n" +
                            "0. Sair"
            );
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> livroService.buscarLivroApiSalvar();
                case "2" -> livroService.listarLivros();
                case "3" -> livroService.listarLivrosPorAutor();
                case "4" -> livroService.listarAutores();
                case "5" -> livroService.listarLivrosPorIdioma();
                case "6" -> livroService.deletarLivro();
                case "0" -> {
                    System.out.println("Saindo... Até mais!");
                    return;
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}