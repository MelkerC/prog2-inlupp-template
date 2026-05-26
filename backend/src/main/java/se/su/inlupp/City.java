package se.su.inlupp;

public class City {
    private final String name;

    private boolean visited;

    public City(String name,  boolean visited) {
        this.name = name;
        this.visited = visited;
    }

    public String getName() {
        return name;
    }

    public boolean getVisited(){return visited;}

    public void visit(){visited = !visited;}

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City other)) return false;

        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
