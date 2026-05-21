package se.su.inlupp;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class BackendControl {

    //Memory data
    private ListGraph<City> citiesGraph = new ListGraph<>();
    private PathLibrary<TrainRail<City>> pathLibrary = new PathLibrary();
    private FileReader fileReader;
    private FileWriter fileWriter;

    //Pathfinders
    private final PathFinder<City> pathFinderBFS = new BFSPathFinder<>();
    private final PathFinder<City> pathFinderDFS = new DFSPathFinder<>();
    private final PathFinder<City> pathFinderDijkstra = new DijkstraPathFinder<>();

    public boolean addCity(String cityName){
        if(citiesGraph.getCityNames().contains(cityName)){
            return false;
        }
        citiesGraph.addCityName(cityName);
        citiesGraph.add(new City(cityName, false));
        return true;
    }

    public boolean removeCity(String cityName){
        if(citiesGraph.getCityNames().contains(cityName)){
            return false;
        }

        citiesGraph.removeCityName(cityName);

        for(City c : citiesGraph.getNodes()){
            if(c.getName().equals(cityName)){citiesGraph.remove(c);}
        }
        return true;
    }

    public List<String> getPaths(){
        return new ArrayList<>(pathLibrary.getAllPaths().keySet());
    }

    private String addConnection(City city1, City city2, int weight){
        return null;
    }
}
