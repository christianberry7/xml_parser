public class Genre {

    private int id;

    private String name;

    public Genre(){

    }

    public Genre(int id, String name) {
        this.id  = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String year) {
        this.name = name;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Genre Details - ");
        sb.append("Id:" + getId());
        sb.append(", ");
        sb.append("Year:" + getName());
        sb.append(", ");

        return sb.toString();
    }
}

