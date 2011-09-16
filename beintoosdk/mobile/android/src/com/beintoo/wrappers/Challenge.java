/*******************************************************************************
 * Copyright 2011 Beintoo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.beintoo.wrappers;

public class Challenge {
	Contest contest;
	Player playerFrom;
	Player playerTo;
	String startdate;
	String enddate;
	Double playerFromScore;
    Double playerToScore; // FIXME: implement
    Player winner;
    String status;
    Double prize;
    Double price;
    
	//public Challenge (){}
	
	public Contest getContest() {
		return contest;
	}
	public void setContest(Contest contest) {
		this.contest = contest;
	}
	public Player getPlayerFrom() {
		return playerFrom;
	}
	public void setPlayerFrom(Player playerFrom) {
		this.playerFrom = playerFrom;
	}
	public Player getPlayerTo() {
		return playerTo;
	}
	public void setPlayerTo(Player playerTo) {
		this.playerTo = playerTo;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Double getPlayerFromScore() {
		return playerFromScore;
	}

	public void setPlayerFromScore(Double playerFromScore) {
		this.playerFromScore = playerFromScore;
	}

	public Double getPlayerToScore() {
		return playerToScore;
	}

	public void setPlayerToScore(Double playerToScore) {
		this.playerToScore = playerToScore;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public Double getPrize() {
		return prize;
	}

	public void setPrize(Double prize) {
		this.prize = prize;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
}
