package triangulo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Contem metodos para desenhar figuras com triangulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public class FiguraTriangulos {
    /**
     * Desenha um triangulo a partir de tres pontos.
     *
     * @param g contexto grafico
     * @param x1 coordenada x do primeiro ponto
     * @param y1 coordenada y do primeiro ponto
     * @param x2 coordenada x do segundo ponto
     * @param y2 coordenada y do segundo ponto
     * @param x3 coordenada x do terceiro ponto
     * @param y3 coordenada y do terceiro ponto
     * @param nome nome do triangulo
     * @param esp espessura do triangulo
     * @param cor cor do triangulo
     */
    public static void desenharTriangulo(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3, String nome, int esp, Color cor) {
        TrianguloGr triangulo = new TrianguloGr(x1, y1, x2, y2, x3, y3, cor, nome, esp);
        triangulo.desenharTriangulo(g);
    }
}