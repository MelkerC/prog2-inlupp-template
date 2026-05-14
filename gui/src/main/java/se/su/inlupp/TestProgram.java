package se.su.inlupp;

public class TestProgram {
    public static void main(String[] args){
        ListGraph<City> cities = new ListGraph<>();
        PathLibrary<City> pathLibrary = new PathLibrary();
        PathFinder<City> pathFinder = new BFSPathFinder<>();

        City malmo = new City("Malmö");
        City gbg = new City("Göteborg");
        City sthlm = new City("Stockholm");
        City oslo = new City("Oslo");


        cities.connect(oslo, gbg, "Silk road", 69);
        cities.connect(gbg, sthlm, "Väg", 12);
        cities.connect(gbg, malmo, "Road", 69);
        cities.connect(malmo, sthlm, "Oslo", 69);

        if(cities.hasPath(oslo, sthlm)){
            GraphPath<City> path = (GraphPath<City>) pathFinder.findPath(cities, oslo, sthlm);
            path.setPathName("Snabbväg");
            pathLibrary.addPath(path.getPathName(), path);
        }




        System.out.println(pathLibrary.getAllPaths());

        cities.disconnect(gbg, sthlm);

        pathLibrary.updateAllPaths(cities);

        System.out.println(pathLibrary.getAllPaths());

        cities.connect(oslo, sthlm, "Silk road", 69);

        pathLibrary.updateAllPaths(cities);

        System.out.println(pathLibrary.getAllPaths());


        cities.disconnect(gbg, sthlm);

        cities.disconnect(malmo, sthlm);


        cities.disconnect(oslo, sthlm);
        pathLibrary.updateAllPaths(cities);

        System.out.println(pathLibrary.getAllPaths());

        cities.connect(oslo, sthlm, "Silk road", 69);
        if(cities.hasPath(oslo, sthlm)){
            GraphPath<City> path = (GraphPath<City>) pathFinder.findPath(cities, oslo, sthlm);
            path.setPathName("Snabbväg");
            pathLibrary.addPath(path.getPathName(), path);
        }

        cities.remove(oslo);

        pathLibrary.updateAllPaths(cities);
        System.out.println(pathLibrary.getAllPaths());
    }


}
