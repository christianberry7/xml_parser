

public class Actor {

    private String name;

    private int year;


    public Actor(){

    }

    public Actor(String name, int year) {
        this.name = name;
        this.year = year;
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String toString() { // CHANGE THIS TO INSERT ... RUNNABLE MYSQL QUERY THAT WILL BE PART OF A .SQL FILE TO INSERT XML DATA
        StringBuffer sb = new StringBuffer();
        sb.append("CALL add_star(\"");
        String name = getName();
        if (name.contains("\"")){
            name = name.replace("\"", "\'");
        }
        sb.append(name);
        sb.append("\", ");
        if (getYear() == 0){
            sb.append("NULL");
        }
        else{
            sb.append(getYear());
        }
        sb.append(");");

        return sb.toString();
    }
}
