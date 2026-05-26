package se.su.inlupp;

import java.io.*;
import java.util.*;

public class BackendControl {

    //Memory data
    private final ListGraph<City> citiesGraph = new ListGraph<>();
    private final PathLibrary<City> pathLibrary = new PathLibrary<>();
    private Map<String, String> uniqueEdges = new HashMap<>();

    //Pathfinders
    private final PathFinder<City> pathFinderBFS = new BFSPathFinder<>();
    private final PathFinder<City> pathFinderDFS = new DFSPathFinder<>();
    private final PathFinder<City> pathFinderDijkstra = new DijkstraPathFinder<>();
    private final ArrayList<PathFinder<City>> pathFinders = new ArrayList<>(List.of(pathFinderDijkstra, pathFinderBFS, pathFinderDFS));


    public void saveProgram(File saveFile, Map<String, String> guiCityPlacement, Map<String, String> guiEdges, String currantBackgroundName) {
        String saveInfo = citiesGraph.getNodes().size() + "\n";

        for(City city : citiesGraph.getNodes()){
            saveInfo += city.getName() + "\n";
            saveInfo += city.getVisited() + "\n";
        }

        saveInfo += citiesGraph.getAllEdges().size() / 2 + "\n";

        Set<String> seen = new HashSet<>();

        for(City city : citiesGraph.getNodes()){
            for(Edge<City> edge : citiesGraph.getEdgesFrom(city)){

                String key = edge.getName();

                if(seen.contains(key)){
                    continue;
                }
                seen.add(key);
                saveInfo += city.getName() + "\n";
                saveInfo += edge.getDestination().getName() + "\n";
                saveInfo += edge.getName() + "\n";
                saveInfo += edge.getWeight() + "\n";
            }
        }

        saveInfo += guiCityPlacement.size() + "\n";
        int undeclared = 0;
        for(String guiCity : guiCityPlacement.keySet()){
            if(guiCity == null){
                saveInfo += "undeclared" + undeclared++ + "\n";
            }else{
                saveInfo += guiCity + "\n";
            }

            saveInfo += guiCityPlacement.get(guiCity) + "\n";
        }

        saveInfo += guiEdges.size() + "\n";
        for(String cityName : guiEdges.keySet()){
            saveInfo += cityName + "\n";
            saveInfo += guiEdges.get(cityName) + "\n";
        }

        saveInfo += pathLibrary.getAllPaths().size() + "\n";
        for(GraphPath<City> path : pathLibrary.getAllPaths().values()){
            saveInfo += path.getStart() + "\n";
            saveInfo += path.getEnd() + "\n";
            saveInfo += path.getAlgorithmIndex() + "\n";
        }

        saveInfo += currantBackgroundName + "\n";

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            writer.write(saveInfo);
            writer.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Map<String, String> loadProgram(File saveFile) {
        citiesGraph.restart();
        uniqueEdges.clear();
        Map<String, String> guiCityPlacement = new HashMap<>();
        Map<String, String> guiEdges = new HashMap<>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(saveFile));
            int nodeCount = Integer.parseInt(reader.readLine());
            for(int i = 0; i < nodeCount; i++){
                String tempName = reader.readLine();
                boolean visited = Boolean.parseBoolean(reader.readLine());
                citiesGraph.add(new City(tempName, visited));
            }

            int cityWithEdge = Integer.parseInt(reader.readLine());

            for(int i = 0; i < cityWithEdge; i++){

                String cityName = reader.readLine();
                String destination = reader.readLine();
                String edgeName = reader.readLine();
                int weight = Integer.parseInt(reader.readLine());

                connectCities(cityName, destination, edgeName, weight);
            }

            int placmentCount = Integer.parseInt(reader.readLine());
            for(int i = 0; i < placmentCount; i++){
                String cityName = reader.readLine();
                String placement = reader.readLine();

                guiCityPlacement.put(cityName, placement);
            }

            int guiEdgeCount = Integer.parseInt(reader.readLine());
            for(int i = 0; i < guiEdgeCount; i++){
                String from = reader.readLine();
                String to = reader.readLine();

                guiEdges.put(from, to);
            }

            uniqueEdges = new HashMap<>(guiEdges);
            System.out.println(uniqueEdges + " uniquueeeee");

            int pathCount = Integer.parseInt(reader.readLine());
            for(int i = 0; i < pathCount; i++){
                String startCity = reader.readLine();
                String endCity = reader.readLine();
                int algortithm = Integer.parseInt(reader.readLine());
                createPath(startCity, endCity, algortithm);
            }

            guiCityPlacement.put("Image", reader.readLine());
            reader.close();

        } catch (IOException e){
            e.printStackTrace();
        }

        return guiCityPlacement;
    }

    public Map<String, String> getUniqueEdges(){
        return uniqueEdges;
    }

    public boolean addCity(String cityName){
        if(citiesGraph.getCityNames().contains(cityName)){
            return false;
        }
        citiesGraph.addCityName(cityName);
        citiesGraph.add(new City(cityName, false));
        return true;
    }

    public boolean removeCity(String cityName){
        if(!citiesGraph.getCityNames().contains(cityName)){
            return false;
        }

        for(City c : citiesGraph.getNodes()){
            if(c.getName().equals(cityName)){
                citiesGraph.remove(c);
                break;
            }
        }

        citiesGraph.removeCityName(cityName);
        return true;
    }

    public boolean connectCities(String city1, String city2, String rail, int weight){
        try{
            citiesGraph.connect(getCity(city1), getCity(city2), rail, weight);
            System.out.println(citiesGraph.getNodes().size() + " size \n");
        }catch(NoSuchElementException | IllegalStateException | IllegalArgumentException e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean disConnectCities(String city1, String city2){
        try{
            citiesGraph.disconnect(getCity(city1), getCity(city2));
        }catch(NoSuchElementException | IllegalStateException e){
            return false;
        }

        return true;
    }

    public boolean createPath(String city1, String city2, int algoritihm){
        GraphPath<City> newPath = (GraphPath<City>) pathFinders.get(algoritihm).findPath(citiesGraph, getCity(city1), getCity(city2));

        if(newPath == null){
            return false;
        }

        pathLibrary.addPath(newPath.toString(), newPath);
        return true;
    }

    public boolean hasNode(String cityName){
        return citiesGraph.getNodes().contains(getCity(cityName));
    }

    public void removePath(GraphPath<City> path){
        pathLibrary.removePath(path.toString());
    }

    public List<String> updateAllPaths(){
        List<GraphPath<City>> removedPaths = new ArrayList<>();
        List<String> returnNames = new ArrayList<>();

        for(GraphPath<City> path : pathLibrary.getAllPaths().values()){
            if(!updatePath(path, path.getAlgorithmIndex())){
                removedPaths.add(path);
                returnNames.add(path.toString());

            }
        }

        for(GraphPath<City> path : removedPaths){
            pathLibrary.removePath(path.toString());
        }

        return returnNames;
    }

    public boolean updatePath(GraphPath<City> path, int algoritihm){
        if(path.getStart() == null || path.getEnd() == null){return false;}
        City from = path.getStart();
        City to = path.getEnd();
        GraphPath<City> updatedPath;

        try{
            updatedPath = (GraphPath<City>) pathFinders.get(algoritihm).findPath(citiesGraph, getCity(from.getName()), getCity(to.getName()));
        }catch(NullPointerException e){
            return false;
        }

        if(updatedPath == null){
            return false;
        }
        pathLibrary.removePath(path.toString());
        pathLibrary.addPath(updatedPath.toString(), updatedPath);
        return true;
    }

    public List<String> getEdgesFrom(String cityName){
        Collection<Edge<City>> temp  = citiesGraph.getEdgesFrom(getCity(cityName));
        List<String> edgeNames = new ArrayList<>();
        for(Edge<City> e : temp){
            edgeNames.add(e.getName());
        }
        return edgeNames;
    }

    public City getCity(String cityName){
        for(City c : citiesGraph.getNodes()){
            if(c.getName().equals(cityName)){return c;}
        }
        return null;
    }

    public List<String> getPaths(){
        return new ArrayList<>(pathLibrary.getAllPaths().keySet());
    }

    public GraphPath<City> getPathByName(String pathName){
        return pathLibrary.getPath(pathName);
    }

    public Edge<City> getEdgesBetween(String from, String to){
        return citiesGraph.getEdgeBetween(getCity(from), getCity(to));
    }

    public String getEdgeNameBetween(City city1, City city2){
        return citiesGraph.getEdgeBetween(city1, city2).getName();
    }
}
