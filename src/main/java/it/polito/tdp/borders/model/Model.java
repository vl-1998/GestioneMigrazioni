package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> graph ;
	private Map<Integer,Country> countriesMap ;
	//definiamo un attributo del modello che si riferisca ad un istanza del simulatore
	private Simulator sim;
	
	public Model() {
		this.countriesMap = new HashMap<>() ;
		this.sim= new Simulator();

	}
	
	public void creaGrafo(int anno) {
		
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;

		BordersDAO dao = new BordersDAO() ;
		
		//vertici
		dao.getCountriesFromYear(anno,this.countriesMap) ;
		Graphs.addAllVertices(graph, this.countriesMap.values()) ;
		
		// archi
		List<Adiacenza> archi = dao.getCoppieAdiacenti(anno) ;
		for( Adiacenza c: archi) {
			graph.addEdge(this.countriesMap.get(c.getState1no()), 
					this.countriesMap.get(c.getState2no())) ;
			
		}
	}
	public List <Country> getCountries() {
		List <Country> res = new ArrayList <>();
		res.addAll(this.graph.vertexSet());
		Collections.sort(res);
		return res;
	}
	
	//metodo che ci permette di stampare l'elenco degli stati nella maniera richiesta, si appoggia sulla classe 
	//temporanea country e number, che conta il numero di vicini di quella country
	public List<CountryAndNumber> getCountryAndNumber() {
		List<CountryAndNumber> list = new ArrayList<>() ;
		
		for(Country c: graph.vertexSet()) {
			list.add(new CountryAndNumber(c, graph.degreeOf(c))) ;
		}
		Collections.sort(list);
		return list ;
	}
	
	//creiamo un metodo 
	public void simula(Country partenza) {
		//simuliamo solo se il grafo è stato creato
		if (this.graph!=null) {
			sim.init(partenza, this.graph); //inizializzo il simulatore
			sim.run();
		}
	
	}
	
	public Integer getT() {
		//solo se c'è stata effettivamente una smulazione
		return this.sim.getT();
	}
	
	//metodo per recuperare la mappa di stanziali, l'elenco deve essere stampato in ordine decrescente di numero di persone
	//in questo caso l'attributo number conterra il numero di stanziali invece del numero di stati
	public List <CountryAndNumber>  getStanziali(){
		Map <Country, Integer> stanziali = this.sim.getStanziali();
		List <CountryAndNumber> res = new ArrayList<>();
		//per ogni coppia chiave valore creiamo una nuova istanza di country and number che aggiungiamo nella lista res
		for (Country c: stanziali.keySet()) {
			CountryAndNumber cn = new CountryAndNumber(c, stanziali.get(c));
			res.add(cn);
		}
		Collections.sort(res); // La ordiniamo perchè country and number incrementa comparable gia nel modo che ci serve
		return res;
	}

	
	

}
