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
    private Color corCirculo = Color.BLACK;
    private String nomeCirculo = "";
    private Color corNomeCirculo = Color.BLACK;
    private int espCirculo = 1;

    public CirculoGr(int xCentro, int yCentro, int xBorda, int yBorda, Color cor, String nome, int esp) {
        super(xCentro, yCentro, xBorda, yBorda);
        setCorCirculo(cor);
        setNomeCirculo(nome);
        setEspCirculo(esp);
    }

    public Color getCorCirculo() {
        return this.corCirculo;
    }

    public void setCorCirculo(Color corCirculo) {
        this.corCirculo = corCirculo;
    }

    public String getNomeCirculo() {
        return this.nomeCirculo;
    }

    public void setNomeCirculo(String nomeCirculo) {
        this.nomeCirculo = nomeCirculo;
    }

    public Color getCorNomeCirculo() {
        return this.corNomeCirculo;
    }

    public void setCorNomeCirculo(Color corNomeCirculo) {
        this.corNomeCirculo = corNomeCirculo;
    }

    public int getEspCirculo() {
        return this.espCirculo;
    }

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

    private void desenharPonto(Graphics g, int x, int y) {
        PontoGr ponto = new PontoGr(x, y, getCorCirculo(), getEspCirculo());
        ponto.desenharPonto(g);
    }

    public void desenhar(Graphics g) {
        desenharCirculo(g);
    }

    public String getTipo() {
        return "CIRCULO";
    }
}
