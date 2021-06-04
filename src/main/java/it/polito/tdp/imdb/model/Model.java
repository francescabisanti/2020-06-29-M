package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;


import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	SimpleWeightedGraph <Director, DefaultWeightedEdge> grafo;
	ImdbDAO dao;
	Map <Integer, Director> idMap;
	List <Director>migliore;
	int maxVicini;
	public Model() {
		dao= new ImdbDAO();
	}
	
	public void creaGrafo(int anno) {
		grafo= new SimpleWeightedGraph <Director, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		idMap= new HashMap <Integer, Director>();
		dao.getVertici(idMap, anno);
		
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		for(Adiacenza a : dao.getAdiacenza(idMap, anno)) {
			DefaultWeightedEdge e= this.grafo.getEdge(a.getD1(), a.getD2());
			if(e==null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(),a.getPeso());
			}
		}
	}
	
	public List <String> vicini (Director selezionato, Map <Integer, Director> idMap, int anno){
		List <String> result= new ArrayList <>();
		for(Adiacenza a: dao.getAdiacenza(idMap, anno)) {
			if(a.getD1().equals(selezionato) ) {
				result.add(a.getD2().toString()+ " #attori condivisi: "+a.getPeso());
			}else if(a.getD2().equals(selezionato))
				result.add(a.getD1().toString()+ " #attori condivisi: "+a.getPeso());
		}
		return result;
	}
	
	public List <Director> trovaPercorso (Director partenza, int maxcondivisi,int anno){
		migliore= new ArrayList<>();
		List <Director> parziale= new ArrayList <>();
		parziale.add(partenza);
		cerca(partenza,parziale, maxcondivisi,anno);
		maxVicini=0;
		return migliore;
		
	}
	
	
	
	private void cerca(Director partenza, List<Director> parziale, int maxCondivisi, int anno) {
		int condivisi=calcolaCondivisi(parziale, anno, partenza);
		//caso Terminale
		if(condivisi>maxCondivisi) {
			return;
			}
		if(condivisi>this.maxVicini) {
			maxVicini=condivisi;
			this.migliore= new ArrayList <>(parziale);
		}
			
			
		for(Director vicino: Graphs.neighborListOf(this.grafo,partenza)) {
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				cerca(partenza,parziale, maxCondivisi, anno);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	
	public int calcolaCondivisi(List<Director> parziale, int anno, Director selezionato) {
		int condivisi=0;
		
		for(Adiacenza a: dao.getAdiacenza(idMap, anno)) {
			if(a.getD1().equals(selezionato))
				condivisi=(int) (condivisi+a.getPeso());
		}
		return condivisi;
	}

	public int getVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getArchi() {
		return grafo.edgeSet().size();
	}

	public SimpleWeightedGraph<Director, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(SimpleWeightedGraph<Director, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public ImdbDAO getDao() {
		return dao;
	}

	public void setDao(ImdbDAO dao) {
		this.dao = dao;
	}

	public Map<Integer, Director> getIdMap() {
		return idMap;
	}

	public void setIdMap(Map<Integer, Director> idMap) {
		this.idMap = idMap;
	}
	
}
