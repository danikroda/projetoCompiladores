package com.mycompany.analisadorlexico;

/** 
 * * INTEGRANTES DA DUPLA:
 * Nome: [Daniel Elder Kroda] - RA: [2605600]
 * Nome: [Guilherme Rozzi Dicatti] - RA: [2605660]
 */
public class AnalisadorLexico {

    public static void main(String[] args) {
        
        AnaliseLexica lex = new AnaliseLexica("teste.gyh");
        
        Token t = lex.proxToken();
        
        // Imprime os tokens 
        while(t != null){
            System.out.println(t.toString());
            t = lex.proxToken();
        }
        
        System.out.println("\nAnalise Lexica deu certoo!");
    }
}