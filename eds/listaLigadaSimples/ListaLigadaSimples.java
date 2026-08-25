package eds.listaLigadaSimples;

/**
 * Lista ligada simples generica usada para armazenar os primitivos desenhados.
 *
 * @author Kaua Bezerra Brito
 * @version 20260824
 */
public class ListaLigadaSimples<T> implements IListaLigadaSimples<T> {
    private No inicio;
    private No fim;
    private int qtdNos;

    /**
     * No de uma lista ligada simples.
     */
    private class No {
        private T conteudo;
        private No proximo;

        public No(T conteudo) {
            setConteudo(conteudo);
            setProximo(null);
        }

        public void setConteudo(T conteudo) {
            this.conteudo = conteudo;
        }

        public void setProximo(No proximo) {
            this.proximo = proximo;
        }

        public T getConteudo() {
            return this.conteudo;
        }

        public No getProximo() {
            return this.proximo;
        }

        public String toString() {
            return conteudo.toString();
        }
    }

    /**
     * Constroi uma lista ligada simples vazia.
     */
    public ListaLigadaSimples() {
        setInicio(null);
        setFim(null);
        setQtdNos(0);
    }

    private void setInicio(No inicio) {
        this.inicio = inicio;
    }

    private No getInicio() {
        return this.inicio;
    }

    private No getFim() {
        return this.fim;
    }

    private void setFim(No fim) {
        this.fim = fim;
    }

    /**
     * Retorna a quantidade de nos da lista.
     *
     * @return quantidade de nos
     */
    public int getQtdNos() {
        return this.qtdNos;
    }

    private void setQtdNos(int qtdNos) {
        this.qtdNos = qtdNos;
    }

    /**
     * Verifica se a lista esta vazia.
     *
     * @return true quando a lista esta vazia
     */
    public boolean estaVazia() {
        return getQtdNos() == 0 && getInicio() == null && getFim() == null;
    }

    /**
     * Insere um elemento no inicio da lista.
     *
     * @param elem elemento a ser inserido
     */
    public void inserirInicio(T elem) {
        No novo = new No(elem);

        if (estaVazia()) {
            setInicio(novo);
            setFim(novo);
        } else {
            novo.setProximo(getInicio());
            setInicio(novo);
        }

        setQtdNos(getQtdNos() + 1);
    }

    /**
     * Insere um elemento no final da lista.
     *
     * @param elem elemento a ser inserido
     */
    public void inserirFim(T elem) {
        No novo = new No(elem);

        if (estaVazia()) {
            setInicio(novo);
            setFim(novo);
        } else {
            getFim().setProximo(novo);
            setFim(novo);
        }

        setQtdNos(getQtdNos() + 1);
    }

    /**
     * Remove o primeiro elemento da lista.
     *
     * @return elemento removido
     */
    public T removerInicio() {
        No aux;
        T obj = null;

        if (!estaVazia()) {
            if (getInicio() == getFim()) {
                aux = getInicio();
                setInicio(null);
                setFim(null);
            } else {
                aux = getInicio();
                setInicio(aux.getProximo());
                aux.setProximo(null);
            }

            setQtdNos(getQtdNos() - 1);
            obj = aux.getConteudo();
        }

        return obj;
    }

    /**
     * Remove o ultimo elemento da lista.
     *
     * @return elemento removido
     */
    public T removerFim() {
        No ant = getInicio();
        No aux;
        T obj = null;

        if (!estaVazia()) {
            if (getInicio() == getFim()) {
                aux = getInicio();
                setInicio(null);
                setFim(null);
            } else {
                while (ant.getProximo() != getFim()) {
                    ant = ant.getProximo();
                }

                ant.setProximo(null);
                aux = getFim();
                setFim(ant);
            }

            setQtdNos(getQtdNos() - 1);
            obj = aux.getConteudo();
        }

        return obj;
    }

    /**
     * Retorna o elemento armazenado em uma posicao sem remove-lo da lista.
     *
     * @param indice posicao do elemento
     * @return elemento encontrado
     */
    public T obter(int indice) {
        No atual = getInicio();

        if (indice < 0 || indice >= getQtdNos()) {
            return null;
        }

        for (int i = 0; i < indice; i++) {
            atual = atual.getProximo();
        }

        return atual.getConteudo();
    }

    /**
     * Monta a representacao textual da lista.
     *
     * @return elementos da lista
     */
    public String toString() {
        No temp = getInicio();
        String valores = "[";

        for (int i = 0; i < getQtdNos(); i++) {
            valores += temp.getConteudo();

            if (i < getQtdNos() - 1) {
                valores += ",";
            }

            temp = temp.getProximo();
        }

        valores += "]";
        return valores;
    }
}
