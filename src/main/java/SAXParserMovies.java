import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SAXParserMovies extends DefaultHandler {

    List<Movie> movies;
    List<Genre> genres;

    private String tempVal;

    //to maintain context
    private Movie tempMovie;
    private Genre tempGenre;

    public SAXParserMovies() {
        movies = new ArrayList<Movie>();
    }

    public void runExample() {
        parseDocument();
        writeData();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() { //change this to write to file

        System.out.println("No of Movies '" + movies.size() + "'.");

        Iterator<Movie> it = movies.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString()); //change to insert statement to insert into database
        }
    }

    private void writeData() { //change this to WRITE TO FILE METHOD
        System.out.println(movies.size());
        try {
            FileWriter myWriter = new FileWriter("Movies.sql", false);
            myWriter.write("USE parseTest;\n");
            myWriter.write("BEGIN; -- start transaction\n");
            //System.out.println("No of actors '" + myActors.size() + "'.");

            Iterator<Movie> it = movies.iterator();
            while (it.hasNext()) {
                myWriter.write(it.next().toString() + "\n");
            }
            myWriter.write("COMMIT;\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            System.out.println(Movie.errorMovies);
            System.out.println(Movie.errorMovies.size());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("film")) {
            tempMovie = new Movie();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("film")) {
            //add it to the list
            movies.add(tempMovie);
        } else if (qName.equalsIgnoreCase("fid")) {
            tempMovie.setId(tempVal);
        } else if (qName.equalsIgnoreCase("t")) {
            tempMovie.setTitle(tempVal);
        } else if (qName.equalsIgnoreCase("year")) {
            try {
                tempMovie.setYear(Integer.parseInt(tempVal));
            }
            catch (Exception e){
                tempMovie.setYear(0);
            }
        }else if (qName.equalsIgnoreCase("dirn")) {
            tempMovie.setDirector(tempVal);
        }else if (qName.equalsIgnoreCase("cat")) {
            tempMovie.addGenre(tempVal);
        }

    }

    public static void main(String[] args) {
        SAXParserMovies spe = new SAXParserMovies();
        spe.runExample();
    }

}
