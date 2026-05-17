package com.mycompany.analisadorlexico;

/**
 * Enumeração contem os tipos de tokens da linguagem GYH.
 */
public enum TipoToken {
    // Palavras chave
    PCDec, PCProg, PCInt, PCReal, PCLer, PCImprimir, PCSe, PCSenao, PCEntao, PCEnqto, PCIni, PCFim,
    
    // Operadores Aritméticos
    OpAritMult, OpAritDiv, OpAritSoma, OpAritSub,
    
    // Operadores Relacionais
    OpRelMenor, OpRelMenorIgual, OpRelMaior, OpRelMaiorIgual, OpRelIgual, OpRelDif,
    
    // Operadores Booleanos
    OpBoolE, OpBoolOu,
    
    // Delimitador, Atribuição e Parêntesis
    Delim, Atrib, AbrePar, FechaPar,
    
    // Identificadores e Literais
    Var, NumInt, NumReal, Cadeia
}