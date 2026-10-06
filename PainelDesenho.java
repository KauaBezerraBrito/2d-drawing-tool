import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

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
import retangulo.QuadrilateroGr;
import triangulo.TrianguloGr;

/**
 * Cria desenhos de acordo com o tipo e eventos do mouse
 * 
 * @author Heitor de Sousa Cavalcanti, Kaua Bezerra Brito, Marcelo Liao, Rodrigo
 *         Ward Leite
 * @version 06.10.2026
 */
public class PainelDesenho extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

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

    /** Ativa a previa que acompanha o mouse entre os cliques. */
    private boolean modoElastico = false;
    private int xMouse;
    private int yMouse;

    /** Novos desenhos ficam visiveis mesmo depois de limpar ou filtrar a tela. */
    private int inicioNovos = 0;

    /** Estrutura de dados que armazena os primitivos desenhados. */
    ListaLigadaSimples<PrimitivoGrafico> primitivos = new ListaLigadaSimples<PrimitivoGrafico>();

    /** Filtro usado pelo combo de redesenho. */
    TipoPrimitivo filtroRedesenho = TipoPrimitivo.NENHUM;

    /** Indica se o modo de selecao para apagar esta ativo. */
    private boolean modoApagar = false;

    /** Indica se o modo de espelhamento esta ativo. */
    private boolean modoEspelhamento = false;

    /** Indice do primitivo selecionado para espelhamento. */
    private int indiceEspelhamento = -1;

    /** Primeiro ponto da reta de espelhamento. */
    private int eixoX1;

    /** Primeiro ponto da reta de espelhamento. */
    private int eixoY1;

    /** Segundo ponto da reta de espelhamento. */
    private int eixoX2;

    /** Segundo ponto da reta de espelhamento. */
    private int eixoY2;

    /** Indica quantos pontos da reta de espelhamento ja foram definidos. */
    private int qtdeCliquesEixo = 0;

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
        cancelarEspelhamento();
        repaint();
    }

    /** Liga ou desliga o modo elastico e cancela o desenho em andamento. */
    public void setModoElastico(boolean ativo) {
        modoElastico = ativo;
        qtdeCliques = 0;
        requestFocusInWindow();
        repaint();
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
        repaint();
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
        repaint();
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
        desenharDestaqueEspelhamento(g);
        desenharPrevia(g);
        desenharEixoEspelhamento(g);
    }

    /** A previa e temporaria: nao entra na lista de primitivos. */
    private void desenharPrevia(Graphics g) {
        if (!modoElastico || modoApagar || qtdeCliques == 0) {
            return;
        }

        PrimitivoGrafico previa = null;
        if (tipo == TipoPrimitivo.RETA || (tipo == TipoPrimitivo.TRIANGULO && qtdeCliques == 1)) {
            previa = new RetaGr(x1, y1, xMouse, yMouse, corAtual, "", esp);
        } else if (tipo == TipoPrimitivo.CIRCULO) {
            previa = new CirculoGr(x1, y1, xMouse, yMouse, corAtual, "", esp);
        } else if (tipo == TipoPrimitivo.RETANGULO) {
            previa = new RetanguloGr(x1, y1, xMouse, yMouse, corAtual, "", esp);
        } else if (tipo == TipoPrimitivo.TRIANGULO) {
            previa = new TrianguloGr(x1, y1, x2, y2, xMouse, yMouse, corAtual, "", esp);
        }
        if (previa != null) {
            previa.desenhar(g);
        }
    }

    /**
     * Limpa somente a tela. Os primitivos continuam armazenados na ED.
     */
    public void limparTela() {
        filtroRedesenho = TipoPrimitivo.NENHUM;
        inicioNovos = primitivos.getQtdNos();
        modoApagar = false;
        cancelarEspelhamento();
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
        inicioNovos = primitivos.getQtdNos();
        qtdeCliques = 0;
        modoApagar = false;
        cancelarEspelhamento();
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
            inicioNovos = primitivos.getQtdNos();
            qtdeCliques = 0;
            modoApagar = false;
            repaint();
            msg.setText("Desenho carregado de: " + caminho + " - ED: " + primitivos.getQtdNos());
        } catch (IOException e) {
            msg.setText("Erro ao carregar: " + e.getMessage());
        }
    }

    /**
     * Ativa o modo de espelhamento. O usuario seleciona um primitivo com o
     * primeiro clique e depois define a reta de espelhamento com dois cliques.
     */
    public void iniciarModoEspelhamento() {
        modoApagar = false;
        modoEspelhamento = true;
        indiceEspelhamento = -1;
        qtdeCliquesEixo = 0;
        qtdeCliques = 0;
        requestFocusInWindow();
        repaint();
        msg.setText("Espelhar: clique no primitivo que deseja selecionar.");
    }

    /** Cancela o modo de espelhamento e limpa seu estado temporario. */
    private void cancelarEspelhamento() {
        modoEspelhamento = false;
        indiceEspelhamento = -1;
        qtdeCliquesEixo = 0;
    }

    /** Desenha o destaque do primitivo selecionado para espelhamento. */
    private void desenharDestaqueEspelhamento(Graphics g) {
        if (!modoEspelhamento || indiceEspelhamento < 0 || indiceEspelhamento >= primitivos.getQtdNos()) {
            return;
        }

        criarDestaque(primitivos.obter(indiceEspelhamento), Color.RED).desenhar(g);
    }

    /** Desenha a reta de espelhamento definida parcialmente ou completamente. */
    private void desenharEixoEspelhamento(Graphics g) {
        if (!modoEspelhamento || qtdeCliquesEixo == 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2f));
            int ex = (qtdeCliquesEixo == 1) ? xMouse : eixoX2;
            int ey = (qtdeCliquesEixo == 1) ? yMouse : eixoY2;
            g2.drawLine(eixoX1, eixoY1, ex, ey);
        } finally {
            g2.dispose();
        }
    }

    /**
     * Seleciona o primitivo que esta mais proximo do ponto informado.
     *
     * @param x coordenada x do clique
     * @param y coordenada y do clique
     * @return indice do primitivo ou -1 quando nenhum foi encontrado
     */
    private int localizarPrimitivo(int x, int y) {
        final double tolerancia = 9.0;

        // Percorre de tras para frente para selecionar primeiro o primitivo
        // visualmente mais recente.
        for (int i = primitivos.getQtdNos() - 1; i >= 0; i--) {
            PrimitivoGrafico p = primitivos.obter(i);

            if (p instanceof PontoGr) {
                PontoGr ponto = (PontoGr) p;
                if (distancia(x, y, ponto.getX(), ponto.getY()) <= Math.max(tolerancia, ponto.getDiametro())) {
                    return i;
                }
            } else if (p instanceof RetaGr) {
                RetaGr reta = (RetaGr) p;
                if (distanciaSegmento(x, y, reta.getP1().getX(), reta.getP1().getY(),
                        reta.getP2().getX(), reta.getP2().getY()) <= tolerancia) {
                    return i;
                }
            } else if (p instanceof CirculoGr) {
                CirculoGr c = (CirculoGr) p;
                double d = distancia(x, y, c.getCentro().getX(), c.getCentro().getY());
                if (Math.abs(d - c.getRaio()) <= tolerancia) {
                    return i;
                }
            } else if (p instanceof QuadrilateroGr) {
                QuadrilateroGr q = (QuadrilateroGr) p;
                if (pertenceAoQuadrilatero(q, x, y, tolerancia)) {
                    return i;
                }
            } else if (p instanceof RetanguloGr) {
                RetanguloGr r = (RetanguloGr) p;
                if (pertenceAoRetangulo(r, x, y, tolerancia)) {
                    return i;
                }
            } else if (p instanceof TrianguloGr) {
                TrianguloGr t = (TrianguloGr) p;
                if (distanciaSegmento(x, y, t.getP1().getX(), t.getP1().getY(),
                        t.getP2().getX(), t.getP2().getY()) <= tolerancia
                        || distanciaSegmento(x, y, t.getP2().getX(), t.getP2().getY(),
                                t.getP3().getX(), t.getP3().getY()) <= tolerancia
                        || distanciaSegmento(x, y, t.getP3().getX(), t.getP3().getY(),
                                t.getP1().getX(), t.getP1().getY()) <= tolerancia) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Verifica se o ponto (x, y) esta proximo das bordas do retangulo r,
     * considerando
     * 
     * @param r          Retangulo a ser verificado
     * @param x          Coordenada x do ponto
     * @param y          Coordenada y do ponto
     * @param tolerancia Tolerancia para considerar o ponto como proximo
     * 
     * @return true se o ponto estiver proximo das bordas do retangulo, false caso
     *         contrario
     */
    private boolean pertenceAoRetangulo(RetanguloGr r, double x, double y, double tolerancia) {
        double x1 = r.getXMin();
        double y1 = r.getYMin();
        double x2 = r.getXMax();
        double y2 = r.getYMax();

        return distanciaSegmento(x, y, x1, y1, x2, y1) <= tolerancia
                || distanciaSegmento(x, y, x2, y1, x2, y2) <= tolerancia
                || distanciaSegmento(x, y, x2, y2, x1, y2) <= tolerancia
                || distanciaSegmento(x, y, x1, y2, x1, y1) <= tolerancia;
    }

    /**
     * Verifica se o ponto (x, y) esta proximo das bordas do quadrilatero q,
     * considerando
     * 
     * @param q          Quadrilatero a ser verificado
     * @param x          Coordenada x do ponto
     * @param y          Coordenada y do ponto
     * @param tolerancia Tolerancia para considerar o ponto como proximo
     * 
     * @return true se o ponto estiver proximo das bordas do quadrilatero, false
     *         caso contrario
     */
    private boolean pertenceAoQuadrilatero(QuadrilateroGr q, double x, double y, double tolerancia) {
        return distanciaSegmento(x, y, q.getP1().getX(), q.getP1().getY(), q.getP2().getX(),
                q.getP2().getY()) <= tolerancia
                || distanciaSegmento(x, y, q.getP2().getX(), q.getP2().getY(), q.getP3().getX(),
                        q.getP3().getY()) <= tolerancia
                || distanciaSegmento(x, y, q.getP3().getX(), q.getP3().getY(), q.getP4().getX(),
                        q.getP4().getY()) <= tolerancia
                || distanciaSegmento(x, y, q.getP4().getX(), q.getP4().getY(), q.getP1().getX(),
                        q.getP1().getY()) <= tolerancia;
    }

    /**
     * Calcula a distancia entre dois pontos (x1, y1) e (x2, y2).
     * 
     * @param x1 Coordenada x do primeiro ponto
     * @param y1 Coordenada y do primeiro ponto
     * @param x2 Coordenada x do segundo ponto
     * @param y2 Coordenada y do segundo ponto
     * @return Distancia entre os dois pontos
     */
    private double distancia(double x1, double y1, double x2, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    /**
     * Calcula a distancia entre um ponto (px, py) e um segmento de reta definido
     * pelos pontos (x1, y1) e (x2, y2).
     * 
     * @param px Coordenada x do ponto
     * @param py Coordenada y do ponto
     * @param x1 Coordenada x do primeiro ponto do segmento
     * @param y1 Coordenada y do primeiro ponto do segmento
     * @param x2 Coordenada x do segundo ponto do segmento
     * @param y2 Coordenada y do segundo ponto do segmento
     * 
     * @return Distancia entre o ponto e o segmento
     */
    private double distanciaSegmento(double px, double py, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        if (dx == 0 && dy == 0) {
            return distancia(px, py, x1, y1);
        }

        double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));

        return distancia(px, py, x1 + t * dx, y1 + t * dy);
    }

    /**
     * Reflete um ponto em relacao a uma reta usando transformacoes
     * geometricas compostas em coordenadas homogeneas.
     * 
     * @param ponto Ponto a ser refletido
     * @param x1    Coordenada x do primeiro ponto da reta
     * @param y1    Coordenada y do primeiro ponto da reta
     * @param x2    Coordenada x do segundo ponto da reta
     * @param y2    Coordenada y do segundo ponto da reta
     * @param cor   Cor do ponto refletido
     * @param esp   Espessura do ponto refletido
     * 
     * @return Ponto refletido
     */
    private PontoGr refletirPonto(Ponto ponto, int x1, int y1, int x2, int y2, Color cor, int esp) {
        double[] resultado = TransfGeometricaComposta.espelhamentoRetaQquer(
                ponto.getX(), ponto.getY(),
                new Ponto(x1, y1), new Ponto(x2, y2));

        return new PontoGr(
                (int) Math.round(resultado[0]),
                (int) Math.round(resultado[1]),
                cor, esp);
    }

    /**
     * Cria o primitivo refletido pela reta selecionada.
     * O resultado e um novo primitivo, mantendo o original na ED.
     * 
     * @param original Primitivo a ser refletido
     * 
     * @return Primitivo refletido ou null se a reta de espelhamento nao
     */
    private PrimitivoGrafico criarEspelhado(PrimitivoGrafico original) {
        if (eixoX1 == eixoX2 && eixoY1 == eixoY2) {
            return null;
        }

        if (original instanceof PontoGr) {
            PontoGr p = (PontoGr) original;
            return refletirPonto(p, eixoX1, eixoY1, eixoX2, eixoY2,
                    p.getCorPto(), p.getDiametro());
        }

        if (original instanceof RetaGr) {
            RetaGr r = (RetaGr) original;
            PontoGr p1 = refletirPonto(r.getP1(), eixoX1, eixoY1, eixoX2, eixoY2,
                    r.getCorReta(), r.getEspReta());
            PontoGr p2 = refletirPonto(r.getP2(), eixoX1, eixoY1, eixoX2, eixoY2,
                    r.getCorReta(), r.getEspReta());
            return new RetaGr((int) p1.getX(), (int) p1.getY(),
                    (int) p2.getX(), (int) p2.getY(),
                    r.getCorReta(), "", r.getEspReta());
        }

        if (original instanceof CirculoGr) {
            CirculoGr c = (CirculoGr) original;
            PontoGr centro = refletirPonto(c.getCentro(), eixoX1, eixoY1, eixoX2, eixoY2,
                    c.getCorCirculo(), c.getEspCirculo());
            return new CirculoGr((int) centro.getX(), (int) centro.getY(),
                    (int) centro.getX() + c.getRaio(), (int) centro.getY(),
                    c.getCorCirculo(), "", c.getEspCirculo());
        }

        if (original instanceof QuadrilateroGr) {
            QuadrilateroGr q = (QuadrilateroGr) original;
            PontoGr p1 = refletirPonto(q.getP1(), eixoX1, eixoY1, eixoX2, eixoY2,
                    q.getCorRetangulo(), q.getEspRetangulo());
            PontoGr p2 = refletirPonto(q.getP2(), eixoX1, eixoY1, eixoX2, eixoY2,
                    q.getCorRetangulo(), q.getEspRetangulo());
            PontoGr p3 = refletirPonto(q.getP3(), eixoX1, eixoY1, eixoX2, eixoY2,
                    q.getCorRetangulo(), q.getEspRetangulo());
            PontoGr p4 = refletirPonto(q.getP4(), eixoX1, eixoY1, eixoX2, eixoY2,
                    q.getCorRetangulo(), q.getEspRetangulo());
            return new QuadrilateroGr(p1, p2, p3, p4,
                    q.getCorRetangulo(), "", q.getEspRetangulo());
        }

        if (original instanceof RetanguloGr) {
            RetanguloGr r = (RetanguloGr) original;
            Ponto p1 = new Ponto(r.getXMin(), r.getYMin());
            Ponto p2 = new Ponto(r.getXMax(), r.getYMin());
            Ponto p3 = new Ponto(r.getXMax(), r.getYMax());
            Ponto p4 = new Ponto(r.getXMin(), r.getYMax());

            PontoGr rp1 = refletirPonto(p1, eixoX1, eixoY1, eixoX2, eixoY2,
                    r.getCorRetangulo(), r.getEspRetangulo());
            PontoGr rp2 = refletirPonto(p2, eixoX1, eixoY1, eixoX2, eixoY2,
                    r.getCorRetangulo(), r.getEspRetangulo());
            PontoGr rp3 = refletirPonto(p3, eixoX1, eixoY1, eixoX2, eixoY2,
                    r.getCorRetangulo(), r.getEspRetangulo());
            PontoGr rp4 = refletirPonto(p4, eixoX1, eixoY1, eixoX2, eixoY2,
                    r.getCorRetangulo(), r.getEspRetangulo());

            return new QuadrilateroGr(rp1, rp2, rp3, rp4,
                    r.getCorRetangulo(), "", r.getEspRetangulo());
        }

        if (original instanceof TrianguloGr) {
            TrianguloGr t = (TrianguloGr) original;
            PontoGr p1 = refletirPonto(t.getP1(), eixoX1, eixoY1, eixoX2, eixoY2,
                    t.getCorTriangulo(), t.getEspTriangulo());
            PontoGr p2 = refletirPonto(t.getP2(), eixoX1, eixoY1, eixoX2, eixoY2,
                    t.getCorTriangulo(), t.getEspTriangulo());
            PontoGr p3 = refletirPonto(t.getP3(), eixoX1, eixoY1, eixoX2, eixoY2,
                    t.getCorTriangulo(), t.getEspTriangulo());
            return new TrianguloGr((int) p1.getX(), (int) p1.getY(),
                    (int) p2.getX(), (int) p2.getY(),
                    (int) p3.getX(), (int) p3.getY(),
                    t.getCorTriangulo(), "", t.getEspTriangulo());
        }

        return null;
    }

    /**
     * Processa o clique do modo de apagar usando a mesma selecao por ponto
     * utilizada pelo espelhamento.
     */
    private void processarCliqueApagar(MouseEvent e) {
        int indice = localizarPrimitivo(e.getX(), e.getY());

        if (indice < 0) {
            msg.setText("Nenhum primitivo encontrado nesse ponto. Clique sobre um primitivo.");
            return;
        }

        primitivos.remover(indice);
        if (indice < inicioNovos) {
            inicioNovos--;
        }

        modoApagar = false;
        qtdeCliques = 0;
        repaint();
        msg.setText("Primitivo removido. ED: " + primitivos.getQtdNos());
    }

    /**
     * Processa os cliques do modo de espelhamento.
     */
    private boolean processarCliqueEspelhamento(MouseEvent e) {
        if (!modoEspelhamento) {
            return false;
        }

        int x = e.getX();
        int y = e.getY();

        if (indiceEspelhamento < 0) {
            indiceEspelhamento = localizarPrimitivo(x, y);
            if (indiceEspelhamento < 0) {
                msg.setText("Nenhum primitivo encontrado nesse ponto. Clique sobre um primitivo.");
            } else {
                qtdeCliquesEixo = 0;
                msg.setText("Primitivo selecionado. Agora clique no primeiro ponto da reta de espelhamento.");
                repaint();
            }
            return true;
        }

        if (qtdeCliquesEixo == 0) {
            eixoX1 = x;
            eixoY1 = y;
            qtdeCliquesEixo = 1;
            xMouse = x;
            yMouse = y;
            msg.setText("Clique no segundo ponto da reta de espelhamento.");
            repaint();
            return true;
        }

        eixoX2 = x;
        eixoY2 = y;

        if (eixoX1 == eixoX2 && eixoY1 == eixoY2) {
            msg.setText("A reta de espelhamento precisa ter dois pontos diferentes.");
            return true;
        }

        PrimitivoGrafico espelhado = criarEspelhado(primitivos.obter(indiceEspelhamento));

        if (espelhado != null) {
            primitivos.inserirFim(espelhado);
            inicioNovos = primitivos.getQtdNos() - 1;
            filtroRedesenho = TipoPrimitivo.TODOS;
            msg.setText("Primitivo espelhado e incluido na ED. ED: " + primitivos.getQtdNos());
        }

        cancelarEspelhamento();
        repaint();
        return true;
    }

    /**
     * Ativa o modo de apagar. O usuario seleciona diretamente o primitivo
     * clicando sobre ele, usando a mesma logica de selecao do espelhamento.
     */
    public void iniciarModoApagar() {
        if (primitivos.getQtdNos() == 0) {
            msg.setText("Nao ha primitivos para apagar.");
            return;
        }

        modoEspelhamento = false;
        modoApagar = true;
        qtdeCliques = 0;
        filtroRedesenho = TipoPrimitivo.TODOS;
        requestFocusInWindow();
        repaint();
        msg.setText("Apagar: clique no primitivo que deseja remover.");
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
        } else if (original instanceof QuadrilateroGr) {
            QuadrilateroGr q = (QuadrilateroGr) original;
            return new QuadrilateroGr(
                    new PontoGr((int) q.getP1().getX(), (int) q.getP1().getY(), corDestaque),
                    new PontoGr((int) q.getP2().getX(), (int) q.getP2().getY(), corDestaque),
                    new PontoGr((int) q.getP3().getX(), (int) q.getP3().getY(), corDestaque),
                    new PontoGr((int) q.getP4().getX(), (int) q.getP4().getY(), corDestaque),
                    corDestaque, "", q.getEspRetangulo());
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
        requestFocusInWindow();
        if (modoApagar) {
            processarCliqueApagar(e);
            return;
        }
        xMouse = e.getX();
        yMouse = e.getY();

        if (processarCliqueEspelhamento(e)) {
            return;
        }

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
        repaint();
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
        repaint();
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
        mouseMoved(e);
    }

    /**
     * Evento mouseMoved: escreve mensagem no rodape (x, y) do mouse
     *
     * @param e dados do evento do mouse
     */
    public void mouseMoved(MouseEvent e) {
        if (modoApagar) {
            return;
        }
        xMouse = e.getX();
        yMouse = e.getY();
        if (modoEspelhamento && qtdeCliquesEixo == 1) {
            repaint();
            msg.setText("Definindo reta de espelhamento: (" + eixoX1 + ", " + eixoY1 + ") -> (" + xMouse + ", " + yMouse
                    + ")");
            return;
        }
        if (modoElastico && qtdeCliques > 0) {
            repaint();
        }
        this.msg.setText("(" + e.getX() + ", " + e.getY() + ") - " + getTipo() + " - ED: " + primitivos.getQtdNos());
    }

    /**
     * Desenha os primitivos armazenados de acordo com o filtro escolhido.
     *
     * @param g      biblioteca para desenhar em modo grafico
     * @param filtro tipo de primitivo que deve ser desenhado
     */
    public void desenharPrimitivosArmazenados(Graphics g, TipoPrimitivo filtro) {
        for (int i = 0; i < primitivos.getQtdNos(); i++) {
            PrimitivoGrafico primitivo = primitivos.obter(i);

            if (i >= inicioNovos || filtro == TipoPrimitivo.TODOS || primitivo.getTipo().equals(filtro.name())) {
                if (modoApagar) {
                    primitivo.desenhar(g);
                } else {
                    primitivo.desenhar(g);
                }
            }
        }
    }

    /**
     * Trata o evento de tecla pressionada. Esc cancela modos de selecao.
     *
     * @param e dados do evento de teclado
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_ESCAPE) {
            return;
        }

        if (modoEspelhamento) {
            cancelarEspelhamento();
            repaint();
            msg.setText("Espelhamento cancelado.");
        } else if (modoApagar) {
            modoApagar = false;
            repaint();
            msg.setText("Selecao para apagar cancelada. ED: " + primitivos.getQtdNos());
        } else {
            qtdeCliques = 0;
            repaint();
            msg.setText("Desenho em andamento cancelado.");
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
