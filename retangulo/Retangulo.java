package retangulo;

import ponto.Ponto;

/**
 * Representacao matematica de um retangulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public class Retangulo {
    /** Armazena p1 da classe. */
    private Ponto p1;
    /** Armazena p2 da classe. */
    private Ponto p2;

    /**
     * Constroi um retangulo a partir de dois cantos opostos.
     *
     * @param x1 coordenada x do primeiro canto
     * @param y1 coordenada y do primeiro canto
     * @param x2 coordenada x do canto oposto
     * @param y2 coordenada y do canto oposto
     */
    public Retangulo(int x1, int y1, int x2, int y2) {
        setP1(new Ponto(x1, y1));
        setP2(new Ponto(x2, y2));
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
     * Retorna o valor de XMin.
     * @return valor retornado
     */
    public int getXMin() {
        return (int)Math.min(getP1().getX(), getP2().getX());
    }

    /**
     * Retorna o valor de YMin.
     * @return valor retornado
     */
    public int getYMin() {
        return (int)Math.min(getP1().getY(), getP2().getY());
    }

    /**
     * Retorna o valor de XMax.
     * @return valor retornado
     */
    public int getXMax() {
        return (int)Math.max(getP1().getX(), getP2().getX());
    }

    /**
     * Retorna o valor de YMax.
     * @return valor retornado
     */
    public int getYMax() {
        return (int)Math.max(getP1().getY(), getP2().getY());
    }

    /**
     * Retorna a representacao textual do objeto.
     * @return valor retornado
     */
    public String toString() {
        return "Retangulo [p1=" + getP1() + ", p2=" + getP2() + "]";
    }
}