
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.FileWriter;  // Import the File class

import org.xml.sax.helpers.DefaultHandler;

public class SAXParserExample extends DefaultHandler { // Parsing actors xml file

    List<Actor> myActors;

    private String tempVal;

    //to maintain context
    private Actor tempAct;

    public SAXParserExample() {
        myActors = new ArrayList<Actor>();
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
            sp.parse("actors63.xml", this);

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

        Iterator<Actor> it = myActors.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
        System.out.println("COMMIT;");
    }

    private void writeData() { //change this to WRITE TO FILE METHOD
        try {
            FileWriter myWriter = new FileWriter("Actors.sql", false);
            myWriter.write("USE parseTest;\n");
            myWriter.write("BEGIN; -- start transaction\n");
            //System.out.println("No of actors '" + myActors.size() + "'.");

            Iterator<Actor> it = myActors.iterator();
            while (it.hasNext()) {
                myWriter.write(it.next().toString() + "\n");
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
        if (qName.equalsIgnoreCase("actor")) {
            //create a new instance of employee
            tempAct = new Actor();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("actor")) {
            //add it to the list
            myActors.add(tempAct);

        } else if (qName.equals("stagename")) {
            tempAct.setName(tempVal);
        } else if (qName.equalsIgnoreCase("dob")) {
            try {
                tempAct.setYear(Integer.parseInt(tempVal));
            }
            catch (Exception e){
                tempAct.setYear(0);
            }
        }

    }

    public static void main(String[] args) {
        SAXParserExample spe = new SAXParserExample();
        spe.runExample();
    }

}
