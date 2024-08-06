package com.mbds.barter.model;

import java.time.LocalDateTime;
import java.util.List;

public class Post {
	
	int id;
    int authorId;
    List<Integer> objectIds;
    List<Integer> suggestionIds;
    LocalDateTime deletedAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
     
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public List<Integer> getObjectIds() {
		return objectIds;
	}
	public void setObjectIds(List<Integer> objectIds) {
		this.objectIds = objectIds;
	}
	public List<Integer> getSuggestionIds() {
		return suggestionIds;
	}
	public void setSuggestionIds(List<Integer> suggestionIds) {
		this.suggestionIds = suggestionIds;
	}
	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
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
