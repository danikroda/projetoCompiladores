package com.mycompany.analisadorlexico;
 
import java.util.ArrayList;
import java.util.List;
 
/**
 * Classe do Analisador Léxico.
 * Reconhece os tokens da linguagem GYH.
 */
public class AnaliseLexica {
    public LeitorArquivo ldat;
    private final List<String> errosLexicos = new ArrayList<>();
    
    public AnaliseLexica(String nome){
        ldat = new LeitorArquivo(nome);
    }
    
    public boolean temErros() {
        return !errosLexicos.isEmpty();
    }
    
    public List<String> getErros() {
        return errosLexicos;
    }
    
    public Token proxToken(){
        int caractere = ldat.lerProximoCaractere();
 
        while(caractere == ' ' || caractere == '\n' || caractere == '\r' || caractere == '\t') {
            caractere = ldat.lerProximoCaractere();
        }
 
        if (caractere == -1) {
            return null;
        }
        
        char c = (char) caractere;
 
        if (c == '#') {
            while (caractere != '\n' && caractere != -1) {
                caractere = ldat.lerProximoCaractere();
            }
 
            return proxToken(); 
        }
 
        if (Character.isDigit(c)) {
            String lexema = "";
            boolean temPonto = false;
            
            while (Character.isDigit((char)caractere) || caractere == '.') {
                if (caractere == '.') {
                    if (temPonto) break;
                    temPonto = true;
                }
                lexema += (char)caractere;
                caractere = ldat.lerProximoCaractere();
            }
            ldat.deslerCaractere(caractere);
            
            if (temPonto) {
                return new Token(lexema, TipoToken.NumReal);
            } else {
                return new Token(lexema, TipoToken.NumInt);
            }
        }
 
        if (c == '"') {
            String lexema = "\"";
            caractere = ldat.lerProximoCaractere();
            while (caractere != '"' && caractere != -1) {
                lexema += (char)caractere;
                caractere = ldat.lerProximoCaractere();
            }
            lexema += "\""; // Adiciona a aspa final
            return new Token(lexema, TipoToken.Cadeia);
        }
       
        if (Character.isLetter(c)) {
            String lexema = "";
            while (Character.isLetterOrDigit((char)caractere)) {
                lexema += (char)caractere;
                caractere = ldat.lerProximoCaractere();
            }
            ldat.deslerCaractere(caractere); 
            
            switch (lexema) {
                case "DEC": return new Token(lexema, TipoToken.PCDec);
                case "PROG": return new Token(lexema, TipoToken.PCProg);
                case "INT": return new Token(lexema, TipoToken.PCInt);
                case "REAL": return new Token(lexema, TipoToken.PCReal);
                case "LER": return new Token(lexema, TipoToken.PCLer);
                case "IMPRIMIR": return new Token(lexema, TipoToken.PCImprimir);
                case "SE": return new Token(lexema, TipoToken.PCSe);
                case "SENAO": return new Token(lexema, TipoToken.PCSenao);
                case "ENTAO": return new Token(lexema, TipoToken.PCEntao);
                case "ENQTO": return new Token(lexema, TipoToken.PCEnqto);
                case "INI": return new Token(lexema, TipoToken.PCIni);
                case "FIM": return new Token(lexema, TipoToken.PCFim);
                case "E": return new Token(lexema, TipoToken.OpBoolE);
                case "OU": return new Token(lexema, TipoToken.OpBoolOu);
                default: 
                  
                    if (Character.isLowerCase(lexema.charAt(0))) {
                        return new Token(lexema, TipoToken.Var);
                    } else {
                        errosLexicos.add("Erro Lexico: Variavel mal formada (deve iniciar com minuscula): " + lexema);
                        return proxToken(); 
                    }
            }
        }
 
 
        int proximoChar = ldat.lerProximoCaractere();
        if (proximoChar != -1) {
            char nextC = (char) proximoChar;
            String opComposto = "" + c + nextC;
            
            switch(opComposto) {
                case "<=": return new Token(opComposto, TipoToken.OpRelMenorIgual);
                case ">=": return new Token(opComposto, TipoToken.OpRelMaiorIgual);
                case "==": return new Token(opComposto, TipoToken.OpRelIgual);
                case "!=": return new Token(opComposto, TipoToken.OpRelDif);
                case ":=": return new Token(opComposto, TipoToken.Atrib);
            }
        }
        
        ldat.deslerCaractere(proximoChar);
 
       
        switch(c){
            case '+': return new Token("+", TipoToken.OpAritSoma); 
            case '-': return new Token("-", TipoToken.OpAritSub);
            case '*': return new Token("*", TipoToken.OpAritMult);
            case '/': return new Token("/", TipoToken.OpAritDiv);
            case '<': return new Token("<", TipoToken.OpRelMenor);
            case '>': return new Token(">", TipoToken.OpRelMaior);
            case ':': return new Token(":", TipoToken.Delim);
            case '(': return new Token("(", TipoToken.AbrePar);
            case ')': return new Token(")", TipoToken.FechaPar);
            default:
               
                errosLexicos.add("Erro Lexico: Caractere invalido na linguagem GYH: " + c);
                return proxToken(); 
        }
    }   
}
