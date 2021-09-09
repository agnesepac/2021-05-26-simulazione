package it.polito.tdp.yelp.model;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private Graph <Business, DefaultWeightedEdge> grafo ;
	private YelpDao dao ;
	private List<Business> vertici; 
	private Map<String,Business> verticiIdMap;

	
	public List<String> getAllCities(){
		dao = new YelpDao();
		return dao.getAllCities();
	}
	
	public String creaGrafo(String city, Year year) {
		
		//RIEMPIO IdMap 
		this.verticiIdMap = new HashMap<>();
		
		for(Business b: this.dao.getNodi(city, year)) {
			verticiIdMap.put(b.getBusinessId(), b);
		}
		
		//INIZIALIZZO GRAFO
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//AGGIUNGI VERTICI
		this.vertici=dao.getNodi(city, year);
		Graphs.addAllVertices(grafo, vertici);
	
		//AGGIUNGO ARCHI (faccio calcolare archi dal DB)
		List<Adiacenza> adiacenze = dao.calcolaAdiacenza(city, year);
		
		for(Adiacenza a: adiacenze) {
			Graphs.addEdge(grafo, verticiIdMap.get(
					a.getBusinessId1()), 
					verticiIdMap.get(a.getBusinessId2()),
					a.getPeso());
		}
		
		return String.format("Grafo creato con %d vertici e %d archi", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	public Business getLocaleMigliore() {
	
		double max = 0.0;
		Business result = null;
		
		for(Business b: grafo.vertexSet()) {
		
			double val = 0.0;
			for(DefaultWeightedEdge e: this.grafo.incomingEdgesOf(b)) { // SOMMO PESO archi entranti		
				val+=this.grafo.getEdgeWeight(e);
			}
			
			for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(b)) { // SOTTRAGGO PESO archi uscenti		
				val-=this.grafo.getEdgeWeight(e);
			}
			
			if(val>max) {
				max = val; //salvo nuovo max
				result = b; //salvo locale migliore
			}
		}
		
		return result;
	}
	
}
