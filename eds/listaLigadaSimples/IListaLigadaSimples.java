package eds.listaLigadaSimples;

/**
 * Metodos a serem implementados numa Lista Ligada Simples.
 *
 * @author Kaua Bezerra Brito
 * @version 20260824
 */
public interface IListaLigadaSimples<T> {
    boolean estaVazia();

    void inserirInicio(T elem);

    void inserirFim(T elem);

    T removerInicio();

    T removerFim();
}
