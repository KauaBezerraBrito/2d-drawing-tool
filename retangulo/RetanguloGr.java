package retangulo;

import java.awt.Color;
import java.awt.Graphics;

import primitivo.PrimitivoGrafico;
import reta.RetaGr;

/**
 * Representacao grafica de um retangulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260824
 */
public class RetanguloGr extends Retangulo implements PrimitivoGrafico {
    private Color corRetangulo = Color.BLACK;
    private String nomeRetangulo = "";
    private Color corNomeRetangulo = Color.BLACK;
    private int espRetangulo = 1;

    public RetanguloGr(int x1, int y1, int x2, int y2, Color cor, String nome, int esp) {
        super(x1, y1, x2, y2);
        setCorRetangulo(cor);
        setNomeRetangulo(nome);
        setEspRetangulo(esp);
    }

    public Color getCorRetangulo() {
        return this.corRetangulo;
    }

    public void setCorRetangulo(Color corRetangulo) {
        this.corRetangulo = corRetangulo;
    }

    public String getNomeRetangulo() {
        return this.nomeRetangulo;
    }

    public void setNomeRetangulo(String nomeRetangulo) {
        this.nomeRetangulo = nomeRetangulo;
    }

    public Color getCorNomeRetangulo() {
        return this.corNomeRetangulo;
    }

    public void setCorNomeRetangulo(Color corNomeRetangulo) {
        this.corNomeRetangulo = corNomeRetangulo;
    }

    public int getEspRetangulo() {
        return this.espRetangulo;
    }

    public void setEspRetangulo(int espRetangulo) {
        this.espRetangulo = espRetangulo;
    }

    /**
     * Desenha o retangulo usando quatro retas.
     *
     * @param g contexto grafico
     */
    public void desenharRetangulo(Graphics g) {
        int xMin = getXMin();
        int yMin = getYMin();
        int xMax = getXMax();
        int yMax = getYMax();

        new RetaGr(xMin, yMin, xMax, yMin, getCorRetangulo(), "", getEspRetangulo()).desenhar(g);
        new RetaGr(xMax, yMin, xMax, yMax, getCorRetangulo(), "", getEspRetangulo()).desenhar(g);
        new RetaGr(xMax, yMax, xMin, yMax, getCorRetangulo(), "", getEspRetangulo()).desenhar(g);
        new RetaGr(xMin, yMax, xMin, yMin, getCorRetangulo(), "", getEspRetangulo()).desenhar(g);

        g.setColor(getCorNomeRetangulo());
        g.drawString(getNomeRetangulo(), xMin + getEspRetangulo(), yMin - getEspRetangulo());
    }

    public void desenhar(Graphics g) {
        desenharRetangulo(g);
    }

    public String getTipo() {
        return "RETANGULO";
    }
}
