package circulo;

import java.awt.Color;
import java.awt.Graphics;

import ponto.PontoGr;
import primitivo.PrimitivoGrafico;

/**
 * Representacao grafica de um circulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public class CirculoGr extends Circulo implements PrimitivoGrafico {
    /** Armazena corCirculo da classe. */
    private Color corCirculo = Color.BLACK;
    /** Armazena nomeCirculo da classe. */
    private String nomeCirculo = "";
    /** Armazena corNomeCirculo da classe. */
    private Color corNomeCirculo = Color.BLACK;
    /** Armazena espCirculo da classe. */
    private int espCirculo = 1;

    /**
     * Constroi um objeto da classe CirculoGr.
     *
     * @param xCentro valor de xCentro
     * @param yCentro valor de yCentro
     * @param xBorda valor de xBorda
     * @param yBorda valor de yBorda
     * @param cor valor de cor
     * @param nome valor de nome
     * @param esp valor de esp
     */
    public CirculoGr(int xCentro, int yCentro, int xBorda, int yBorda, Color cor, String nome, int esp) {
        super(xCentro, yCentro, xBorda, yBorda);
        setCorCirculo(cor);
        setNomeCirculo(nome);
        setEspCirculo(esp);
    }

    /**
     * Retorna o valor de CorCirculo.
     * @return valor retornado
     */
    public Color getCorCirculo() {
        return this.corCirculo;
    }

    /**
     * Altera o valor de CorCirculo.
     *
     * @param corCirculo valor de corCirculo
     */
    public void setCorCirculo(Color corCirculo) {
        this.corCirculo = corCirculo;
    }

    /**
     * Retorna o valor de NomeCirculo.
     * @return valor retornado
     */
    public String getNomeCirculo() {
        return this.nomeCirculo;
    }

    /**
     * Altera o valor de NomeCirculo.
     *
     * @param nomeCirculo valor de nomeCirculo
     */
    public void setNomeCirculo(String nomeCirculo) {
        this.nomeCirculo = nomeCirculo;
    }

    /**
     * Retorna o valor de CorNomeCirculo.
     * @return valor retornado
     */
    public Color getCorNomeCirculo() {
        return this.corNomeCirculo;
    }

    /**
     * Altera o valor de CorNomeCirculo.
     *
     * @param corNomeCirculo valor de corNomeCirculo
     */
    public void setCorNomeCirculo(Color corNomeCirculo) {
        this.corNomeCirculo = corNomeCirculo;
    }

    /**
     * Retorna o valor de EspCirculo.
     * @return valor retornado
     */
    public int getEspCirculo() {
        return this.espCirculo;
    }

    /**
     * Altera o valor de EspCirculo.
     *
     * @param espCirculo valor de espCirculo
     */
    public void setEspCirculo(int espCirculo) {
        this.espCirculo = espCirculo;
    }

    /**
     * Desenha um circulo com o algoritmo do ponto medio.
     *
     * @param g contexto grafico
     */
    public void desenharCirculo(Graphics g) {
        int xCentro = (int)getCentro().getX();
        int yCentro = (int)getCentro().getY();
        int raio = getRaio();
        int x = 0;
        int y = raio;
        int decisao = 1 - raio;

        desenharPontosSimetricos(g, xCentro, yCentro, x, y);

        while (x < y) {
            x++;

            if (decisao < 0) {
                decisao += 2 * x + 1;
            } else {
                y--;
                decisao += 2 * (x - y) + 1;
            }

            desenharPontosSimetricos(g, xCentro, yCentro, x, y);
        }

        g.setColor(getCorNomeCirculo());
        g.drawString(getNomeCirculo(), xCentro + getRaio() + getEspCirculo(), yCentro);
    }

    /**
     * Desenha os pontos simetricos do circulo.
     *
     * @param g valor de g
     * @param xCentro valor de xCentro
     * @param yCentro valor de yCentro
     * @param x valor de x
     * @param y valor de y
     */
    private void desenharPontosSimetricos(Graphics g, int xCentro, int yCentro, int x, int y) {
        desenharPonto(g, xCentro + x, yCentro + y);
        desenharPonto(g, xCentro - x, yCentro + y);
        desenharPonto(g, xCentro + x, yCentro - y);
        desenharPonto(g, xCentro - x, yCentro - y);
        desenharPonto(g, xCentro + y, yCentro + x);
        desenharPonto(g, xCentro - y, yCentro + x);
        desenharPonto(g, xCentro + y, yCentro - x);
        desenharPonto(g, xCentro - y, yCentro - x);
    }

    /**
     * Desenha um ponto do contorno do circulo.
     *
     * @param g valor de g
     * @param x valor de x
     * @param y valor de y
     */
    private void desenharPonto(Graphics g, int x, int y) {
        PontoGr ponto = new PontoGr(x, y, getCorCirculo(), getEspCirculo());
        ponto.desenharPonto(g);
    }

    /**
     * Desenha o primitivo armazenado na ED.
     *
     * @param g valor de g
     */
    public void desenhar(Graphics g) {
        desenharCirculo(g);
    }

    /**
     * Retorna o valor de Tipo.
     * @return valor retornado
     */
    public String getTipo() {
        return "CIRCULO";
    }
}