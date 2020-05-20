package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulator {
	//cosa ci serve per simulare, qual è il modello
	//MODELLO -> stato del sistema ad ogni passo
	//grafo per recuperare i vicini, dove si possono spostare i migranti
	private Graph <Country, DefaultEdge> grafo;
	
	//TIPI DI EVENTO da inserire nella coda prioritaria
	//possiamo modellarla tramite un unico evento, che è l'evento in cui i migranti arrivano in uno stato
	private PriorityQueue<Event> queue;
	
	//PARAMETRI della simulazione
	private int N_MIGRANTI =1000;
	private Country partenza;
	
	//OUTPUT
	private int T=-1;//numero di passi
	private Map<Country, Integer> stanziali;//mi serve per stampare il numero di abitanti stanziali presenti
	
	//metodo di inizializzazione
	//il modello ci passa i parametri della simulazione e il grafo su cui dobbiamo simulare
	public void init(Country partenza, Graph<Country, DefaultEdge> grafo) {
		this.partenza=partenza;
		this.grafo=grafo;
		
		//impostazione dello stato iniziale
		this.T=1;
		stanziali = new HashMap<>();
		//inizializziamo la mappa, prendendo tutti gli stati modellati nel grafo e associarvi inizialmente 
		//un numero di stanziali pari a 0, che andro eventualmente ad incrementare
		for (Country c: this.grafo.vertexSet()) {
			stanziali.put(c, 0);
		}
		
		//dobbiamo creare la coda
		this.queue= new PriorityQueue<>();
		//inserisco il primo evento, ho sia il umero di migranti iniziali, sia lo stato di partenza
		this.queue.add(new Event (T,partenza,N_MIGRANTI));
	}
	
	public void run() {
		//finche c'è un evento nella coda lo estraggo e lo eseguo
		//durante la simulazione la coda potra variare
		Event e;
		
		while ((e=this.queue.poll())!=null) {
			this.T= e.getT(); //l'ultimo T verrà sovrascritto in modo da poterlo restituire
			//Eseguo l'evento e
			//in simulazioni più complicate è dove ho lo switch
			int nPersone = e.getnMigranti();
			Country stato = e.getStato();
			
			//la meta si sposta in parti uguali tra tutti i vicini di stato che devo cercare
			List <Country> vicini = Graphs.neighborListOf(this.grafo, stato); //ho i vicini e so quanti sono, posso 
																				//decidere quanti migranti si spostano
			//essendo le variabili intere viene arrotondato automaticamente per difetto
			int migranti = (nPersone/2)/vicini.size(); //si sposteranno in parti uguali tra gli stati vicini
			
			if (migranti>0) {//si sposteranno in parti uguali tra i vicini
				for (Country confinante: vicini) {
					queue.add(new Event(e.getT()+1, confinante, migranti));
				}
			}
			
			//tengo traccia degli stanziali
			int stanziali = nPersone - migranti*vicini.size();
			//prevedo il caso in cui i migranti possano tornare nello stato da cui sono appena arrivati
			//quindi al posto di sovrascrivere gli stanziali li andiamo ogni volta ad incrementare
			this.stanziali.put(stato, this.stanziali.get(stato)+ stanziali);	
		}
	}
	
	public Map<Country, Integer> getStanziali(){
		return this.stanziali;
	}
	
	//il numero di passi simulati sara l'ultimo T visto nella coda
	public Integer getT() {
		return this.T;
	}

}
