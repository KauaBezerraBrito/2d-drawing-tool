package circulo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Contem metodos para desenhar figuras com circulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public class FiguraCirculos {
    /**
     * Desenha um circulo a partir do centro e de um ponto da borda.
     *
     * @param g contexto grafico
     * @param xCentro coordenada x do centro
     * @param yCentro coordenada y do centro
     * @param xBorda coordenada x de um ponto da borda
     * @param yBorda coordenada y de um ponto da borda
     * @param nome nome do circulo
     * @param esp espessura do circulo
     * @param cor cor do circulo
     */
    public static void desenharCirculo(Graphics g, int xCentro, int yCentro, int xBorda, int yBorda, String nome, int esp, Color cor) {
        CirculoGr circulo = new CirculoGr(xCentro, yCentro, xBorda, yBorda, cor, nome, esp);
        circulo.desenharCirculo(g);
    }
}