package ti.modules.beintoo;

import java.util.List;
import java.util.Map;

import org.appcelerator.kroll.annotations.Kroll;

import com.beintoo.beintoosdk.BeintooAchievements;
import com.beintoo.beintoosdk.BeintooApp;
import com.beintoo.beintoosdk.BeintooPlayer;
import com.beintoo.beintoosdk.BeintooUser;
import com.beintoo.beintoosdk.BeintooVgood;
import com.beintoo.wrappers.AchievementWrap;
import com.beintoo.wrappers.Challenge;
import com.beintoo.wrappers.EntryCouplePlayer;
import com.beintoo.wrappers.Message;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.PlayerAchievement;
import com.beintoo.wrappers.PlayerScore;
import com.beintoo.wrappers.User;
import com.beintoo.wrappers.Vgood;
import com.beintoo.wrappers.VgoodChooseOne;

public class Api {
	/*
	 * PLAYER RESOURCE
	 */
	
	@Kroll.method
	public Player playerLogin(String userExt, String guid, String codeID, String language){
		try {
			BeintooPlayer bp = new BeintooPlayer();
			return bp.playerLogin(userExt, guid, codeID, null, language, null); 
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public Player getPlayer(String guid){
		try{
			BeintooPlayer bp = new BeintooPlayer();
			return bp.getPlayer(guid);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public Player getPlayerByUser(String userExt, String codeID){
		try{
			BeintooPlayer bp = new BeintooPlayer();
			return bp.getPlayerByUser(userExt, codeID);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public PlayerScore getPlayerByUser(String guid){
		try{
			BeintooPlayer bp = new BeintooPlayer();
			return bp.getScore(guid);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public PlayerScore getScoreForContest(String guid, String codeID){
		try{
			BeintooPlayer bp = new BeintooPlayer();
			return bp.getScoreForContest(guid, codeID);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public Message submitScore(String guid, String codeID, String deviceUUID, int lastScore, int balance,
			String latitude, String longitude, String radius, String language){
		try{
			BeintooPlayer bp = new BeintooPlayer();
			return bp.submitScore(guid, codeID, deviceUUID, lastScore, balance, latitude, longitude, radius, language);
		}catch(Exception e){
			return null;
		}
	}
	
	/*
	 * USER RESOURCE
	 */
	
	@Kroll.method
	public User getUserByUserExt(String userExt, String codeID){
		try{
			BeintooUser bu = new BeintooUser();
			return bu.getUserByUserExt(userExt, codeID);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public User getUserByEmail(String email, String password){
		try{
			BeintooUser bu = new BeintooUser();
			return bu.getUsersByEmail(email, password);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public User setOrUpdateUser(String action, String guid, String userExt, String email, String nickname, String password, String name, String address, String country, Integer gender, Boolean sendGreetingsEmail, String imageurl, String codeID){
		try{
			BeintooUser bu = new BeintooUser();
			return bu.setOrUpdateUser(action, guid, userExt, email, nickname, password, name, address, country, gender, sendGreetingsEmail, imageurl, codeID);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public Challenge challenge(String userExtFrom, String userExtTo, String action, String codeID){
		try{
			BeintooUser bu = new BeintooUser();
			return bu.challenge(userExtFrom, userExtTo, action, codeID);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public Challenge[] challengeShow(String userExt, String status, String codeID){
		try{
			BeintooUser bu = new BeintooUser();
			return bu.challengeShow(userExt, status, codeID);
		}catch(Exception e){
			return null;
		}
	}
	
	/*
	 * APP RESOURCE
	 */
	
	@Kroll.method
	public Map<String, List<EntryCouplePlayer>> topScore(String codeID, int rows){
		try{
			BeintooApp ba = new BeintooApp();
			return ba.topScore(codeID, rows);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public Map<String, List<EntryCouplePlayer>> topScore(String codeID, int rows, String userExt){
		try{
			BeintooApp ba = new BeintooApp();
			return ba.topScoreByUserExt(codeID, rows, userExt);
		}catch(Exception e){
			return null;
		}
	}
	
	/*
	 * ACHIEVEMENT RESOURCE
	 */
	
	@Kroll.method
	public List<AchievementWrap> getAppAchievements(){
		try{
			BeintooAchievements ba = new BeintooAchievements();
			return ba.getAppAchievements();
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public List<PlayerAchievement> submitPlayerAchievement(String guid, String achievement, Float percentage, Float value, boolean increment){
		try{
			BeintooAchievements ba = new BeintooAchievements();
			return ba.submitPlayerAchievement(guid, achievement, percentage, value, increment);
		}catch(Exception e){
			return null;
		}
	}
	
	/*
	 * VGOOD RESOURCE
	 */
	
	@Kroll.method
	public Vgood getVgood(String guid, String codeID,String latitude, String longitude, String radius, boolean privategood) {
		try{
			BeintooVgood bv = new BeintooVgood();
			return bv.getVgood(guid, codeID, latitude, longitude, radius, privategood);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public VgoodChooseOne getVgoodList(String guid, String codeID,String latitude, String longitude, String radius, boolean privategood) {
		try{
			BeintooVgood bv = new BeintooVgood();
			return bv.getVgoodList(guid, codeID, latitude, longitude, radius, privategood);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public Vgood acceptVgood (String vgoodExt, String userExt, String codeID){
		try{
			BeintooVgood bv = new BeintooVgood();
			return bv.acceptVgood(vgoodExt, userExt, codeID);
		}catch(Exception e){
			return null;
		}
	}
	
	@Kroll.method
	public Vgood[] showByUser (String userExt, String codeID, boolean onlyConverted){
		try{
			BeintooVgood bv = new BeintooVgood();
			return bv.showByUser(userExt, codeID, onlyConverted);
		}catch(Exception e){
			return null;
		}
	}
	
}
