package com.beintoo.wrappers;

public class Leaderboard {
	Alliance alliance;
    User user;
    Integer pos;
    Double score;
    
	public Alliance getAlliance() {
		return alliance;
	}
	public void setAlliance(Alliance alliance) {
		this.alliance = alliance;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getPos() {
		return pos;
	}
	public void setPos(Integer pos) {
		this.pos = pos;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
}
