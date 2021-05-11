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

public class SAXCastParser extends DefaultHandler {

    List<Movie> myMovies;

    private String tempVal;

    //to maintain context
    private Movie tempMovie;

    public SAXCastParser() {
        myMovies = new ArrayList<Movie>();
    }

    public void runExample() {
        parseDocument();
        //printData();
        writeData();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("casts124.xml", this);

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
    private void printData() { //change this to WRITE TO FILE METHOD
        System.out.println("BEGIN; -- start transaction");
        //System.out.println("No of actors '" + myActors.size() + "'.");

        Iterator<Movie> it = myMovies.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toCastString());
        }
        System.out.println("COMMIT;");
    }

    private void writeData() { //change this to WRITE TO FILE METHOD
        try {
            FileWriter myWriter = new FileWriter("Cast.sql", false);
            myWriter.write("USE parseTest;\n");
            myWriter.write("BEGIN; -- start transaction\n");
            System.out.println("No of cast lines '" + myMovies.size() + "'.");

            Iterator<Movie> it = myMovies.iterator();
            while (it.hasNext()) {
                myWriter.write(it.next().toCastString() + "\n");
            }
            myWriter.write("COMMIT;\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("filmc")) {
            //create a new instance of employee
            tempMovie = new Movie();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("filmc")) {
            //add it to the list
            myMovies.add(tempMovie);

        } else if (qName.equals("f")) {
            tempMovie.setId(tempVal);
        } else if (qName.equalsIgnoreCase("a")) {
            tempMovie.addActor(tempVal);
        }

    }

    public static void main(String[] args) {
        SAXCastParser spe = new SAXCastParser();
        spe.runExample();
    }

}
