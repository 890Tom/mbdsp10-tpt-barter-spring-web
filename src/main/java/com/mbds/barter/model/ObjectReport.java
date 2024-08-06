package com.mbds.barter.model;

public class ObjectReport {
	String _id;
    int id;
    String nom;
    int proprietaireId;
    int categorieId;
    
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getProprietaireId() {
		return proprietaireId;
	}
	public void setProprietaireId(int proprietaireId) {
		this.proprietaireId = proprietaireId;
	}
	public int getCategorieId() {
		return categorieId;
	}
	public void setCategorieId(int categorieId) {
		this.categorieId = categorieId;
	}
    
    
}
