import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;

import circulo.CirculoGr;
import eds.listaLigadaSimples.ListaLigadaSimples;
import persistencia.PersistenciaJSON;
import ponto.Ponto;
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
public class PainelDesenho extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    /** Cor usada para destacar o primitivo selecionado no modo de apagar. */
    private static final Color COR_DESTAQUE = Color.RED;

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

    /** Indica se o modo de selecao para apagar esta ativo. */
    private boolean modoApagar = false;

    /** Tipo de primitivo sendo navegado no modo de apagar. */
    private TipoPrimitivo tipoApagar;

    /**
     * Indices reais (dentro de "primitivos") dos itens do tipo escolhido para
     * apagar.
     */
    private int[] indicesApagar;

    /** Posicao selecionada dentro de indicesApagar. */
    private int posicaoApagar;

    /**
     * Constroi o painel de desenho
     *
     * @param msg      mensagem a ser escrita no rodape do painel
     * @param tipo     tipo atual do primitivo
     * @param corAtual cor atual do primitivo
     * @param esp      espessura atual do primitivo
     */
    public PainelDesenho(JLabel msg, TipoPrimitivo tipo, Color corAtual, int esp) {
        setTipo(tipo);
        setMsg(msg);
        setCorAtual(corAtual);
        setEsp(esp);

        // Adiciona "ouvidor" de eventos de mouse
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Adiciona "ouvidor" de teclado (usado pelo modo de apagar) e
        // garante que o painel possa receber foco/eventos de tecla
        this.setFocusable(true);
        this.addKeyListener(this);

    }

    /**
     * Altera o tipo atual do primitivo
     *
     * @param tipo tipo do primitivo
     */
    public void setTipo(TipoPrimitivo tipo) {
        this.tipo = tipo;
        this.qtdeCliques = 0;
    }

    /**
     * Retorna o tipo do primitivo
     *
     * @return tipo do primitivo
     */
    public TipoPrimitivo getTipo() {
        return this.tipo;
    }

    /**
     * Altera a espessura do primitivo
     *
     * @param esp espessura do primitivo
     */
    public void setEsp(int esp) {
        this.esp = esp;
    }

    /**
     * Retorna a espessura do primitivo
     *
     * @return espessura do primitivo
     */
    public int getEsp() {
        return this.esp;
    }

    /**
     * Altera a cor atual do primitivo
     *
     * @param corAtual cor atual do primitivo
     */
    public void setCorAtual(Color corAtual) {
        this.corAtual = corAtual;
    }

    /**
     * retorna a cor atual do primitivo
     *
     * @return cor atual do primitivo
     */
    public Color getCorAtual() {
        return this.corAtual;
    }

    /**
     * Altera a msg a ser apresentada no rodape
     *
     * @param msg mensagem a ser apresentada
     */
    public void setMsg(JLabel msg) {
        this.msg = msg;
    }

    /**
     * Retorna a mensagem
     *
     * @return mensagem as ser apresentada no rodape
     */
    public JLabel getMsg() {
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
     * Salva os primitivos atualmente armazenados na ED em um arquivo JSON.
     * As coordenadas sao normalizadas de acordo com o tamanho atual do painel.
     *
     * @param caminho caminho do arquivo a ser gravado (ex.: "saves/desenho.json")
     */
    public void salvarArquivo(String caminho) {
        try {
            PersistenciaJSON.salvar(caminho, primitivos, getWidth(), getHeight());
            msg.setText("Desenho salvo em: " + caminho + " - ED: " + primitivos.getQtdNos());
        } catch (IOException e) {
            msg.setText("Erro ao salvar: " + e.getMessage());
        }
    }

    /**
     * Carrega os primitivos gravados em um arquivo JSON, substituindo o desenho
     * atual.
     * As coordenadas normalizadas sao convertidas de volta usando o tamanho atual
     * do painel.
     *
     * @param caminho caminho do arquivo a ser lido
     */
    public void carregarArquivo(String caminho) {
        try {
            primitivos = PersistenciaJSON.carregar(caminho, getWidth(), getHeight());
            filtroRedesenho = TipoPrimitivo.TODOS;
            repaint();
            msg.setText("Desenho carregado de: " + caminho + " - ED: " + primitivos.getQtdNos());
        } catch (IOException e) {
            msg.setText("Erro ao carregar: " + e.getMessage());
        }
    }

    /**
     * Entra no modo de selecao para apagar: percorre a ED e guarda os indices
     * reais dos primitivos que batem com o tipo escolhido. Navegue com as
     * setas esquerda/direita, confirme com Enter ou cancele com Esc.
     *
     * @param tipo tipo de primitivo a navegar (TODOS navega a ED inteira)
     */
    public void iniciarModoApagar(TipoPrimitivo tipo) {
        if (tipo == null || tipo == TipoPrimitivo.NENHUM) {
            msg.setText("Escolha um tipo valido no combo de Redesenhar antes de apagar.");
            return;
        }

        int[] indicesTemp = new int[primitivos.getQtdNos()];
        int qtd = 0;

        for (int i = 0; i < primitivos.getQtdNos(); i++) {
            PrimitivoGrafico p = primitivos.obter(i);

            if (tipo == TipoPrimitivo.TODOS || p.getTipo().equals(tipo.name())) {
                indicesTemp[qtd] = i;
                qtd++;
            }
        }

        if (qtd == 0) {
            msg.setText("Nao ha primitivos do tipo " + tipo + " para apagar.");
            modoApagar = false;
            return;
        }

        indicesApagar = Arrays.copyOf(indicesTemp, qtd);
        posicaoApagar = 0;
        tipoApagar = tipo;
        modoApagar = true;

        // mostra so o tipo que esta sendo apagado (o mesmo efeito do combo de
        // Redesenhar).
        // se o usuario escolheu TODOS, mantem TODOS visivel mesmo.
        filtroRedesenho = tipo;
        requestFocusInWindow();
        repaint();
        atualizarMsgApagar();
    }

    /**
     * Atualiza a mensagem de rodape com a posicao atual do modo de apagar.
     */
    private void atualizarMsgApagar() {
        msg.setText("Apagar " + tipoApagar + ": item " + (posicaoApagar + 1) + " de " + indicesApagar.length
                + "  (setas esquerda/direita navegam, Enter apaga, Esc cancela)");
    }

    /**
     * Monta uma copia temporaria do primitivo informado, na mesma posicao,
     * porem com outra cor. Usada so para desenhar o destaque de selecao;
     * a copia nunca e inserida na ED.
     *
     * @param original    primitivo original guardado na ED
     * @param corDestaque cor a ser usada na copia
     * @return copia do primitivo com a cor de destaque
     */
    private PrimitivoGrafico criarDestaque(PrimitivoGrafico original, Color corDestaque) {
        if (original.getTipo().equals("PONTO")) {
            PontoGr p = (PontoGr) original;
            return new PontoGr((int) p.getX(), (int) p.getY(), corDestaque, p.getDiametro());
        } else if (original.getTipo().equals("RETA")) {
            RetaGr r = (RetaGr) original;
            return new RetaGr((int) r.getP1().getX(), (int) r.getP1().getY(),
                    (int) r.getP2().getX(), (int) r.getP2().getY(), corDestaque, "", r.getEspReta());
        } else if (original.getTipo().equals("TRIANGULO")) {
            TrianguloGr t = (TrianguloGr) original;
            return new TrianguloGr((int) t.getP1().getX(), (int) t.getP1().getY(),
                    (int) t.getP2().getX(), (int) t.getP2().getY(),
                    (int) t.getP3().getX(), (int) t.getP3().getY(), corDestaque, "", t.getEspTriangulo());
        } else if (original.getTipo().equals("RETANGULO")) {
            RetanguloGr r = (RetanguloGr) original;
            return new RetanguloGr((int) r.getP1().getX(), (int) r.getP1().getY(),
                    (int) r.getP2().getX(), (int) r.getP2().getY(), corDestaque, "", r.getEspRetangulo());
        } else if (original.getTipo().equals("CIRCULO")) {
            CirculoGr c = (CirculoGr) original;
            Ponto centro = c.getCentro();
            int xBorda = (int) centro.getX() + c.getRaio();
            int yBorda = (int) centro.getY();
            return new CirculoGr((int) centro.getX(), (int) centro.getY(), xBorda, yBorda, corDestaque, "",
                    c.getEspCirculo());
        }

        return original;
    }

    /**
     * Evento: pressionar do mouse
     *
     * @param e dados do evento
     */
    public void mousePressed(MouseEvent e) {
        PrimitivoGrafico primitivo = null;

        if (tipo == TipoPrimitivo.PONTO) {
            primitivo = new PontoGr(e.getX(), e.getY(), getCorAtual(), getEsp());
        } else if (tipo == TipoPrimitivo.RETA || tipo == TipoPrimitivo.CIRCULO || tipo == TipoPrimitivo.RETANGULO) {
            primitivo = criarPrimitivoComDoisCliques(e);
        } else if (tipo == TipoPrimitivo.TRIANGULO) {
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
        this.msg.setText("(" + e.getX() + ", " + e.getY() + ") - " + getTipo() + " - ED: " + primitivos.getQtdNos());
    }

    /**
     * Desenha os primitivos armazenados de acordo com o filtro escolhido.
     *
     * @param g      biblioteca para desenhar em modo grafico
     * @param filtro tipo de primitivo que deve ser desenhado
     */
    public void desenharPrimitivosArmazenados(Graphics g, TipoPrimitivo filtro) {
        if (filtro == TipoPrimitivo.NENHUM) {
            return;
        }

        for (int i = 0; i < primitivos.getQtdNos(); i++) {
            PrimitivoGrafico primitivo = primitivos.obter(i);

            if (filtro == TipoPrimitivo.TODOS || primitivo.getTipo().equals(filtro.name())) {
                if (modoApagar && i == indicesApagar[posicaoApagar]) {
                    criarDestaque(primitivo, COR_DESTAQUE).desenhar(g);
                } else {
                    primitivo.desenhar(g);
                }
            }
        }
    }

    /**
     * Trata o evento de tecla pressionada. So faz algo quando o modo de
     * apagar esta ativo: seta esquerda/direita navega, Enter apaga o item
     * selecionado, Esc cancela a selecao.
     *
     * @param e dados do evento de teclado
     */
    public void keyPressed(KeyEvent e) {
        if (!modoApagar) {
            return;
        }

        int codigo = e.getKeyCode();

        if (codigo == KeyEvent.VK_LEFT) {
            posicaoApagar = (posicaoApagar - 1 + indicesApagar.length) % indicesApagar.length;
            repaint();
            atualizarMsgApagar();
        } else if (codigo == KeyEvent.VK_RIGHT) {
            posicaoApagar = (posicaoApagar + 1) % indicesApagar.length;
            repaint();
            atualizarMsgApagar();
        } else if (codigo == KeyEvent.VK_ENTER) {
            int indiceReal = indicesApagar[posicaoApagar];
            primitivos.remover(indiceReal);
            modoApagar = false;
            indicesApagar = null;
            repaint();
            msg.setText("Primitivo removido. ED: " + primitivos.getQtdNos());
        } else if (codigo == KeyEvent.VK_ESCAPE) {
            modoApagar = false;
            indicesApagar = null;
            repaint();
            msg.setText("Selecao para apagar cancelada. ED: " + primitivos.getQtdNos());
        }
    }

    /**
     * Trata o evento de tecla solta (nao usado).
     *
     * @param e dados do evento de teclado
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Trata o evento de tecla digitada (nao usado).
     *
     * @param e dados do evento de teclado
     */
    public void keyTyped(KeyEvent e) {
    }
}