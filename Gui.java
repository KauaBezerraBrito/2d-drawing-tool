import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
/**
 * Cria a interface com o usuario (GUI)
 * 
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
class Gui extends JFrame {
    /** Tipo atual de primitivo selecionado pelo usuario. */
    private TipoPrimitivo tipoAtual = TipoPrimitivo.NENHUM;

    /** Cor atual usada nos novos primitivos. */
    private Color corAtual = Color.BLACK;

    /** Espessura atual usada nos novos primitivos. */
    private int espAtual = 1;

    /** Barra de ferramentas com os comandos da aplicacao. */
    private JToolBar barraComandos = new JToolBar();

    /** Mensagem exibida no rodape da janela. */
    private JLabel msg = new JLabel("Msg: ");

    /** Area central responsavel pelo desenho dos primitivos. */
    private PainelDesenho areaDesenho = new PainelDesenho(msg, tipoAtual, corAtual, 10);

    /** Botao para selecionar o primitivo ponto. */
    private JButton jbPonto = new JButton("Ponto");

    /** Botao para selecionar o primitivo reta. */
    private JButton jbReta = new JButton("Reta");

    /** Botao para selecionar o primitivo circulo. */
    private JButton jbCirculo = new JButton("Circulo");

    /** Botao para selecionar o primitivo retangulo. */
    private JButton jbRetangulo = new JButton("Retangulo");

    /** Botao para selecionar o primitivo triangulo. */
    private JButton jbTriangulo = new JButton("Triangulo");

    /** Botao para limpar somente a tela. */
    private JButton jbLimpar = new JButton("Limpar");

    /** Botao para alterar a cor atual. */
    private JButton jbCor = new JButton("Cor");

    /** Botao para encerrar a aplicacao. */
    private JButton jbSair = new JButton("Sair");

    /** Texto associado ao combo de redesenho. */
    private JLabel jlRedesenhar = new JLabel("   Redesenhar: ");

    /** Combo que define o tipo de primitivo a ser redesenhado a partir da ED. */
    private JComboBox<TipoPrimitivo> jcRedesenhar = new JComboBox<TipoPrimitivo>(new TipoPrimitivo[] {
        TipoPrimitivo.TODOS,
        TipoPrimitivo.PONTO,
        TipoPrimitivo.RETA,
        TipoPrimitivo.CIRCULO,
        TipoPrimitivo.RETANGULO,
        TipoPrimitivo.TRIANGULO
    });

    /** Texto que mostra a espessura atual. */
    private JLabel jlEsp = new JLabel("   Espessura: " + String.format("%-5s", 1));

    /** Slider usado para escolher a espessura dos primitivos. */
    private JSlider jsEsp = new JSlider(1, 50, 1);
    /**
     * Constroi a GUI
     *
     * @param larg largura da janela
     * @param alt altura da janela
     */
    public Gui(int larg, int alt) {
        /**
         * Definicoes de janela
         */
        super("Testa Primitivos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(larg, alt);
        setVisible(true);
        setResizable(false);

        // Adicionando os componentes
        barraComandos.add(jbPonto);
        barraComandos.add(jbReta);
        barraComandos.add(jbCirculo);
        barraComandos.add(jbRetangulo);
        barraComandos.add(jbTriangulo);
        barraComandos.add(jbLimpar); // Botao de Limpar
        barraComandos.add(jbCor); // Botao de Cores

        barraComandos.add(jlEsp); // Label para espessura
        barraComandos.add(jsEsp);    // Slider para espacamento
        areaDesenho.setEsp(espAtual); // define a espessura inicial
        barraComandos.add(jlRedesenhar);
        barraComandos.add(jcRedesenhar);
        barraComandos.add(jbSair); // Botao de Cores

        // adiciona os componentes com os respectivos layouts
        add(barraComandos, BorderLayout.NORTH);                
        add(areaDesenho, BorderLayout.CENTER);                
        add(msg, BorderLayout.SOUTH);

        // Adiciona "tratador" ("ouvidor") de eventos para 
        // cada componente
        jbPonto.addActionListener(e -> {
            tipoAtual = TipoPrimitivo.PONTO;
            areaDesenho.setTipo(tipoAtual);
        });        
        jbReta.addActionListener(e -> {
            tipoAtual = TipoPrimitivo.RETA;
            areaDesenho.setTipo(tipoAtual);
        });        
        jbCirculo.addActionListener(e -> {
            tipoAtual = TipoPrimitivo.CIRCULO;
            areaDesenho.setTipo(tipoAtual);
        });        
        jbRetangulo.addActionListener(e -> {
            tipoAtual = TipoPrimitivo.RETANGULO;
            areaDesenho.setTipo(tipoAtual);
        });
        jbTriangulo.addActionListener(e -> {
            tipoAtual = TipoPrimitivo.TRIANGULO;
            areaDesenho.setTipo(tipoAtual);
        });
        jbLimpar.addActionListener(e -> {
            areaDesenho.limparTela();
            jsEsp.setValue(1); // inicia slider
        });        
        jbCor.addActionListener(e -> {
            Color c = JColorChooser.showDialog(null, "Escolha uma cor", msg.getForeground()); 
            if (c != null){ 
                corAtual = c; // pega do chooserColor 
            }
            areaDesenho.setCorAtual(corAtual); // cor atual
        });  
        jsEsp.addChangeListener(e -> {
            espAtual = jsEsp.getValue();
            jlEsp.setText("   Espessura: " + String.format("%-5s", espAtual));
            areaDesenho.setEsp(espAtual);        
        });        
        jcRedesenhar.addActionListener(e -> {
            TipoPrimitivo filtro = (TipoPrimitivo)jcRedesenhar.getSelectedItem();
            areaDesenho.redesenharPrimitivos(filtro);
        });

        jbSair.addActionListener(e -> {
            System.exit(0);
        });        
    }
}