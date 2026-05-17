# Compilador da Linguagem GYH

Este projeto consiste no desenvolvimento de um Compilador para a Linguagem GYH, contemplando as etapas de Análise Léxica e Análise Sintática. O projeto foi desenvolvido em Java utilizando o padrão de projeto de Descida Recursiva (Recursive Descent Parser) para o analisador sintático, garantindo a validação e processamento corretos da gramática estruturada.

---

## Integrantes da Dupla
* Daniel Elder Kroda - RA: 2605600
* Guilherme Rozzi Dicati - RA: 2605660

---

## Especificações Técnicas

### 1. Analisador Léxico
Responsável por ler o fluxo de caracteres do arquivo fonte `.gyh` e agrupá-los em unidades significativas chamadas Tokens.
* Ignora Elementos Irrelevantes: Comentários iniciados por `#` e caracteres de espaçamento (` `, `\n`, `\r`, `\t`) são descartados de forma transparente.
* Lookahead Otimizado: Implementação de um mecanismo de "desleitura" utilizando `PushbackInputStream` para tratar de forma precisa operadores simples (`<`, `>`, `:`) e compostos (`<=`, `>=`, `==`, `!=`, `:=`).
* Validação Rígida de Identificadores: Variáveis são validadas para garantir que comecem estritamente com letras minúsculas, gerando erros léxicos explícitos caso a regra seja violada.

### 2. Analisador Sintático
Construído a partir de uma gramática LL(1) tratada. Para permitir a correta execução em um parser top-down sem travamentos ou loops infinitos, a gramática original passou por processos de:
* Eliminação de Recursão à Esquerda: Substituição por estruturas iterativas (`while`) no código fonte.
* Fatoração: Resolução de não-determinismos em prefixos comuns (como na estrutura de comandos condicionais SE-ENTAO-SENAO).

---

## Gramática GYH Tratada (Referência)

```text
Programa → ':' 'DEC' ListaDeclaracoes ':' 'PROG' ListaComandos

ListaDeclaracoes → Declaracao ListaDeclaracoesLinha
ListaDeclaracoesLinha → Declaracao ListaDeclaracoesLinha | ε

Declaracao → VARIAVEL ':' TipoVar
TipoVar → 'INT' | 'REAL'

ExpressaoAritmetica → TermoAritmetico ExpressaoAritmeticaLinha
ExpressaoAritmeticaLinha → '+' TermoAritmetico ExpressaoAritmeticaLinha 
                         | '-' TermoAritmetico ExpressaoAritmeticaLinha 
                         | ε

TermoAritmetico → FatorAritmetico TermoAritmeticoLinha
TermoAritmeticoLinha → '*' FatorAritmetico TermoAritmeticoLinha 
                     | '/' FatorAritmetico TermoAritmeticoLinha 
                     | ε

FatorAritmetico → NUMINT | NUMREAL | VARIAVEL | '(' ExpressaoAritmetica ')'

ExpressaoRelacional → TermoRelacional ExpressaoRelacionalLinha
ExpressaoRelacionalLinha → OperadorBooleano TermoRelacional ExpressaoRelacionalLinha | ε

TermoRelacional → ExpressaoAritmetica OP_REL ExpressaoAritmetica | '(' ExpressaoRelacional ')'
OperadorBooleano → 'E' | 'OU'

ListaComandos → Comando ListaComandosLinha
ListaComandosLinha → Comando ListaComandosLinha | ε

Comando → ComandoAtribuicao | ComandoEntrada | ComandoSaida 
        | ComandoCondicao | ComandoRepeticao | SubAlgoritmo

ComandoAtribuicao → VARIAVEL ':=' ExpressaoAritmetica
ComandoEntrada → 'LER' VARIAVEL
ComandoSaida → 'IMPRIMIR' ComandoSaidaLinha
ComandoSaidaLinha → VARIAVEL | CADEIA

ComandoCondicao → 'SE' ExpressaoRelacional 'ENTAO' Comando ComandoCondicaoLinha
ComandoCondicaoLinha → 'SENAO' Comando | ε

ComandoRepeticao → 'ENQTO' ExpressaoRelacional Comando
SubAlgoritmo → 'INI' ListaComandos 'FIM'
