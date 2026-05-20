package se.su.inlupp;

import java.util.ArrayList;
import java.util.List;

public class BackendControl {

    //Memory data
    private ListGraph<City> citiesGraph = new ListGraph<>();
    private PathLibrary<TrainRail<City>> pathLibrary = new PathLibrary();

    //Pathfinders
    private final PathFinder<City> pathFinderBFS = new BFSPathFinder<>();
    private final PathFinder<City> pathFinderDFS = new DFSPathFinder<>();
    private final PathFinder<City> pathFinderDijkstra = new DijkstraPathFinder<>();

    private String addCity(String cityName){
        if(citiesGraph.getCityNames().contains(cityName)){
            return String.format("%s has not been added beacause %s already exists in the graph.\n", cityName, cityName);
        }
        citiesGraph.addCityName(cityName);
        citiesGraph.add(new City(cityName, false));
        return String.format("%s has been added to the graph.\n", cityName);
    }

    private String removeCity(City city){
        if(!citiesGraph.getCityNames().contains(city.getName())){
            return "The graph does not contain the given city.";
        }
        citiesGraph.removeCityName(city.getName());
        citiesGraph.remove(city);
        return String.format("%s has been removed from the graph.\n", city.getName());
    }

    public List<String> getPaths(){
        return new ArrayList<>(pathLibrary.getAllPaths().keySet());
    }

    private String addConnection(City city1, City city2, int weight){
        return null;
    }
}
