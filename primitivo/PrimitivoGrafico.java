package primitivo;

import java.awt.Graphics;

/**
 * Define o comportamento comum dos primitivos graficos armazenados na ED.
 *
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public interface PrimitivoGrafico {
    /**
     * Desenha o primitivo no contexto grafico recebido.
     *
     * @param g contexto grafico
     */
    void desenhar(Graphics g);

    /**
     * Retorna o tipo do primitivo.
     *
     * @return tipo do primitivo
     */
    String getTipo();
}