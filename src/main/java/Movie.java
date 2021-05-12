import java.util.ArrayList;
import java.util.HashSet;

public class Movie {
    static public HashSet<String> includedGenres = new HashSet<>();
    static public HashSet<String> errorMovies = new HashSet<>();
    static public HashSet<String> usedKeys = new HashSet<>();
    private String title;

    private String id;

    private int year;

    private String director;

    private HashSet<String> starIds;
    private HashSet<String> genreIds;

    public Movie(){
        this.starIds = new HashSet<>();
        this.genreIds = new HashSet<>();
    }

    public Movie(String title, String id, int year, String director) {
        this.title = title;
        this.id  = id;
        this.year = year;
        this.director = director;
        this.starIds = new HashSet<>();
        this.genreIds = new HashSet<>();

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public HashSet<String> getStarIds() {return this.starIds; }

    public HashSet<String> getGenreIds() {return this.genreIds; }



    public void addGenre(String genre){
        if (!genre.equals("")){
            genreIds.add(fixGenre(genre));
        }
    }

    public void addActor(String star){
        if (!star.equals("")){
            starIds.add(star);
        }
    }

    public String fixGenre(String genre){
        String temp = genre.toLowerCase().strip();
        return temp.substring(0,1).toUpperCase() + temp.substring(1);
    }

    public String toString() {
        // INSERT INTO MOVIES
        StringBuffer sb = new StringBuffer();
        if (getYear() == 0){
            errorMovies.add("Movie: Id=" + getId() + ", Title=" + getTitle() +" could not be added because year is not an integer\n");
            return "";
        }
        if (usedKeys.contains(getId())){
            errorMovies.add("Movie: Id=" + getId() + ", Title=" + getTitle() +" could not be added because key is already taken\n");
            return "";
        }
        usedKeys.add(getId());
        // if any are null include in our problems file or whatever
        sb.append("INSERT INTO movies VALUES (\"" + getId() + "\", \"" + getTitle() + "\", " + getYear() + ", \"" + getDirector() + "\");\n");
        for (String g: genreIds){

            if (!includedGenres.contains(g)) {
                // do the insert and create a new genre in the genres table
                sb.append("CALL add_genre(\"" + g + "\");\n");
                includedGenres.add(g); //making sure we don't add the same genre over and over again (waste of time)
            }
            // otherwise the genre was already made

            //then do genres_in_movies

            sb.append("INSERT INTO genres_in_movies VALUES ((SELECT id from genres where name = \"" + g + "\"), \"" + getId() + "\");\n");
        }
        return sb.toString();
    }

    public String toCastString() {
        // INSERT INTO MOVIES
        StringBuffer sb = new StringBuffer();
        //if the star id is not in the db, don't add
        // if the movie id is not in the db, don't add

        // if any are null include in our problems file or whatever
        for (String s: starIds) {
            sb.append("CALL add_cast(\"" + getId() + "\", \"");
            if (s.contains("\"")){
                s = s.replace("\"", "\'");
            }
            if(s.contains("`")){
                s = s.replace("`", "\'");
            }
            if(s.contains("\\")){
                s = s.replace("\\", "");
            }
            sb.append(s);
            sb.append("\");\n");
        }
        return sb.toString();
    }
}
