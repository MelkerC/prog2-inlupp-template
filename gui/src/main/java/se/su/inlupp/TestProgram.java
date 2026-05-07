package se.su.inlupp;

public class TestProgram {
    public static void main(String[] args){
        Graph<City> cities = new ListGraph<>();

        PathFinder<City> cityPathFinder = new DFSPathFinder<>();

        City london = new City("London");
        City newYork = new City("New York");

        cities.add(london);
        cities.add(newYork);

        cities.connect(london, newYork, "Silk road", 69);

        //GraphPath<City> path = new GraphPath<>(cityPathFinder.findPath(cities, london, newYork));

        //System.out.println(path);


    }


}
