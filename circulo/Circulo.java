package circulo;

import ponto.Ponto;

/**
 * Representacao matematica de um circulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public class Circulo {
    /** Armazena centro da classe. */
    private Ponto centro;
    /** Armazena raio da classe. */
    private int raio;

    /**
     * Constroi um circulo a partir do centro e do raio.
     *
     * @param xCentro coordenada x do centro
     * @param yCentro coordenada y do centro
     * @param raio raio do circulo
     */
    public Circulo(int xCentro, int yCentro, int raio) {
        setCentro(new Ponto(xCentro, yCentro));
        setRaio(raio);
    }

    /**
     * Constroi um circulo a partir do centro e de um ponto da borda.
     *
     * @param xCentro coordenada x do centro
     * @param yCentro coordenada y do centro
     * @param xBorda coordenada x de um ponto da borda
     * @param yBorda coordenada y de um ponto da borda
     */
    public Circulo(int xCentro, int yCentro, int xBorda, int yBorda) {
        this(xCentro, yCentro, calcularRaio(xCentro, yCentro, xBorda, yBorda));
    }

    /**
     * Calcula o raio a partir do centro e de um ponto da borda.
     *
     * @param xCentro valor de xCentro
     * @param yCentro valor de yCentro
     * @param xBorda valor de xBorda
     * @param yBorda valor de yBorda
     * @return valor retornado
     */
    private static int calcularRaio(int xCentro, int yCentro, int xBorda, int yBorda) {
        double dx = xBorda - xCentro;
        double dy = yBorda - yCentro;
        return (int)Math.round(Math.sqrt(dx * dx + dy * dy));
    }

    /**
     * Retorna o valor de Centro.
     * @return valor retornado
     */
    public Ponto getCentro() {
        return this.centro;
    }

    /**
     * Altera o valor de Centro.
     *
     * @param centro valor de centro
     */
    public void setCentro(Ponto centro) {
        this.centro = centro;
    }

    /**
     * Retorna o valor de Raio.
     * @return valor retornado
     */
    public int getRaio() {
        return this.raio;
    }

    /**
     * Altera o valor de Raio.
     *
     * @param raio valor de raio
     */
    public void setRaio(int raio) {
        if (raio < 0) {
            this.raio = 0;
        } else {
            this.raio = raio;
        }
    }

    /**
     * Retorna a representacao textual do objeto.
     * @return valor retornado
     */
    public String toString() {
        return "Circulo [centro=" + getCentro() + ", raio=" + getRaio() + "]";
    }
}