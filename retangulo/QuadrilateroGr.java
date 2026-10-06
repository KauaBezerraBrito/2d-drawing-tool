package retangulo;

import java.awt.Color;
import java.awt.Graphics;

import ponto.PontoGr;
import reta.RetaGr;

/**
 * Representacao grafica de um retangulo que pode estar rotacionado.
 * 
 * @author Heitor de Sousa Cavalcanti, Kaua Bezerra Brito, Marcelo Liao, Rodrigo
 *         Ward Leite
 * @version 06.10.2026
 */
public class QuadrilateroGr extends RetanguloGr {
        private PontoGr p3;
        private PontoGr p4;

        /**
         * Construtor da classe QuadrilateroGr.
         * 
         * @param p1   ponto 1 do quadrilatero
         * @param p2   ponto 2 do quadrilatero
         * @param p3   ponto 3 do quadrilatero
         * @param p4   ponto 4 do quadrilatero
         * @param cor  cor do quadrilatero
         * @param nome nome do quadrilatero
         * @param esp  espessura do quadrilatero
         */
        public QuadrilateroGr(PontoGr p1, PontoGr p2, PontoGr p3, PontoGr p4,
                        Color cor, String nome, int esp) {
                super((int) p1.getX(), (int) p1.getY(), (int) p3.getX(), (int) p3.getY(), cor, nome, esp);
                this.setP1(p1);
                this.setP2(p2);
                this.p3 = p3;
                this.p4 = p4;
        }

        /**
         * Retorna o ponto 3 do quadrilatero.
         * 
         * @return ponto 3 do quadrilatero
         */
        public PontoGr getP3() {
                return p3;
        }

        /**
         * Retorna o ponto 4 do quadrilatero.
         * 
         * @return ponto 4 do quadrilatero
         */
        public PontoGr getP4() {
                return p4;
        }

        /**
         * Desenha o quadrilatero no contexto grafico.
         * 
         * @param g contexto grafico
         */
        @Override
        public void desenhar(Graphics g) {
                new RetaGr((int) getP1().getX(), (int) getP1().getY(),
                                (int) getP2().getX(), (int) getP2().getY(),
                                getCorRetangulo(), "", getEspRetangulo()).desenhar(g);
                new RetaGr((int) getP2().getX(), (int) getP2().getY(),
                                (int) getP3().getX(), (int) getP3().getY(),
                                getCorRetangulo(), "", getEspRetangulo()).desenhar(g);
                new RetaGr((int) getP3().getX(), (int) getP3().getY(),
                                (int) getP4().getX(), (int) getP4().getY(),
                                getCorRetangulo(), "", getEspRetangulo()).desenhar(g);
                new RetaGr((int) getP4().getX(), (int) getP4().getY(),
                                (int) getP1().getX(), (int) getP1().getY(),
                                getCorRetangulo(), "", getEspRetangulo()).desenhar(g);

                g.setColor(getCorNomeRetangulo());
                g.drawString(getNomeRetangulo(), (int) getP1().getX() + getEspRetangulo(),
                                (int) getP1().getY() - getEspRetangulo());
        }
}
