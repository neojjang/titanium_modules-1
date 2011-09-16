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
package com.beintoo.main.managers;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


import com.beintoo.beintoosdk.BeintooAchievements;
import com.beintoo.beintoosdkutility.JSONconverter;
import com.beintoo.beintoosdkutility.PreferencesHandler;
import com.beintoo.beintoosdkutility.SerialExecutor;
import com.beintoo.main.Beintoo;
import com.beintoo.main.Beintoo.BAchievementListener;
import com.beintoo.wrappers.Player;
import com.beintoo.wrappers.PlayerAchievement;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

public class AchievementManager {
	
	private static Context currentContext;
	private static AtomicLong LAST_OPERATION = new AtomicLong(0);
	private long OPERATION_TIMEOUT = 5000;
	
	public AchievementManager (Context ctx){
		currentContext = ctx;
	}
	
	public void submitAchievementScore(final String achievement, final Float percentage, final Float value, final Boolean increment,
			final boolean showNotification, final int gravity, final BAchievementListener listener){
		SerialExecutor executor = SerialExecutor.getInstance();	
		executor.execute(new Runnable(){     					
    		public void run(){	
    			synchronized (LAST_OPERATION){
					try {
						
						if(System.currentTimeMillis() < LAST_OPERATION.get() + OPERATION_TIMEOUT)
							return;
						
						Player p = JSONconverter.playerJsonToObject(PreferencesHandler.getString("currentPlayer", currentContext));
						BeintooAchievements ba = new BeintooAchievements();
						
						boolean localUnlocked = PreferencesHandler.getBool("achievements"+p.getGuid(), achievement, currentContext);
						
						// IF THE USER HAS ALREADY UNLOCKED THIS ACHIEVEMENT ON THIS DEVICE DON'T DO ANYTHING
						if(!localUnlocked){
							
							// IF THE USER DOESN'T UNLOCKED THE ACHIEVEMENT ON THIS DEVICE 
							// CHECK IF THE ACHIEVEMENT WAS PREVIOUS UNLOCKED ON ANOTHER DEVICE
							List<PlayerAchievement> prevachievements = ba.getPlayerAchievements(p.getGuid());
							boolean previousUnlocked = false;					
							for(PlayerAchievement pa : prevachievements){
								if(pa.getAchievement().getId().equals(achievement)){
									if(pa.getStatus().equals("UNLOCKED")){
										previousUnlocked = true;
										
										// ADD TO THIS DEVICE A PREVIOUS UNLOCKED ACHIEVEMENT FROM ANOTHER DEVICE
										PreferencesHandler.saveBool("achievements"+p.getGuid(), pa.getAchievement().getId(), true, currentContext);
									}
								}  
							}   
							 
							// IF THE ACHIEVEMENT WAS NOT PREVIOUS UNLOCKED UNLOCK IT
							if(!previousUnlocked){
								List<PlayerAchievement> achievements = ba.submitPlayerAchievement(p.getGuid(), achievement, percentage, value, increment);
								StringBuilder message = new StringBuilder("");
								boolean hasUnlocked = false;
								int count = 0;
								for(PlayerAchievement pa : achievements){
									if(pa.getStatus().equals("UNLOCKED")){ 
										hasUnlocked = true; 
										message.append(pa.getAchievement().getName());
										message.append(",");
										
										// SAVE LOCALLY
										PreferencesHandler.saveBool("achievements"+p.getGuid(), pa.getAchievement().getId(), true, currentContext);
										
										count++;
									} 
								}		
									
								if(message.length() > 0)
									message.replace(message.length()-1, message.length(), "");
								
								if(count == 1)
									message.insert(0, currentContext.getString(currentContext.getResources().getIdentifier("achievementunlocked", "string", currentContext.getPackageName())));
								else if(count >1)
									message.insert(0, currentContext.getString(currentContext.getResources().getIdentifier("achievementsunlocked", "string", currentContext.getPackageName())));
								
								if(listener != null){
									listener.onComplete(achievements);
									if(hasUnlocked){								
										listener.onAchievementUnlocked(achievements);
									}
								}
								
								if(hasUnlocked && showNotification){
									Message msg = new Message();
									Bundle b = new Bundle();						
									b.putString("Message", message.toString()); 						
									b.putInt("Gravity", gravity);						
									msg.setData(b);
									msg.what = Beintoo.SUBMITSCORE_POPUP;
									Beintoo.UIhandler.sendMessage(msg);
								}
							}
						}
						
						LAST_OPERATION.set(System.currentTimeMillis());
					}catch(Exception e){
						if(listener != null) listener.onBeintooError(e);
						e.printStackTrace();
					}
				}
    		}
		});
	} 
}
