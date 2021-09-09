package it.polito.tdp.yelp.model;

import java.time.Year;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private Graph <Business, DefaultWeightedEdge> grafo ;
	private YelpDao dao ;
	private List<Business> vertici; 

	
	public List<String> getAllCities(){
		dao = new YelpDao();
		return dao.getAllCities();
	}
	
	public String creaGrafo(String city, Year year) {
		
		//INIZIALIZZO GRAFO
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//AGGIUNGI VERTICI
		this.vertici=dao.getNodi(city, year);
		Graphs.addAllVertices(grafo, vertici);
		
		return String.format("Grafo creato con %d vertici e %d archi", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
}
