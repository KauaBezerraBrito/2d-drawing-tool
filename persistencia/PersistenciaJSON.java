package persistencia;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import circulo.CirculoGr;
import eds.listaLigadaSimples.ListaLigadaSimples;
import ponto.Ponto;
import ponto.PontoGr;
import primitivo.PrimitivoGrafico;
import reta.RetaGr;
import retangulo.RetanguloGr;
import triangulo.TrianguloGr;

/**
 * Salva e carrega os primitivos graficos em um arquivo JSON.
 *
 * @author Heitor Cavalcanti
 * @version 20260825
 */
public class PersistenciaJSON {
    private PersistenciaJSON() {
    }

    /**
     * Salva os primitivos armazenados na ED em um arquivo JSON.
     *
     * @param caminho    caminho do arquivo a ser gravado (ex.:
     *                   "saves/desenho.json")
     * @param primitivos ED com os primitivos desenhados
     * @param largura    largura do painel de desenho, usada para normalizar as
     *                   coordenadas
     * @param altura     altura do painel de desenho, usada para normalizar as
     *                   coordenadas
     * @throws IOException se o arquivo nao puder ser criado/escrito
     */
    public static void salvar(String caminho, ListaLigadaSimples<PrimitivoGrafico> primitivos, int largura, int altura)
            throws IOException {
        StringBuilder pontos = new StringBuilder();
        StringBuilder retas = new StringBuilder();
        StringBuilder triangulos = new StringBuilder();
        StringBuilder retangulos = new StringBuilder();
        StringBuilder circulos = new StringBuilder();

        int idPonto = 0;
        int idReta = 0;
        int idTriangulo = 0;
        int idRetangulo = 0;
        int idCirculo = 0;

        for (int i = 0; i < primitivos.getQtdNos(); i++) {
            PrimitivoGrafico p = primitivos.obter(i);

            if (p.getTipo().equals("PONTO")) {
                idPonto++;
                anexarItem(pontos, jsonPonto((PontoGr) p, largura, altura, "ponto_" + idPonto));
            } else if (p.getTipo().equals("RETA")) {
                idReta++;
                anexarItem(retas, jsonReta((RetaGr) p, largura, altura, "reta_" + idReta));
            } else if (p.getTipo().equals("TRIANGULO")) {
                idTriangulo++;
                anexarItem(triangulos, jsonTriangulo((TrianguloGr) p, largura, altura, "triangulo_" + idTriangulo));
            } else if (p.getTipo().equals("RETANGULO")) {
                idRetangulo++;
                anexarItem(retangulos, jsonRetangulo((RetanguloGr) p, largura, altura, "retangulo_" + idRetangulo));
            } else if (p.getTipo().equals("CIRCULO")) {
                idCirculo++;
                anexarItem(circulos, jsonCirculo((CirculoGr) p, largura, altura, "circulo_" + idCirculo));
            }
        }

        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("\t\"figura\": {\n");
        json.append("\t\t\"ponto\": [").append(pontos).append("\n\t\t],\n");
        json.append("\t\t\"reta\": [").append(retas).append("\n\t\t],\n");
        json.append("\t\t\"triangulo\": [").append(triangulos).append("\n\t\t],\n");
        json.append("\t\t\"retangulo\": [").append(retangulos).append("\n\t\t],\n");
        json.append("\t\t\"circulo\": [").append(circulos).append("\n\t\t]\n");
        json.append("\t}\n");
        json.append("}");

        escreverArquivo(caminho, json.toString());
    }

    /**
     * Acrescenta um item ja formatado em JSON a lista (cuidando da virgula entre
     * itens).
     *
     * @param destino  builder da lista (ex.: builder dos pontos)
     * @param itemJson texto JSON do item a ser acrescentado
     */
    private static void anexarItem(StringBuilder destino, String itemJson) {
        if (destino.length() > 0) {
            destino.append(",");
        }

        destino.append("\n\t\t\t").append(itemJson);
    }

    /**
     * Monta o JSON de um ponto.
     *
     * @param pg      ponto grafico
     * @param largura largura do painel
     * @param altura  altura do painel
     * @param id      identificador do item (ex.: "ponto_1")
     * @return texto JSON do item
     */
    private static String jsonPonto(PontoGr pg, int largura, int altura, String id) {
        return "{ \"x\": " + fracaoTexto(pg.getX(), largura)
                + ", \"y\": " + fracaoTexto(pg.getY(), altura)
                + ", \"cor\": " + jsonCor(pg.getCorPto())
                + ", \"esp\": " + pg.getDiametro()
                + ", \"id\": \"" + id + "\" }";
    }

    /**
     * Monta o JSON de uma reta.
     *
     * @param rg      reta grafica
     * @param largura largura do painel
     * @param altura  altura do painel
     * @param id      identificador do item
     * @return texto JSON do item
     */
    private static String jsonReta(RetaGr rg, int largura, int altura, String id) {
        return "{ \"p1\": " + jsonPontoTexto(rg.getP1(), largura, altura)
                + ", \"p2\": " + jsonPontoTexto(rg.getP2(), largura, altura)
                + ", \"cor\": " + jsonCor(rg.getCorReta())
                + ", \"esp\": " + rg.getEspReta()
                + ", \"id\": \"" + id + "\" }";
    }

    /**
     * Monta o JSON de um triangulo.
     *
     * @param tg      triangulo grafico
     * @param largura largura do painel
     * @param altura  altura do painel
     * @param id      identificador do item
     * @return texto JSON do item
     */
    private static String jsonTriangulo(TrianguloGr tg, int largura, int altura, String id) {
        return "{ \"p1\": " + jsonPontoTexto(tg.getP1(), largura, altura)
                + ", \"p2\": " + jsonPontoTexto(tg.getP2(), largura, altura)
                + ", \"p3\": " + jsonPontoTexto(tg.getP3(), largura, altura)
                + ", \"cor\": " + jsonCor(tg.getCorTriangulo())
                + ", \"esp\": " + tg.getEspTriangulo()
                + ", \"id\": \"" + id + "\" }";
    }

    /**
     * Monta o JSON de um retangulo.
     *
     * @param rg      retangulo grafico
     * @param largura largura do painel
     * @param altura  altura do painel
     * @param id      identificador do item
     * @return texto JSON do item
     */
    private static String jsonRetangulo(RetanguloGr rg, int largura, int altura, String id) {
        return "{ \"p1\": " + jsonPontoTexto(rg.getP1(), largura, altura)
                + ", \"p2\": " + jsonPontoTexto(rg.getP2(), largura, altura)
                + ", \"cor\": " + jsonCor(rg.getCorRetangulo())
                + ", \"esp\": " + rg.getEspRetangulo()
                + ", \"id\": \"" + id + "\" }";
    }

    /**
     * Monta o JSON de um circulo.
     *
     * O campo "raio" guarda um ponto sintetico na borda do circulo
     * (centro.x + raio, centro.y), para poder ser usado direto no
     * construtor CirculoGr(xCentro,yCentro,xBorda,yBorda,...) ao carregar.
     *
     * @param cg      circulo grafico
     * @param largura largura do painel
     * @param altura  altura do painel
     * @param id      identificador do item
     * @return texto JSON do item
     */
    private static String jsonCirculo(CirculoGr cg, int largura, int altura, String id) {
        Ponto centro = cg.getCentro();
        double xBorda = centro.getX() + cg.getRaio();
        double yBorda = centro.getY();

        return "{ \"centro\": " + jsonPontoTexto(centro.getX(), centro.getY(), largura, altura)
                + ", \"raio\": " + jsonPontoTexto(xBorda, yBorda, largura, altura)
                + ", \"cor\": " + jsonCor(cg.getCorCirculo())
                + ", \"esp\": " + cg.getEspCirculo()
                + ", \"id\": \"" + id + "\" }";
    }

    /**
     * Monta o JSON de um ponto (x,y) normalizado, a partir de um objeto Ponto.
     *
     * @param p       ponto em coordenadas de pixel
     * @param largura largura do painel
     * @param altura  altura do painel
     * @return texto JSON do ponto
     */
    private static String jsonPontoTexto(Ponto p, int largura, int altura) {
        return jsonPontoTexto(p.getX(), p.getY(), largura, altura);
    }

    /**
     * Monta o JSON de um ponto (x,y) normalizado, a partir de coordenadas soltas.
     *
     * @param xPixel  coordenada x em pixel
     * @param yPixel  coordenada y em pixel
     * @param largura largura do painel
     * @param altura  altura do painel
     * @return texto JSON do ponto
     */
    private static String jsonPontoTexto(double xPixel, double yPixel, int largura, int altura) {
        return "{ \"x\": " + fracaoTexto(xPixel, largura) + ", \"y\": " + fracaoTexto(yPixel, altura) + " }";
    }

    /**
     * Monta o JSON de uma cor.
     *
     * @param cor cor a ser convertida
     * @return texto JSON da cor
     */
    private static String jsonCor(Color cor) {
        return "{ \"r\": " + cor.getRed() + ", \"g\": " + cor.getGreen() + ", \"b\": " + cor.getBlue() + " }";
    }

    /**
     * Converte um valor de pixel em fracao (0 a 1) em relacao ao tamanho do painel,
     * ja formatado como texto (com ponto decimal, independente do idioma do
     * sistema).
     *
     * @param valorPixel  valor em pixel
     * @param tamanhoTela largura ou altura do painel
     * @return texto do numero formatado, ex.: "0.162"
     */
    private static String fracaoTexto(double valorPixel, int tamanhoTela) {
        double fracao = (tamanhoTela == 0) ? 0 : valorPixel / tamanhoTela;

        // Locale.US garante ponto decimal (".") mesmo em maquinas configuradas em
        // pt-BR,
        // que usariam virgula (",") por padrao e quebrariam o JSON.
        return String.format(Locale.US, "%.3f", fracao);
    }

    /**
     * Escreve um texto em um arquivo, criando a pasta de destino se necessario.
     *
     * @param caminho  caminho do arquivo
     * @param conteudo texto a ser gravado
     * @throws IOException se o arquivo nao puder ser escrito
     */
    private static void escreverArquivo(String caminho, String conteudo) throws IOException {
        File arquivo = new File(caminho);
        File pastaPai = arquivo.getParentFile();

        if (pastaPai != null && !pastaPai.exists()) {
            pastaPai.mkdirs();
        }

        Writer escritor = new OutputStreamWriter(new FileOutputStream(arquivo), StandardCharsets.UTF_8);
        try {
            escritor.write(conteudo);
        } finally {
            escritor.close();
        }
    }

    /**
     * Carrega os primitivos gravados em um arquivo JSON.
     *
     * @param caminho caminho do arquivo a ser lido
     * @param largura largura atual do painel de desenho, usada para desnormalizar
     *                as coordenadas
     * @param altura  altura atual do painel de desenho, usada para desnormalizar as
     *                coordenadas
     * @return nova ED ja populada com os primitivos lidos do arquivo
     * @throws IOException se o arquivo nao existir ou nao puder ser lido
     */
    @SuppressWarnings("unchecked")
    public static ListaLigadaSimples<PrimitivoGrafico> carregar(String caminho, int largura, int altura)
            throws IOException {
        String texto = lerArquivo(caminho);
        Object raiz = JsonParser.parse(texto);

        Map<String, Object> objetoRaiz = (Map<String, Object>) raiz;
        Map<String, Object> figura = (Map<String, Object>) objetoRaiz.get("figura");

        ListaLigadaSimples<PrimitivoGrafico> primitivos = new ListaLigadaSimples<PrimitivoGrafico>();

        carregarPontos(figura, primitivos, largura, altura);
        carregarRetas(figura, primitivos, largura, altura);
        carregarTriangulos(figura, primitivos, largura, altura);
        carregarRetangulos(figura, primitivos, largura, altura);
        carregarCirculos(figura, primitivos, largura, altura);

        return primitivos;
    }

    /**
     * Le todo o conteudo de um arquivo texto (UTF-8).
     *
     * @param caminho caminho do arquivo
     * @return conteudo completo do arquivo
     * @throws IOException se o arquivo nao existir ou nao puder ser lido
     */
    private static String lerArquivo(String caminho) throws IOException {
        StringBuilder conteudo = new StringBuilder();
        BufferedReader leitor = new BufferedReader(
                new InputStreamReader(new FileInputStream(caminho), StandardCharsets.UTF_8));

        try {
            String linha = leitor.readLine();
            while (linha != null) {
                conteudo.append(linha).append("\n");
                linha = leitor.readLine();
            }
        } finally {
            leitor.close();
        }

        return conteudo.toString();
    }

    /**
     * Carrega os pontos do bloco "figura" para a ED.
     *
     * @param figura     mapa com os arrays de cada tipo de primitivo
     * @param primitivos ED de destino
     * @param largura    largura do painel
     * @param altura     altura do painel
     */
    @SuppressWarnings("unchecked")
    private static void carregarPontos(Map<String, Object> figura, ListaLigadaSimples<PrimitivoGrafico> primitivos,
            int largura, int altura) {
        for (Object item : obterLista(figura, "ponto")) {
            Map<String, Object> obj = (Map<String, Object>) item;

            int x = pixel(obj.get("x"), largura);
            int y = pixel(obj.get("y"), altura);
            Color cor = lerCor(obj.get("cor"));
            int esp = (int) numero(obj.get("esp"));

            primitivos.inserirFim(new PontoGr(x, y, cor, esp));
        }
    }

    /**
     * Carrega as retas do bloco "figura" para a ED.
     *
     * @param figura     mapa com os arrays de cada tipo de primitivo
     * @param primitivos ED de destino
     * @param largura    largura do painel
     * @param altura     altura do painel
     */
    @SuppressWarnings("unchecked")
    private static void carregarRetas(Map<String, Object> figura, ListaLigadaSimples<PrimitivoGrafico> primitivos,
            int largura, int altura) {
        for (Object item : obterLista(figura, "reta")) {
            Map<String, Object> obj = (Map<String, Object>) item;

            int[] p1 = lerPontoPixel(obj.get("p1"), largura, altura);
            int[] p2 = lerPontoPixel(obj.get("p2"), largura, altura);
            Color cor = lerCor(obj.get("cor"));
            int esp = (int) numero(obj.get("esp"));

            primitivos.inserirFim(new RetaGr(p1[0], p1[1], p2[0], p2[1], cor, "", esp));
        }
    }

    /**
     * Carrega os triangulos do bloco "figura" para a ED.
     *
     * @param figura     mapa com os arrays de cada tipo de primitivo
     * @param primitivos ED de destino
     * @param largura    largura do painel
     * @param altura     altura do painel
     */
    @SuppressWarnings("unchecked")
    private static void carregarTriangulos(Map<String, Object> figura, ListaLigadaSimples<PrimitivoGrafico> primitivos,
            int largura, int altura) {
        for (Object item : obterLista(figura, "triangulo")) {
            Map<String, Object> obj = (Map<String, Object>) item;

            int[] p1 = lerPontoPixel(obj.get("p1"), largura, altura);
            int[] p2 = lerPontoPixel(obj.get("p2"), largura, altura);
            int[] p3 = lerPontoPixel(obj.get("p3"), largura, altura);
            Color cor = lerCor(obj.get("cor"));
            int esp = (int) numero(obj.get("esp"));

            primitivos.inserirFim(new TrianguloGr(p1[0], p1[1], p2[0], p2[1], p3[0], p3[1], cor, "", esp));
        }
    }

    /**
     * Carrega os retangulos do bloco "figura" para a ED.
     *
     * @param figura     mapa com os arrays de cada tipo de primitivo
     * @param primitivos ED de destino
     * @param largura    largura do painel
     * @param altura     altura do painel
     */
    @SuppressWarnings("unchecked")
    private static void carregarRetangulos(Map<String, Object> figura, ListaLigadaSimples<PrimitivoGrafico> primitivos,
            int largura, int altura) {
        for (Object item : obterLista(figura, "retangulo")) {
            Map<String, Object> obj = (Map<String, Object>) item;

            int[] p1 = lerPontoPixel(obj.get("p1"), largura, altura);
            int[] p2 = lerPontoPixel(obj.get("p2"), largura, altura);
            Color cor = lerCor(obj.get("cor"));
            int esp = (int) numero(obj.get("esp"));

            primitivos.inserirFim(new RetanguloGr(p1[0], p1[1], p2[0], p2[1], cor, "", esp));
        }
    }

    /**
     * Carrega os circulos do bloco "figura" para a ED.
     *
     * O campo "raio" e lido como um ponto da borda (nao como numero),
     * e usado direto no construtor de 4 coordenadas do CirculoGr.
     *
     * @param figura     mapa com os arrays de cada tipo de primitivo
     * @param primitivos ED de destino
     * @param largura    largura do painel
     * @param altura     altura do painel
     */
    @SuppressWarnings("unchecked")
    private static void carregarCirculos(Map<String, Object> figura, ListaLigadaSimples<PrimitivoGrafico> primitivos,
            int largura, int altura) {
        for (Object item : obterLista(figura, "circulo")) {
            Map<String, Object> obj = (Map<String, Object>) item;

            int[] centro = lerPontoPixel(obj.get("centro"), largura, altura);
            int[] borda = lerPontoPixel(obj.get("raio"), largura, altura);
            Color cor = lerCor(obj.get("cor"));
            int esp = (int) numero(obj.get("esp"));

            primitivos.inserirFim(new CirculoGr(centro[0], centro[1], borda[0], borda[1], cor, "", esp));
        }
    }

    /**
     * Le a lista associada a uma chave do bloco "figura", tratando o caso de nao
     * existir.
     *
     * @param figura mapa com os arrays de cada tipo de primitivo
     * @param chave  nome do tipo de primitivo (ex.: "circulo")
     * @return lista de itens (vazia se a chave nao existir)
     */
    @SuppressWarnings("unchecked")
    private static List<Object> obterLista(Map<String, Object> figura, String chave) {
        Object valor = figura.get(chave);

        if (valor == null) {
            return new java.util.ArrayList<Object>();
        }

        return (List<Object>) valor;
    }

    /**
     * Le um ponto normalizado (mapa com "x" e "y") e devolve suas coordenadas em
     * pixel.
     *
     * @param valorPonto mapa com os campos "x" e "y"
     * @param largura    largura do painel
     * @param altura     altura do painel
     * @return vetor [xPixel, yPixel]
     */
    @SuppressWarnings("unchecked")
    private static int[] lerPontoPixel(Object valorPonto, int largura, int altura) {
        Map<String, Object> ponto = (Map<String, Object>) valorPonto;
        int x = pixel(ponto.get("x"), largura);
        int y = pixel(ponto.get("y"), altura);
        return new int[] { x, y };
    }

    /**
     * Le uma cor (mapa com "r", "g" e "b").
     *
     * @param valorCor mapa com os campos "r", "g" e "b"
     * @return cor lida
     */
    @SuppressWarnings("unchecked")
    private static Color lerCor(Object valorCor) {
        Map<String, Object> cor = (Map<String, Object>) valorCor;
        int r = (int) numero(cor.get("r"));
        int g = (int) numero(cor.get("g"));
        int b = (int) numero(cor.get("b"));
        return new Color(r, g, b);
    }

    /**
     * Converte um valor normalizado (fracao 0 a 1) de volta para pixel.
     *
     * @param valorFracao valor lido do JSON (Double)
     * @param tamanhoTela largura ou altura do painel
     * @return valor em pixel, arredondado
     */
    private static int pixel(Object valorFracao, int tamanhoTela) {
        return (int) Math.round(numero(valorFracao) * tamanhoTela);
    }

    /**
     * Converte um Object (produzido pelo JsonParser) em double.
     *
     * @param valor valor lido do JSON (Double)
     * @return valor numerico
     */
    private static double numero(Object valor) {
        return ((Number) valor).doubleValue();
    }
}
