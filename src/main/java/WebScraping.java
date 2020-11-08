
import java.io.IOException;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import javax.swing.*;

public class WebScraping extends JFrame {

    public static final String URL = "https://en.wikipedia.org/wiki/List_of_Ray_Donovan_episodes";

    public WebScraping() {

        // Compruebo si me da 200 al hacer la petición
        if (getStatusConnectionCode(URL) == 200) {

            // Obtengo el HTML de la web en un objeto Document
            Document document = getHtmlDocument(URL);

            // Busco todos los elementos que están dentro de: 
            Elements entradas = document.select("tr.vevent");
            //System.out.println("Número de entradas: " + entradas.size() + "\n");//82

            String[] encabezado = new String[]{
                "Nº EPISODIO TOTAL", "Nº TEMPORADA", "Nº EPISODIO TEMPORADA", "TÍTULO DEL EPISODIO", "FECHA DEL EPISODIO"
            };
            String[][] tabla_datos = new String[entradas.size()][encabezado.length];//[82][5]

            int fila = 0;
            int numero_temporada = 0;

            // Parseo cada una de las entradas
            for (Element elem : entradas) {

                // Con el método "text()" obtengo el contenido que hay dentro de las etiquetas HTML
                // Con el método "toString()" obtengo todo el HTML con etiquetas incluidas
                String numero_episodio_total = elem.getElementsByTag("th").text();
                String numero_episodio_temporada = elem.getElementsByTag("th").next().text();
                String titulo_episodio = elem.getElementsByClass("summary").text();
                String fecha_episodio = elem.getElementsByClass("summary").next().next().next().text();
                //System.out.println(numero_episodio_total + "\n" + numero_episodio_temporada + "\n" + titulo_episodio + "\n" + fecha_episodio + "\n\n");

                if (Integer.parseInt(numero_episodio_temporada) == 1) {
                    numero_temporada++;
                }

                tabla_datos[fila][0] = numero_episodio_total;
                tabla_datos[fila][1] = String.valueOf(numero_temporada);
                tabla_datos[fila][2] = numero_episodio_temporada;
                tabla_datos[fila][3] = titulo_episodio;
                tabla_datos[fila][4] = fecha_episodio;
                fila++;

            }

            JTable table = new JTable(tabla_datos, encabezado);
            this.add(new JScrollPane(table));
            this.setTitle("LISTA DE LOS EPISODIOS DE LA SERIE RAY DONOVAN - DESARROLLADOR: LUIS VALLES PASTOR");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.pack();
            this.setVisible(true);

        } else {
            System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(URL));
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WebScraping();
            }
        });
    }

    public static int getStatusConnectionCode(String url) {
        /**
         * Con esta método compruebo el Status code de la respuesta que recibo
         * al hacer la petición EJM: 200 OK	300 Multiple Choices 301 Moved
         * Permanently 305 Use Proxy 400 Bad Request	403 Forbidden 404 Not Found
         * 500 Internal Server Error 502 Bad Gateway	503 Service Unavailable
         *
         * @param url
         * @return Status Code
         */

        Response response = null;

        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
        }
        return response.statusCode();
    }

    public static Document getHtmlDocument(String url) {
        /**
         * Con este método devuelvo un objeto de la clase Document con el
         * contenido del HTML de la web que me permitirá parsearlo con los
         * métodos de la librería JSoup
         *
         * @param url
         * @return Documento con el HTML
         */

        Document doc = null;

        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
        }

        return doc;

    }

}
