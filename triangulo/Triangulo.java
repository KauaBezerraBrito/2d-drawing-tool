package triangulo;

import ponto.Ponto;

/**
 * Representacao matematica de um triangulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public class Triangulo {
    /** Armazena p1 da classe. */
    private Ponto p1;
    /** Armazena p2 da classe. */
    private Ponto p2;
    /** Armazena p3 da classe. */
    private Ponto p3;

    /**
     * Constroi um triangulo a partir de tres pontos.
     *
     * @param x1 coordenada x do primeiro ponto
     * @param y1 coordenada y do primeiro ponto
     * @param x2 coordenada x do segundo ponto
     * @param y2 coordenada y do segundo ponto
     * @param x3 coordenada x do terceiro ponto
     * @param y3 coordenada y do terceiro ponto
     */
    public Triangulo(int x1, int y1, int x2, int y2, int x3, int y3) {
        setP1(new Ponto(x1, y1));
        setP2(new Ponto(x2, y2));
        setP3(new Ponto(x3, y3));
    }

    /**
     * Retorna o valor de P1.
     * @return valor retornado
     */
    public Ponto getP1() {
        return this.p1;
    }

    /**
     * Altera o valor de P1.
     *
     * @param p1 valor de p1
     */
    public void setP1(Ponto p1) {
        this.p1 = p1;
    }

    /**
     * Retorna o valor de P2.
     * @return valor retornado
     */
    public Ponto getP2() {
        return this.p2;
    }

    /**
     * Altera o valor de P2.
     *
     * @param p2 valor de p2
     */
    public void setP2(Ponto p2) {
        this.p2 = p2;
    }

    /**
     * Retorna o valor de P3.
     * @return valor retornado
     */
    public Ponto getP3() {
        return this.p3;
    }

    /**
     * Altera o valor de P3.
     *
     * @param p3 valor de p3
     */
    public void setP3(Ponto p3) {
        this.p3 = p3;
    }

    /**
     * Retorna a representacao textual do objeto.
     * @return valor retornado
     */
    public String toString() {
        return "Triangulo [p1=" + getP1() + ", p2=" + getP2() + ", p3=" + getP3() + "]";
    }
}