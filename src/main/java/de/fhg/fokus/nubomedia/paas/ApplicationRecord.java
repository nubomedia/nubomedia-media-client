/*
 * (C) Copyright 2016 NUBOMEDIA (http://www.nubomedia.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.fhg.fokus.nubomedia.paas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationRecord {
	
	private String id;
	private String vnfr_id;
	private int points;
	private String ip;
	private String mediaServerId;
	private String extAppId;
	

	public String getInternalAppId() {
		return id;
	}

	public void setInternalAppId(String internalAppId) {
		this.id = internalAppId;
	}

	public String getVnfrId() {
		return vnfr_id;
	}

	public void setVnfrId(String vnfrId) {
		this.vnfr_id = vnfrId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int loadPoints) {
		this.points = loadPoints;
	}

	public String getIP() {
		return ip;
	}

	public void setIP(String kurentoMediaServerIP) {
		this.ip = kurentoMediaServerIP;
	}

	public String getmediaServerId() {
		return mediaServerId;
	}

	public void setMediaServerId(String kurentoMediaServerId) {
		this.mediaServerId = kurentoMediaServerId;
	}

	public String getExtAppId() {
		return extAppId;
	}

	public void setExtAppId(String externalAppId) {
		this.extAppId = externalAppId;
	}
	
	public String toString(){
		 return "Response{" +
	                "id='" + id + '\'' +
	                ", vnfr_id=" + vnfr_id + '\'' +
	                ", points=" + points+'\'' +
	                ", ip=" + ip+'\'' +
	                ", mediaServerId=" + mediaServerId+'\'' +
	                ", extAppId=" + extAppId+    
	                '}';
	}
	
}
