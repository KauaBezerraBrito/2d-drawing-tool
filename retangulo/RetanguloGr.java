package retangulo;

import java.awt.Color;
import java.awt.Graphics;

import primitivo.PrimitivoGrafico;
import reta.RetaGr;

/**
 * Representacao grafica de um retangulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public class RetanguloGr extends Retangulo implements PrimitivoGrafico {
    /** Armazena corRetangulo da classe. */
    private Color corRetangulo = Color.BLACK;
    /** Armazena nomeRetangulo da classe. */
    private String nomeRetangulo = "";
    /** Armazena corNomeRetangulo da classe. */
    private Color corNomeRetangulo = Color.BLACK;
    /** Armazena espRetangulo da classe. */
    private int espRetangulo = 1;

    /**
     * Constroi um objeto da classe RetanguloGr.
     *
     * @param x1 valor de x1
     * @param y1 valor de y1
     * @param x2 valor de x2
     * @param y2 valor de y2
     * @param cor valor de cor
     * @param nome valor de nome
     * @param esp valor de esp
     */
    public RetanguloGr(int x1, int y1, int x2, int y2, Color cor, String nome, int esp) {
        super(x1, y1, x2, y2);
        setCorRetangulo(cor);
        setNomeRetangulo(nome);
        setEspRetangulo(esp);
    }

    /**
     * Retorna o valor de CorRetangulo.
     * @return valor retornado
     */
    public Color getCorRetangulo() {
        return this.corRetangulo;
    }

    /**
     * Altera o valor de CorRetangulo.
     *
     * @param corRetangulo valor de corRetangulo
     */
    public void setCorRetangulo(Color corRetangulo) {
        this.corRetangulo = corRetangulo;
    }

    /**
     * Retorna o valor de NomeRetangulo.
     * @return valor retornado
     */
    public String getNomeRetangulo() {
        return this.nomeRetangulo;
    }

    /**
     * Altera o valor de NomeRetangulo.
     *
     * @param nomeRetangulo valor de nomeRetangulo
     */
    public void setNomeRetangulo(String nomeRetangulo) {
        this.nomeRetangulo = nomeRetangulo;
    }

    /**
     * Retorna o valor de CorNomeRetangulo.
     * @return valor retornado
     */
    public Color getCorNomeRetangulo() {
        return this.corNomeRetangulo;
    }

    /**
     * Altera o valor de CorNomeRetangulo.
     *
     * @param corNomeRetangulo valor de corNomeRetangulo
     */
    public void setCorNomeRetangulo(Color corNomeRetangulo) {
        this.corNomeRetangulo = corNomeRetangulo;
    }

    /**
     * Retorna o valor de EspRetangulo.
     * @return valor retornado
     */
    public int getEspRetangulo() {
        return this.espRetangulo;
    }

    /**
     * Altera o valor de EspRetangulo.
     *
     * @param espRetangulo valor de espRetangulo
     */
    public void setEspRetangulo(int espRetangulo) {
        this.espRetangulo = espRetangulo;
    }

    /**
     * Desenha o retangulo usando quatro retas.
     *
     * @param g contexto grafico
     */
    public void desenharRetangulo(Graphics g) {
        int xMin = getXMin();
        int yMin = getYMin();
        int xMax = getXMax();
        int yMax = getYMax();

        new RetaGr(xMin, yMin, xMax, yMin, getCorRetangulo(), "", getEspRetangulo()).desenhar(g);
        new RetaGr(xMax, yMin, xMax, yMax, getCorRetangulo(), "", getEspRetangulo()).desenhar(g);
        new RetaGr(xMax, yMax, xMin, yMax, getCorRetangulo(), "", getEspRetangulo()).desenhar(g);
        new RetaGr(xMin, yMax, xMin, yMin, getCorRetangulo(), "", getEspRetangulo()).desenhar(g);

        g.setColor(getCorNomeRetangulo());
        g.drawString(getNomeRetangulo(), xMin + getEspRetangulo(), yMin - getEspRetangulo());
    }

    /**
     * Desenha o primitivo armazenado na ED.
     *
     * @param g valor de g
     */
    public void desenhar(Graphics g) {
        desenharRetangulo(g);
    }

    /**
     * Retorna o valor de Tipo.
     * @return valor retornado
     */
    public String getTipo() {
        return "RETANGULO";
    }
}