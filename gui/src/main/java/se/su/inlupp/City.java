package se.su.inlupp;

public class City {
    private final String name;

    private boolean visited;

    public City(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean getVisited(){return visited;}

    public void visit(){visited = true;}

    @Override
    public String toString(){
        if(visited){
            return String.format("%s (Visited)", name);
        }else{
            return String.format("%s (Not Visited)", name);
        }
    }


}
