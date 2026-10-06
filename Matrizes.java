/**
 * Operacoes com matrizes 3x3 e vetores homogeneos.
 * 
 * @author Heitor de Sousa Cavalcanti, Kaua Bezerra Brito, Marcelo Liao,
 *         Rodrigo Ward Leite
 * @version 06.10.2026
 */
public class Matrizes {
    /**
     * Multiplica uma matriz 3x3 por um vetor homogeneo 3x1.
     *
     * @param matriz Matriz 3x3
     * @param vetor  Vetor homogeneo 3x1
     * @return Vetor resultante da multiplicacao
     */
    public static double[] multiplicarMatrizVetor(double[][] matriz, double[] vetor) {
        if (matriz.length != 3 || matriz[0].length != 3 || vetor.length != 3) {
            throw new IllegalArgumentException("A matriz deve ser 3x3 e o vetor deve ter 3 elementos.");
        }

        // Inicializa o vetor resultante
        double[] resultado = new double[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                resultado[i] += matriz[i][j] * vetor[j];
            }
        }
        return resultado;
    }

    /**
     * Multiplica duas matrizes 3x3.
     *
     * @param ma Matriz 3x3
     * @param mb Matriz 3x3
     * @return Matriz resultante da multiplicacao
     */
    public static double[][] multiplicarMatrizes3x3(double[][] ma, double[][] mb) {
        if (ma.length != 3 || ma[0].length != 3 || mb.length != 3 || mb[0].length != 3) {
            throw new IllegalArgumentException("As matrizes devem ser 3x3.");
        }

        double[][] resultado = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    resultado[i][j] += ma[i][k] * mb[k][j];
                }
            }
        }
        return resultado;
    }
}
