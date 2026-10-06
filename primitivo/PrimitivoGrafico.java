package primitivo;

import java.awt.Graphics;

/**
 * Define o comportamento comum dos primitivos graficos armazenados na ED.
 *
 * @author Heitor de Sousa Cavalcanti, Kaua Bezerra Brito, Marcelo Liao, Rodrigo
 *         Ward Leite
 * @version 06.10.2026
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