package se.su.inlupp;

public class TestProgram {
    public static void main(String[] args){
        Graph<City> cities = new ListGraph<>();

        PathFinder<City> cityPathFinder = new DFSPathFinder<>();

        City malmo = new City("Malmö");
        City gbg = new City("Göteborg");
        City sthlm = new City("Stockholm");
        City oslo = new City("Oslo");

        cities.add(malmo);
        cities.add(gbg);

        cities.connect(malmo, gbg, "Silk road", 69, 69);
        cities.connect(gbg, sthlm, "Enått", 2, 3);

        //GraphPath<City> path = new GraphPath<>(cityPathFinder.findPath(cities, london, newYork));

        System.out.println(cities);
    }


}
