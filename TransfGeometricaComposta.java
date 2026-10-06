import ponto.Ponto;

/**
 * Transformacoes geometricas compostas usadas pelo desenhador.
 * 
 * @author Heitor de Sousa Cavalcanti, Kaua Bezerra Brito, Marcelo Liao, Rodrigo
 *         Ward Leite
 * @version 06.10.2026
 */
public class TransfGeometricaComposta {

    /**
     * Espelha um ponto em relacao a uma reta qualquer definida por p1 e p2.
     *
     * A reta e levada para a origem, alinhada ao eixo X, espelhada em X,
     * e depois as transformacoes sao desfeitas.
     * 
     * @param x  coordenada x do ponto a ser espelhado
     * @param y  coordenada y do ponto a ser espelhado
     * @param p1 ponto 1 da reta
     * @param p2 ponto 2 da reta
     * 
     * @return coordenadas do ponto espelhado
     * @throws IllegalArgumentException se p1 e p2 forem iguais
     */
    public static double[] espelhamentoRetaQquer(double x, double y, Ponto p1, Ponto p2) {
        double[] xy = { x, y, 1 };

        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();

        if (x1 == x2 && y1 == y2) {
            throw new IllegalArgumentException("A reta precisa ser definida por dois pontos diferentes.");
        }

        double theta = Math.atan2(y2 - y1, x2 - x1);

        // Matriz de translacao para levar a reta para a origem
        double[][] trans_1 = {
                { 1, 0, -x1 },
                { 0, 1, -y1 },
                { 0, 0, 1 }
        };

        // Matriz de rotacao para alinhar a reta ao eixo X
        double[][] rot_2 = {
                { Math.cos(-theta), -Math.sin(-theta), 0 },
                { Math.sin(-theta), Math.cos(-theta), 0 },
                { 0, 0, 1 }
        };

        // Matriz de espelhamento em relacao ao eixo X
        double[][] espX_3 = {
                { 1, 0, 0 },
                { 0, -1, 0 },
                { 0, 0, 1 }
        };

        // Matriz de rotacao para desfazer a rotacao inicial
        double[][] rot_4 = {
                { Math.cos(theta), -Math.sin(theta), 0 },
                { Math.sin(theta), Math.cos(theta), 0 },
                { 0, 0, 1 }
        };

        // Matriz de translacao para desfazer a translacao inicial
        double[][] trans_5 = {
                { 1, 0, x1 },
                { 0, 1, y1 },
                { 0, 0, 1 }
        };

        double[][] composta = Matrizes.multiplicarMatrizes3x3(
                Matrizes.multiplicarMatrizes3x3(
                        Matrizes.multiplicarMatrizes3x3(
                                Matrizes.multiplicarMatrizes3x3(trans_5, rot_4),
                                espX_3),
                        rot_2),
                trans_1);

        return Matrizes.multiplicarMatrizVetor(composta, xy);
    }
}
