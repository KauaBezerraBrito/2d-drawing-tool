package retangulo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Contem metodos para desenhar figuras com retangulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260824
 */
public class FiguraRetangulos {
    /**
     * Desenha um retangulo a partir de dois cantos opostos.
     *
     * @param g contexto grafico
     * @param x1 coordenada x do primeiro canto
     * @param y1 coordenada y do primeiro canto
     * @param x2 coordenada x do canto oposto
     * @param y2 coordenada y do canto oposto
     * @param nome nome do retangulo
     * @param esp espessura do retangulo
     * @param cor cor do retangulo
     */
    public static void desenharRetangulo(Graphics g, int x1, int y1, int x2, int y2, String nome, int esp, Color cor) {
        RetanguloGr retangulo = new RetanguloGr(x1, y1, x2, y2, cor, nome, esp);
        retangulo.desenharRetangulo(g);
    }
}
