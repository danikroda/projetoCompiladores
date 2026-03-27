package com.mycompany.analisadorlexico;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PushbackInputStream;

/**
 * Classe responsável por ler o arquivo de código.
 * quando o analisador ler um caractere a mais do que o necessário para fechar um token.
 */
public class LeitorArquivo {
    public PushbackInputStream is;
    
    public LeitorArquivo(String nome){
        try {
            is = new PushbackInputStream(new FileInputStream(nome));
        } catch (FileNotFoundException ex) {
            System.err.println("Arquivo não encontrado: " + nome);
        }
    }
    
    public int lerProximoCaractere(){
        int c = -1;
        try {
            c = is.read();
        } catch (IOException ex) {
            System.err.println("Erro ao ler o arquivo.");
        }
        return c;
    }
    
    //devolve o caractere se ele não pertencer ao token atual
    public void deslerCaractere(int c) {
        if (c != -1) {
            try {
                is.unread(c);
            } catch (IOException ex) {
                System.err.println("Erro ao devolver caractere.");
            }
        }
    }
}