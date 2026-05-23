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

    public void visit(){visited = true;}

    @Override
    public String toString(){
        return name;
    }
}
