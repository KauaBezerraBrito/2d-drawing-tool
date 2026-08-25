import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import circulo.CirculoGr;
import eds.listaLigadaSimples.ListaLigadaSimples;
import ponto.PontoGr;
import primitivo.PrimitivoGrafico;
import reta.RetaGr;
import retangulo.RetanguloGr;
import triangulo.TrianguloGr;

/**
 * Cria desenhos de acordo com o tipo e eventos do mouse
 * 
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public class PainelDesenho extends JPanel implements MouseListener, MouseMotionListener {

    /** Label usada para exibir mensagens no rodape. */
    JLabel msg;

    /** Tipo atual de primitivo selecionado. */
    TipoPrimitivo tipo;

    /** Cor atual usada para desenhar novos primitivos. */
    Color corAtual;

    /** Espessura atual usada para desenhar novos primitivos. */
    int esp;

    /** Primeira coordenada x coletada pelo mouse. */
    int x1;

    /** Primeira coordenada y coletada pelo mouse. */
    int y1;

    /** Segunda coordenada x coletada pelo mouse. */
    int x2;

    /** Segunda coordenada y coletada pelo mouse. */
    int y2;

    /** Terceira coordenada x coletada pelo mouse. */
    int x3;

    /** Terceira coordenada y coletada pelo mouse. */
    int y3;

    /** Quantidade de cliques ja coletados para o primitivo atual. */
    int qtdeCliques = 0;

    /** Estrutura de dados que armazena os primitivos desenhados. */
    ListaLigadaSimples<PrimitivoGrafico> primitivos = new ListaLigadaSimples<PrimitivoGrafico>();

    /** Filtro usado pelo combo de redesenho. */
    TipoPrimitivo filtroRedesenho = TipoPrimitivo.NENHUM;
    /**
     * Constroi o painel de desenho
     *
     * @param msg mensagem a ser escrita no rodape do painel
     * @param tipo tipo atual do primitivo
     * @param corAtual cor atual do primitivo
     * @param esp espessura atual do primitivo
     */
    public PainelDesenho(JLabel msg, TipoPrimitivo tipo, Color corAtual, int esp){
        setTipo(tipo);
        setMsg(msg);
        setCorAtual(corAtual);
        setEsp(esp);

        // Adiciona "ouvidor" de eventos de mouse
        this.addMouseListener(this); 
        this.addMouseMotionListener(this);

    }

    /**
     * Altera o tipo atual do primitivo
     *
     * @param tipo tipo do primitivo
     */
    public void setTipo(TipoPrimitivo tipo){
        this.tipo = tipo;
        this.qtdeCliques = 0;
    }

    /**
     * Retorna o tipo do primitivo
     *
     * @return tipo do primitivo
     */
    public TipoPrimitivo getTipo(){
        return this.tipo;
    }

    /**
     * Altera a espessura do primitivo
     *
     * @param esp espessura do primitivo
     */
    public void setEsp(int esp){
        this.esp = esp;
    }

    /**
     * Retorna a espessura do primitivo
     *
     * @return espessura do primitivo
     */
    public int getEsp(){
        return this.esp;
    }

    /**
     * Altera a cor atual do primitivo
     *
     * @param corAtual cor atual do primitivo
     */
    public void setCorAtual(Color corAtual){
        this.corAtual = corAtual;
    }

    /**
     * retorna a cor atual do primitivo
     *
     * @return cor atual do primitivo
     */
    public Color getCorAtual(){
        return this.corAtual;
    }

    /**
     * Altera a msg a ser apresentada no rodape
     *
     * @param msg mensagem a ser apresentada
     */
    public void setMsg(JLabel msg){
        this.msg = msg;
    }

    /**
     * Retorna a mensagem
     *
     * @return mensagem as ser apresentada no rodape
     */
    public JLabel getMsg(){
        return this.msg;
    }

    /**
     * Metodo chamado quando o paint eh acionado
     *
     * @param g biblioteca para desenhar em modo grafico
     */
    public void paintComponent(Graphics g) {   
        super.paintComponent(g);
        desenharPrimitivosArmazenados(g, filtroRedesenho);
    }

    /**
     * Limpa somente a tela. Os primitivos continuam armazenados na ED.
     */
    public void limparTela() {
        filtroRedesenho = TipoPrimitivo.NENHUM;
        qtdeCliques = 0;
        repaint();
    }

    /**
     * Redesenha os primitivos guardados na ED usando o filtro informado.
     *
     * @param filtro tipo que deve ser redesenhado
     */
    public void redesenharPrimitivos(TipoPrimitivo filtro) {
        filtroRedesenho = filtro;
        repaint();
    }

    /**
     * Evento: pressionar do mouse
     *
     * @param e dados do evento
     */
    public void mousePressed(MouseEvent e) { 
        PrimitivoGrafico primitivo = null;

        if (tipo == TipoPrimitivo.PONTO){
            primitivo = new PontoGr(e.getX(), e.getY(), getCorAtual(), getEsp());
        } else if (tipo == TipoPrimitivo.RETA || tipo == TipoPrimitivo.CIRCULO || tipo == TipoPrimitivo.RETANGULO){
            primitivo = criarPrimitivoComDoisCliques(e);
        } else if (tipo == TipoPrimitivo.TRIANGULO){
            primitivo = criarTriangulo(e);
        }

        if (primitivo != null) {
            armazenarEDesenhar(primitivo);
        }
    }     

    /**
     * Cria retas, circulos ou retangulos a partir de dois cliques.
     *
     * @param e valor de e
     * @return valor retornado
     */
    private PrimitivoGrafico criarPrimitivoComDoisCliques(MouseEvent e) {
        PrimitivoGrafico primitivo = null;

        if (qtdeCliques == 0) {
            x1 = e.getX();
            y1 = e.getY();
            qtdeCliques = 1;
        } else {
            x2 = e.getX();
            y2 = e.getY();
            qtdeCliques = 0;

            if (tipo == TipoPrimitivo.RETA) {
                primitivo = new RetaGr(x1, y1, x2, y2, getCorAtual(), "", getEsp());
            } else if (tipo == TipoPrimitivo.CIRCULO) {
                primitivo = new CirculoGr(x1, y1, x2, y2, getCorAtual(), "", getEsp());
            } else if (tipo == TipoPrimitivo.RETANGULO) {
                primitivo = new RetanguloGr(x1, y1, x2, y2, getCorAtual(), "", getEsp());
            }
        }

        return primitivo;
    }

    /**
     * Cria um triangulo a partir de tres cliques.
     *
     * @param e valor de e
     * @return valor retornado
     */
    private PrimitivoGrafico criarTriangulo(MouseEvent e) {
        PrimitivoGrafico primitivo = null;

        if (qtdeCliques == 0) {
            x1 = e.getX();
            y1 = e.getY();
            qtdeCliques = 1;
        } else if (qtdeCliques == 1) {
            x2 = e.getX();
            y2 = e.getY();
            qtdeCliques = 2;
        } else {
            x3 = e.getX();
            y3 = e.getY();
            qtdeCliques = 0;
            primitivo = new TrianguloGr(x1, y1, x2, y2, x3, y3, getCorAtual(), "", getEsp());
        }

        return primitivo;
    }

    /**
     * Armazena o primitivo na ED e desenha imediatamente na tela.
     *
     * @param primitivo valor de primitivo
     */
    private void armazenarEDesenhar(PrimitivoGrafico primitivo) {
        primitivos.inserirFim(primitivo);
        filtroRedesenho = TipoPrimitivo.NENHUM;
        primitivo.desenhar(getGraphics());
    }

    /**
     * Trata o evento de mouse mouseReleased.
     *
     * @param e valor de e
     */
    public void mouseReleased(MouseEvent e) { 
    }           

    /**
     * Trata o evento de mouse mouseClicked.
     *
     * @param e valor de e
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Trata o evento de mouse mouseEntered.
     *
     * @param e valor de e
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Trata o evento de mouse mouseExited.
     *
     * @param e valor de e
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Trata o evento de mouse mouseDragged.
     *
     * @param e valor de e
     */
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * Evento mouseMoved: escreve mensagem no rodape (x, y) do mouse
     *
     * @param e dados do evento do mouse
     */
    public void mouseMoved(MouseEvent e) {
        this.msg.setText("("+e.getX() + ", " + e.getY() + ") - " + getTipo() + " - ED: " + primitivos.getQtdNos());
    }

    /**
     * Desenha os primitivos armazenados de acordo com o filtro escolhido.
     *
     * @param g biblioteca para desenhar em modo grafico
     * @param filtro tipo de primitivo que deve ser desenhado
     */
    public void desenharPrimitivosArmazenados(Graphics g, TipoPrimitivo filtro){
        if (filtro == TipoPrimitivo.NENHUM) {
            return;
        }

        for (int i = 0; i < primitivos.getQtdNos(); i++) {
            PrimitivoGrafico primitivo = primitivos.obter(i);

            if (filtro == TipoPrimitivo.TODOS || primitivo.getTipo().equals(filtro.name())) {
                primitivo.desenhar(g);
            }
        }
    }
}