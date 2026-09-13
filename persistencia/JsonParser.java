package persistencia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Converte um texto JSON em uma arvore de objetos Java "genericos":
 * - objeto JSON vira um Map&lt;String, Object&gt;
 * - array JSON vira um List&lt;Object&gt;
 * - string JSON vira um String
 * - numero JSON vira um Double
 * - true / false vira um Boolean
 * - null vira null
 *
 * @author Heitor Cavalcanti
 * @version 20260825
 */
public class JsonParser {
    private final String texto;
    private int pos;

    /**
     * Constroi o parser a partir do texto JSON.
     *
     * @param texto conteudo JSON completo
     */
    private JsonParser(String texto) {
        this.texto = texto;
        this.pos = 0;
    }

    /**
     * Interpreta um texto JSON e devolve a arvore de objetos equivalente.
     *
     * @param texto conteudo JSON completo
     * @return Map, List, String, Double, Boolean ou null, dependendo do JSON
     */
    public static Object parse(String texto) {
        JsonParser parser = new JsonParser(texto);
        return parser.parseValor();
    }

    /**
     * Interpreta o proximo valor JSON a partir da posicao atual.
     *
     * @return valor interpretado
     */
    private Object parseValor() {
        pularEspacos();
        char c = texto.charAt(pos);

        if (c == '{') {
            return parseObjeto();
        } else if (c == '[') {
            return parseArray();
        } else if (c == '"') {
            return parseString();
        } else if (c == 't' || c == 'f') {
            return parseBooleano();
        } else if (c == 'n') {
            pos += 4; // "null"
            return null;
        } else {
            return parseNumero();
        }
    }

    /**
     * Interpreta um objeto JSON ({ "chave": valor, ... }).
     *
     * @return objeto interpretado como Map
     */
    private Map<String, Object> parseObjeto() {
        Map<String, Object> objeto = new LinkedHashMap<String, Object>();
        pos++; // consome '{'
        pularEspacos();

        if (texto.charAt(pos) == '}') {
            pos++;
            return objeto;
        }

        boolean continua = true;
        while (continua) {
            pularEspacos();
            String chave = parseString();
            pularEspacos();
            pos++; // consome ':'
            Object valor = parseValor();
            objeto.put(chave, valor);
            pularEspacos();

            char proximo = texto.charAt(pos);
            pos++; // consome ',' ou '}'
            continua = (proximo == ',');
        }

        return objeto;
    }

    /**
     * Interpreta um array JSON ([ valor, valor, ... ]).
     *
     * @return array interpretado como List
     */
    private List<Object> parseArray() {
        List<Object> lista = new ArrayList<Object>();
        pos++; // consome '['
        pularEspacos();

        if (texto.charAt(pos) == ']') {
            pos++;
            return lista;
        }

        boolean continua = true;
        while (continua) {
            Object valor = parseValor();
            lista.add(valor);
            pularEspacos();

            char proximo = texto.charAt(pos);
            pos++; // consome ',' ou ']'
            continua = (proximo == ',');
        }

        return lista;
    }

    /**
     * Interpreta uma string JSON ("texto com \" escapes \"").
     *
     * @return string interpretada, sem as aspas
     */
    private String parseString() {
        pos++; // consome aspas iniciais
        StringBuilder sb = new StringBuilder();

        while (texto.charAt(pos) != '"') {
            char c = texto.charAt(pos);

            if (c == '\\') {
                pos++;
                char escapado = texto.charAt(pos);

                if (escapado == 'n') {
                    sb.append('\n');
                } else if (escapado == 't') {
                    sb.append('\t');
                } else if (escapado == 'r') {
                    sb.append('\r');
                } else {
                    // cobre \" , \\ , \/ e demais casos simples
                    sb.append(escapado);
                }
            } else {
                sb.append(c);
            }

            pos++;
        }

        pos++; // consome aspas finais
        return sb.toString();
    }

    /**
     * Interpreta um numero JSON (inteiro ou decimal, com sinal e expoente
     * opcionais).
     *
     * @return numero interpretado
     */
    private Double parseNumero() {
        int inicio = pos;

        while (pos < texto.length() && "-+.eE0123456789".indexOf(texto.charAt(pos)) >= 0) {
            pos++;
        }

        return Double.parseDouble(texto.substring(inicio, pos));
    }

    /**
     * Interpreta um literal booleano (true ou false).
     *
     * @return valor booleano interpretado
     */
    private Boolean parseBooleano() {
        if (texto.charAt(pos) == 't') {
            pos += 4; // "true"
            return Boolean.TRUE;
        }

        pos += 5; // "false"
        return Boolean.FALSE;
    }

    /**
     * Avanca a posicao de leitura enquanto houver espacos em branco.
     */
    private void pularEspacos() {
        while (pos < texto.length() && Character.isWhitespace(texto.charAt(pos))) {
            pos++;
        }
    }
}
