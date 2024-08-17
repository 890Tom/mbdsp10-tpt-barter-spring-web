package com.mbds.barter.model;

public class CountDashboardInsight {
	int pending;
	int accepted;
	int rejected;
	int usersCount;
	int dailyExchange;
	int failedExchange;
	
	public int getPending() {
		return pending;
	}
	public void setPending(int pending) {
		this.pending = pending;
	}
	public int getAccepted() {
		return accepted;
	}
	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}
	public int getRejected() {
		return rejected;
	}
	public void setRejected(int rejected) {
		this.rejected = rejected;
	}
	public int getUsersCount() {
		return usersCount;
	}
	public void setUsersCount(int usersCount) {
		this.usersCount = usersCount;
	}
	public int getDailyExchange() {
		return dailyExchange;
	}
	public void setDailyExchange(int dailyExchange) {
		this.dailyExchange = dailyExchange;
	}
	public int getFailedExchange() {
		return failedExchange;
	}
	public void setFailedExchange(int failedExchange) {
		this.failedExchange = failedExchange;
	}
	
	
}
