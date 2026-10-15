package com.app.db;

public class OTPSchema {
	public long getIssueTime() {
		return issueTime;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public int getMax_attempt() {
		return max_attempt;
	}
	public int getCurrent_attempt() {
		return current_attempt;
	}
	public void increase_current_attempt() {
		if(this.current_attempt < this.max_attempt) {
			this.current_attempt = this.current_attempt + 1;
		}
	}
	
	
	private long issueTime;
	private long expireTime;
	private int max_attempt;
	private int current_attempt;
	
	public OTPSchema(long issueTime, Long expireTime) {
		this.issueTime = issueTime;
		this.expireTime = expireTime;
		this.max_attempt = 3;
		this.current_attempt = 0;
	}
}
