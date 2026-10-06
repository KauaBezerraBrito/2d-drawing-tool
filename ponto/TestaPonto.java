package ponto;

/**
 * Testa a classe Ponto.
 *
 * @author Heitor de Sousa Cavalcanti, Kaua Bezerra Brito, Marcelo Liao, Rodrigo
 *         Ward Leite
 * @version 06.10.2026
 */
public class TestaPonto {
    /**
     * Inicia a execucao da classe.
     *
     * @param args valor de args
     */
    public static void main(String args[]) {
        Ponto p1 = new Ponto();
        Ponto p2 = new Ponto(1, 1);
        Ponto p3 = new Ponto(2.0, 2.0);
        Ponto p4 = new Ponto(p3);
        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);
        System.out.println("p3 = " + p3);
        System.out.println("p4 = " + p4);
        System.out.println("distancia entre p2 e p3 = " + p2.calcularDistancia(p3));
    }
}