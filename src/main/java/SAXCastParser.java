import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class SAXCastParser extends DefaultHandler {

    List<Movie> myMovies;

    private String tempVal;

    private Connection conn;

    //to maintain context
    private Movie tempMovie;
    private DataSource dataSource;

    public SAXCastParser() {
        myMovies = new ArrayList<Movie>();
        conn = null;
        String jdbcURL="java:comp/env/jdbc/moviedb2";
        try {
            conn = DriverManager.getConnection(jdbcURL,"mytestuser", "My6$Password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        try {
//            conn.
//            //dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb2");
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }
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
            //Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            FileWriter myWriter = new FileWriter("Cast.sql", false);
            myWriter.write("USE moviedb2;\n");
            myWriter.write("BEGIN; -- start transaction\n");
            System.out.println("No of cast lines '" + myMovies.size() + "'.");
            String mov_query = "SELECT COUNT(*) as cnt from movies m where m.id = ";
            String star_query = "SELECT COUNT(*) as cnt from stars s where lower(s.name) = lower(";

            // Perform the query
            ResultSet rs = null;

            boolean step2;

            Iterator<Movie> it = myMovies.iterator();
            while (it.hasNext()) {
                step2 = false;
                Movie m = it.next();
                rs = statement.executeQuery(mov_query + m.getId());
                if (rs.next()) {
                    String count = rs.getString("cnt");
                    if (!count.equals("1")){
                        System.out.println("Error! movie id:" + m.getId() + " could not be added because the movie does not exist in our movies table");
                        step2 = true;
                    }
                }
                if (step2){
                    for (String s: m.getStarIds()) {
                        rs = statement.executeQuery(star_query + s.toLowerCase() + ")");
                        if (rs.next()) {
                            String count = rs.getString("cnt");
                            if (!count.equals("1")){
                                System.out.println("Error! movie id:" + m.getId() + " could not be added because there are multiple actors with the same name");
                            }
                            else{
                                myWriter.write(Movie.write_it(m.getId(), s));
                            }
                        }
                    }

                }
            }
            rs.close();
            myWriter.write("COMMIT;\n");
            myWriter.close();
            statement.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Not an IO error");
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
