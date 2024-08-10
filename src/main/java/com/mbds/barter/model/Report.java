package com.mbds.barter.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Report {
	@JsonProperty("_id")
	String id;
	@JsonProperty("usermakereport")
	UserReport userMakeReport;
	UserReport userReport;
	ObjectReport objetReport;
	String motif;
	LocalDateTime dateCreation;
	String statut;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public UserReport getUserMakeReport() {
		return userMakeReport;
	}
	public void setUserMakeReport(UserReport userMakeReport) {
		this.userMakeReport = userMakeReport;
	}
	public UserReport getUserReport() {
		return userReport;
	}
	public void setUserReport(UserReport userReport) {
		this.userReport = userReport;
	}
	public ObjectReport getObjetReport() {
		return objetReport;
	}
	public void setObjetReport(ObjectReport objetReport) {
		this.objetReport = objetReport;
	}
	public String getMotif() {
		return motif;
	}
	public void setMotif(String motif) {
		this.motif = motif;
	}
	public LocalDateTime getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
     
     
}
