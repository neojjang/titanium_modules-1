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
package com.beintoo.beintoosdkutility;

import java.util.List;

import android.location.LocationManager;

public class LocationManagerUtils {
	public static boolean isProviderSupported(String in_Provider, LocationManager locMgr)
	  {
	      /* locals */
	      int  lv_N;
	      List<?> lv_List;

	    try {
	      lv_List = locMgr.getAllProviders();
	    } catch (Throwable e) {
	      return false;
	    }

	    // scan the list for the specified provider
	    for (lv_N = 0; lv_N < lv_List.size(); ++lv_N)
	      if (in_Provider.equals((String)lv_List.get(lv_N)))
	        return true;

	    // not supported
	    return false;
	  }
}
