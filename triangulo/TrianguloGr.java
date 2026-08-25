package triangulo;

import java.awt.Color;
import java.awt.Graphics;

import primitivo.PrimitivoGrafico;
import reta.RetaGr;

/**
 * Representacao grafica de um triangulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public class TrianguloGr extends Triangulo implements PrimitivoGrafico {
    /** Armazena corTriangulo da classe. */
    private Color corTriangulo = Color.BLACK;
    /** Armazena nomeTriangulo da classe. */
    private String nomeTriangulo = "";
    /** Armazena corNomeTriangulo da classe. */
    private Color corNomeTriangulo = Color.BLACK;
    /** Armazena espTriangulo da classe. */
    private int espTriangulo = 1;

    /**
     * Constroi um objeto da classe TrianguloGr.
     *
     * @param x1 valor de x1
     * @param y1 valor de y1
     * @param x2 valor de x2
     * @param y2 valor de y2
     * @param x3 valor de x3
     * @param y3 valor de y3
     * @param cor valor de cor
     * @param nome valor de nome
     * @param esp valor de esp
     */
    public TrianguloGr(int x1, int y1, int x2, int y2, int x3, int y3, Color cor, String nome, int esp) {
        super(x1, y1, x2, y2, x3, y3);
        setCorTriangulo(cor);
        setNomeTriangulo(nome);
        setEspTriangulo(esp);
    }

    /**
     * Retorna o valor de CorTriangulo.
     * @return valor retornado
     */
    public Color getCorTriangulo() {
        return this.corTriangulo;
    }

    /**
     * Altera o valor de CorTriangulo.
     *
     * @param corTriangulo valor de corTriangulo
     */
    public void setCorTriangulo(Color corTriangulo) {
        this.corTriangulo = corTriangulo;
    }

    /**
     * Retorna o valor de NomeTriangulo.
     * @return valor retornado
     */
    public String getNomeTriangulo() {
        return this.nomeTriangulo;
    }

    /**
     * Altera o valor de NomeTriangulo.
     *
     * @param nomeTriangulo valor de nomeTriangulo
     */
    public void setNomeTriangulo(String nomeTriangulo) {
        this.nomeTriangulo = nomeTriangulo;
    }

    /**
     * Retorna o valor de CorNomeTriangulo.
     * @return valor retornado
     */
    public Color getCorNomeTriangulo() {
        return this.corNomeTriangulo;
    }

    /**
     * Altera o valor de CorNomeTriangulo.
     *
     * @param corNomeTriangulo valor de corNomeTriangulo
     */
    public void setCorNomeTriangulo(Color corNomeTriangulo) {
        this.corNomeTriangulo = corNomeTriangulo;
    }

    /**
     * Retorna o valor de EspTriangulo.
     * @return valor retornado
     */
    public int getEspTriangulo() {
        return this.espTriangulo;
    }

    /**
     * Altera o valor de EspTriangulo.
     *
     * @param espTriangulo valor de espTriangulo
     */
    public void setEspTriangulo(int espTriangulo) {
        this.espTriangulo = espTriangulo;
    }

    /**
     * Desenha o triangulo usando tres retas.
     *
     * @param g contexto grafico
     */
    public void desenharTriangulo(Graphics g) {
        int x1 = (int)getP1().getX();
        int y1 = (int)getP1().getY();
        int x2 = (int)getP2().getX();
        int y2 = (int)getP2().getY();
        int x3 = (int)getP3().getX();
        int y3 = (int)getP3().getY();

        new RetaGr(x1, y1, x2, y2, getCorTriangulo(), "", getEspTriangulo()).desenhar(g);
        new RetaGr(x2, y2, x3, y3, getCorTriangulo(), "", getEspTriangulo()).desenhar(g);
        new RetaGr(x3, y3, x1, y1, getCorTriangulo(), "", getEspTriangulo()).desenhar(g);

        g.setColor(getCorNomeTriangulo());
        g.drawString(getNomeTriangulo(), x1 + getEspTriangulo(), y1 - getEspTriangulo());
    }

    /**
     * Desenha o primitivo armazenado na ED.
     *
     * @param g valor de g
     */
    public void desenhar(Graphics g) {
        desenharTriangulo(g);
    }

    /**
     * Retorna o valor de Tipo.
     * @return valor retornado
     */
    public String getTipo() {
        return "TRIANGULO";
    }
}