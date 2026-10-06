package eds.listaLigadaSimples;

/**
 * Metodos a serem implementados numa Lista Ligada Simples.
 *
 * @author Heitor de Sousa Cavalcanti, Kaua Bezerra Brito, Marcelo Liao, Rodrigo
 *         Ward Leite
 * @version 06.10.2026
 */
public interface IListaLigadaSimples<T> {
    boolean estaVazia();

    void inserirInicio(T elem);

    void inserirFim(T elem);

    T removerInicio();

    T removerFim();
}