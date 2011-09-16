package com.beintoo.wrappers;

import java.util.List;

public class LeaderboardContainer {
	Contest contest;
	Leaderboard currentUser;
	List<Leaderboard> leaderboard;
	
    public Contest getContest() {
		return contest;
	}
	public void setContest(Contest contest) {
		this.contest = contest;
	}
	public Leaderboard getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(Leaderboard currentUser) {
		this.currentUser = currentUser;
	}
	public List<Leaderboard> getLeaderboard() {
		return leaderboard;
	}
	public void setLeaderboard(List<Leaderboard> leaderboard) {
		this.leaderboard = leaderboard;
	}
	
}
