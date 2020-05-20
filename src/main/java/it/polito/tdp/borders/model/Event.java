package it.polito.tdp.borders.model;

public class Event implements Comparable<Event>{
	//questa cosa non mi serve perche ho un solo evento
	/*public enum EventType{
		MIGRAZIONE
	}*/
	
	private int t;
	private Country stato; //stato in cui arrivano i migranti al tempo t
	private int nMigranti; //numero di migranti che arrivano nello stato al tempo t, la cui meta si spostera
	/**
	 * @param t
	 * @param stato
	 * @param nMigranti
	 */
	public Event(int t, Country stato, int nMigranti) {
		super();
		this.t = t;
		this.stato = stato;
		this.nMigranti = nMigranti;
	}
	public int getT() {
		return t;
	}
	public void setT(int t) {
		this.t = t;
	}
	public Country getStato() {
		return stato;
	}
	public void setStato(Country stato) {
		this.stato = stato;
	}
	public int getnMigranti() {
		return nMigranti;
	}
	public void setnMigranti(int nMigranti) {
		this.nMigranti = nMigranti;
	}
	@Override
	public int compareTo(Event o) {
		return this.t-o.t;
	}
	
	
}
