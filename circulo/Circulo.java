package circulo;

import ponto.Ponto;

/**
 * Representacao matematica de um circulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260824
 */
public class Circulo {
    private Ponto centro;
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

    private static int calcularRaio(int xCentro, int yCentro, int xBorda, int yBorda) {
        double dx = xBorda - xCentro;
        double dy = yBorda - yCentro;
        return (int)Math.round(Math.sqrt(dx * dx + dy * dy));
    }

    public Ponto getCentro() {
        return this.centro;
    }

    public void setCentro(Ponto centro) {
        this.centro = centro;
    }

    public int getRaio() {
        return this.raio;
    }

    public void setRaio(int raio) {
        if (raio < 0) {
            this.raio = 0;
        } else {
            this.raio = raio;
        }
    }

    public String toString() {
        return "Circulo [centro=" + getCentro() + ", raio=" + getRaio() + "]";
    }
}
