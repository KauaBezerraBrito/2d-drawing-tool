package triangulo;

import java.awt.Color;
import java.awt.Graphics;

import primitivo.PrimitivoGrafico;
import reta.RetaGr;

/**
 * Representacao grafica de um triangulo.
 *
 * @author Kaua Bezerra Brito
 * @version 20260825
 */
public class TrianguloGr extends Triangulo implements PrimitivoGrafico {
    private Color corTriangulo = Color.BLACK;
    private String nomeTriangulo = "";
    private Color corNomeTriangulo = Color.BLACK;
    private int espTriangulo = 1;

    public TrianguloGr(int x1, int y1, int x2, int y2, int x3, int y3, Color cor, String nome, int esp) {
        super(x1, y1, x2, y2, x3, y3);
        setCorTriangulo(cor);
        setNomeTriangulo(nome);
        setEspTriangulo(esp);
    }

    public Color getCorTriangulo() {
        return this.corTriangulo;
    }

    public void setCorTriangulo(Color corTriangulo) {
        this.corTriangulo = corTriangulo;
    }

    public String getNomeTriangulo() {
        return this.nomeTriangulo;
    }

    public void setNomeTriangulo(String nomeTriangulo) {
        this.nomeTriangulo = nomeTriangulo;
    }

    public Color getCorNomeTriangulo() {
        return this.corNomeTriangulo;
    }

    public void setCorNomeTriangulo(Color corNomeTriangulo) {
        this.corNomeTriangulo = corNomeTriangulo;
    }

    public int getEspTriangulo() {
        return this.espTriangulo;
    }

    public void setEspTriangulo(int espTriangulo) {
        this.espTriangulo = espTriangulo;
    }

    /**
     * Desenha o triangulo usando tres retas.
     *
     * @param g contexto grafico
     */
    public void desenharTriangulo(Graphics g) {
        int x1 = (int)getP1().getX();
        int y1 = (int)getP1().getY();
        int x2 = (int)getP2().getX();
        int y2 = (int)getP2().getY();
        int x3 = (int)getP3().getX();
        int y3 = (int)getP3().getY();

        new RetaGr(x1, y1, x2, y2, getCorTriangulo(), "", getEspTriangulo()).desenhar(g);
        new RetaGr(x2, y2, x3, y3, getCorTriangulo(), "", getEspTriangulo()).desenhar(g);
        new RetaGr(x3, y3, x1, y1, getCorTriangulo(), "", getEspTriangulo()).desenhar(g);

        g.setColor(getCorNomeTriangulo());
        g.drawString(getNomeTriangulo(), x1 + getEspTriangulo(), y1 - getEspTriangulo());
    }

    public void desenhar(Graphics g) {
        desenharTriangulo(g);
    }

    public String getTipo() {
        return "TRIANGULO";
    }
}
