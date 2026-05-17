package com.mycompany.analisadorlexico;

import com.mycompany.analisadorsintatico.AnalisadorSintatico;

/**
 * INTEGRANTES DA DUPLA:
 * Nome: [Daniel Elder Kroda] - RA: [2605600]
 * Nome: [Guilherme Rozzi Dicatti] - RA: [2605660]
 */
public class AnalisadorLexico {

    public static void main(String[] args) {
        
        System.out.println("==================================================");
        System.out.println("          TABELA DE TOKENS GERADOS                ");
        System.out.println("==================================================");
        
        // 1. Cria um leitor apenas para listar os tokens no console
        AnaliseLexica lexPainel = new AnaliseLexica("teste.gyh");
        Token t = lexPainel.proxToken();
        
        while (t != null) {
            // Organiza a saída visual alinhando as colunas
            System.out.printf("Tipo: %-15s | Lexema: \"%s\"%n", t.padrao, t.lexema);
            t = lexPainel.proxToken();
        }
        
        System.out.println("==================================================");
        System.out.println("          INICIANDO FLUXO SINTÁTICO               ");
        System.out.println("==================================================\n");
        
        // 2. Cria uma NOVA instância limpa para alimentar o Analisador Sintático
        AnaliseLexica lexReal = new AnaliseLexica("teste.gyh");
        AnalisadorSintatico sintatico = new AnalisadorSintatico(lexReal);
        
        try {
            sintatico.parsePrograma();
            System.out.println("\nAnalise Lexica e Sintatica concluidas com sucesso!");
        } catch (Exception e) {
            System.err.println("\nFalha encontrada durante a analise:");
            System.err.println(e.getMessage());
        }
    }
}