package com.mycompany.analisadorsintatico;

import com.mycompany.analisadorlexico.AnaliseLexica;
import com.mycompany.analisadorlexico.Token;
import com.mycompany.analisadorlexico.TipoToken;

public class AnalisadorSintatico {
    private final AnaliseLexica lexer;
    private Token tokenAtual;

    public AnalisadorSintatico(AnaliseLexica lexer) {
        this.lexer = lexer;
        this.tokenAtual = lexer.proxToken();
    }

    // --- MÉTODO UTILITÁRIO ---
    private void consumir(TipoToken tipoEsperado) {
        if (tokenAtual != null && tokenAtual.padrao == tipoEsperado) {
            System.out.println("[Sintatico] Consumiu Token: " + tokenAtual.toString());
            tokenAtual = lexer.proxToken();
        } else {
            String tipoEncontrado = (tokenAtual != null) ? tokenAtual.padrao.toString() : "FIM_DE_ARQUIVO";
            String valorEncontrado = (tokenAtual != null) ? tokenAtual.lexema : "EOF";
            throw new RuntimeException("Erro Sintatico: Esperado " + tipoEsperado + 
                                       " mas encontrou " + tipoEncontrado + " (" + valorEncontrado + ")");
        }
    }

    private void verificarFimDeArquivo() {
        if (tokenAtual == null) {
            throw new RuntimeException("Erro Sintatico: Fim de arquivo inesperado.");
        }
    }

    // ==========================================================
    // MÉTODOS DA GRAMÁTICA (GYH)
    // ==========================================================

    public void parsePrograma() {
        consumir(TipoToken.Delim); 
        consumir(TipoToken.PCDec); 
        parseListaDeclaracoes();
        consumir(TipoToken.Delim); 
        consumir(TipoToken.PCProg); 
        parseListaComandos();
    }

    private void parseListaDeclaracoes() {
        parseDeclaracao();
        while (tokenAtual != null && tokenAtual.padrao == TipoToken.Var) {
            parseDeclaracao();
        }
    }

    private void parseDeclaracao() {
        consumir(TipoToken.Var);
        consumir(TipoToken.Delim); 
        parseTipoVar();
    }

    private void parseTipoVar() {
        verificarFimDeArquivo();
        // Novo "Rule Switch" com ->
        switch (tokenAtual.padrao) {
            case PCInt -> consumir(TipoToken.PCInt);
            case PCReal -> consumir(TipoToken.PCReal);
            default -> throw new RuntimeException("Erro Sintatico: Esperado tipo INT ou REAL, encontrado: " + tokenAtual.padrao);
        }
    }

    private void parseExpressaoAritmetica() {
        parseTermoAritmetico();
        while (tokenAtual != null && (tokenAtual.padrao == TipoToken.OpAritSoma || tokenAtual.padrao == TipoToken.OpAritSub)) {
            consumir(tokenAtual.padrao); 
            parseTermoAritmetico();
        }
    }

    private void parseTermoAritmetico() {
        parseFatorAritmetico();
        while (tokenAtual != null && (tokenAtual.padrao == TipoToken.OpAritMult || tokenAtual.padrao == TipoToken.OpAritDiv)) {
            consumir(tokenAtual.padrao); 
            parseFatorAritmetico();
        }
    }

    private void parseFatorAritmetico() {
        verificarFimDeArquivo();
        switch (tokenAtual.padrao) {
            case NumInt -> consumir(TipoToken.NumInt);
            case NumReal -> consumir(TipoToken.NumReal);
            case Var -> consumir(TipoToken.Var);
            case AbrePar -> {
                // Quando tem mais de um comando, usamos chaves {}
                consumir(TipoToken.AbrePar);
                parseExpressaoAritmetica();
                consumir(TipoToken.FechaPar);
            }
            default -> throw new RuntimeException("Erro Sintatico em FatorAritmetico: token inesperado " + tokenAtual.padrao);
        }
    }

    private void parseExpressaoRelacional() {
        parseTermoRelacional();
        while (tokenAtual != null && (tokenAtual.padrao == TipoToken.OpBoolE || tokenAtual.padrao == TipoToken.OpBoolOu)) {
            parseOperadorBooleano();
            parseTermoRelacional();
        }
    }

    private void parseTermoRelacional() {
        if (tokenAtual != null && tokenAtual.padrao == TipoToken.AbrePar) {
            consumir(TipoToken.AbrePar);
            parseExpressaoRelacional();
            consumir(TipoToken.FechaPar);
        } else {
            parseExpressaoAritmetica();
            
            if (tokenAtual != null && (
                tokenAtual.padrao == TipoToken.OpRelMenor || tokenAtual.padrao == TipoToken.OpRelMenorIgual ||
                tokenAtual.padrao == TipoToken.OpRelMaior || tokenAtual.padrao == TipoToken.OpRelMaiorIgual ||
                tokenAtual.padrao == TipoToken.OpRelIgual || tokenAtual.padrao == TipoToken.OpRelDif)) {
                
                consumir(tokenAtual.padrao);
            } else {
                 throw new RuntimeException("Erro Sintatico: Esperado Operador Relacional em TermoRelacional");
            }
            
            parseExpressaoAritmetica();
        }
    }

    private void parseOperadorBooleano() {
        verificarFimDeArquivo();
        switch (tokenAtual.padrao) {
            case OpBoolE -> consumir(TipoToken.OpBoolE);
            case OpBoolOu -> consumir(TipoToken.OpBoolOu);
            default -> throw new RuntimeException("Erro Sintatico: Esperado Operador Booleano (E ou OU)");
        }
    }

    private void parseListaComandos() {
        parseComando();
        while (tokenAtual != null && (
               tokenAtual.padrao == TipoToken.Var || tokenAtual.padrao == TipoToken.PCLer || 
               tokenAtual.padrao == TipoToken.PCImprimir || tokenAtual.padrao == TipoToken.PCSe || 
               tokenAtual.padrao == TipoToken.PCEnqto || tokenAtual.padrao == TipoToken.PCIni)) {
            parseComando();
        }
    }

    private void parseComando() {
        verificarFimDeArquivo();
        switch (tokenAtual.padrao) {
            case Var -> parseComandoAtribuicao();
            case PCLer -> parseComandoEntrada();
            case PCImprimir -> parseComandoSaida();
            case PCSe -> parseComandoCondicao();
            case PCEnqto -> parseComandoRepeticao();
            case PCIni -> parseSubAlgoritmo();
            default -> throw new RuntimeException("Erro Sintatico: Comando invalido iniciado com " + tokenAtual.padrao);
        }
    }

    private void parseComandoAtribuicao() {
        consumir(TipoToken.Var);
        consumir(TipoToken.Atrib); 
        parseExpressaoAritmetica();
    }

    private void parseComandoEntrada() {
        consumir(TipoToken.PCLer);
        consumir(TipoToken.Var);
    }

    private void parseComandoSaida() {
        consumir(TipoToken.PCImprimir);
        verificarFimDeArquivo();
        switch (tokenAtual.padrao) {
            case Var -> consumir(TipoToken.Var);
            case Cadeia -> consumir(TipoToken.Cadeia);
            default -> throw new RuntimeException("Erro Sintatico em ComandoSaida: esperado VARIAVEL ou CADEIA");
        }
    }

    private void parseComandoCondicao() {
        consumir(TipoToken.PCSe);
        parseExpressaoRelacional();
        consumir(TipoToken.PCEntao);
        parseComando();
        
        if (tokenAtual != null && tokenAtual.padrao == TipoToken.PCSenao) {
            consumir(TipoToken.PCSenao);
            parseComando();
        }
    }

    private void parseComandoRepeticao() {
        consumir(TipoToken.PCEnqto);
        parseExpressaoRelacional();
        parseComando();
    }

    private void parseSubAlgoritmo() {
        consumir(TipoToken.PCIni);
        parseListaComandos();
        consumir(TipoToken.PCFim);
    }
}