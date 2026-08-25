# 2D Drawing Tool

Projeto academico desenvolvido em Java/BlueJ para a disciplina de Computacao Grafica e Processamento de Imagens.

## Autor

Kaua Bezerra Brito

## Objetivo

O projeto implementa um desenhador 2D, semelhante a uma versao simples do Paint, com suporte aos seguintes primitivos graficos:

- Ponto
- Reta
- Circulo
- Retangulo
- Triangulo

## Como usar

Execute a classe `App` no BlueJ.

Na barra superior, escolha o tipo de primitivo, a cor e a espessura. Depois clique na area de desenho de acordo com o primitivo selecionado:

- `Ponto`: 1 clique.
- `Reta`: 2 cliques, um para cada extremidade.
- `Circulo`: 2 cliques, sendo o primeiro o centro e o segundo um ponto da borda.
- `Retangulo`: 2 cliques, representando cantos opostos.
- `Triangulo`: 3 cliques, um para cada vertice.

## Estrutura de dados

Os primitivos desenhados sao armazenados em uma lista ligada simples implementada no pacote `eds.listaLigadaSimples`.

A tela e a estrutura de dados sao tratadas separadamente:

- O botao `Limpar` limpa somente a tela.
- Os primitivos continuam armazenados na lista ligada.
- O combo `Redesenhar` permite redesenhar todos os primitivos ou apenas um tipo especifico.

Essa separacao atende ao requisito de testar a ED, pois permite comprovar que os primitivos foram armazenados e podem ser redesenhados posteriormente.

## Organizacao

Cada primitivo possui um pacote proprio, seguindo o padrao original do projeto:

- `ponto`
- `reta`
- `circulo`
- `retangulo`
- `triangulo`

Tambem foram adicionados:

- `primitivo`: interface comum para objetos graficos desenhaveis.
- `eds.listaLigadaSimples`: estrutura de dados usada para armazenar os primitivos.

## Compilacao

O projeto foi mantido para uso no BlueJ. Pela configuracao original, os arquivos usam a codificacao `windows-1252`.

Para compilar pelo terminal:

```bash
javac -encoding windows-1252 App.java
```

