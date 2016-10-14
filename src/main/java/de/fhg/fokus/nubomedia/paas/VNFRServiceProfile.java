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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Virtual network function record service profile. Contains information to access the REST interface on VNF
 *
 */

public class VNFRServiceProfile {


	private static VNFRServiceProfile instance;
	private static final Logger logger = LoggerFactory.getLogger(VNFRServiceProfile.class);

	private String serverAddress;
	private int serverPort;
	private String vnfrId;
	private String serviceApiUrl;
	private String serviceBaseUrl;

	public VNFRServiceProfile() {
		super();
		logger.info("Instantiating the VNFR service profile with ff System Properties;\n VNFM_IP: "
		+ System.getenv("VNFM_IP")+"\nVNFM_PORT: "
		+ System.getenv("VNFM_PORT")+"\n"
		+ System.getenv("VNFR_ID"));
				
		serverAddress = System.getenv("VNFM_IP");
		serverPort = Integer.parseInt(System.getenv("VNFM_PORT"));
		vnfrId = System.getenv("VNFR_ID");
		
		if(vnfrId == null)
			instance  = null;
		else{
			this.serviceBaseUrl = "http://"+serverAddress+":"+serverPort+"/vnfr";
			this.serviceApiUrl = serviceBaseUrl+"/"+vnfrId+"/app";
			instance = this;
		}		
	}


	public static VNFRServiceProfile getInstance() {

		if(instance == null)
			new VNFRServiceProfile();		

		return instance;
	}

	public String getServiceBaseUrl() {
		return serviceBaseUrl;
	}

	public String getVnfrId() {
		return vnfrId;
	}

	public void setVnfrId(String vnfrId) {
		this.vnfrId = vnfrId;
	}

	public static void setInstance(VNFRServiceProfile instance) {
		VNFRServiceProfile.instance = instance;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServiceApiUrl() {
		return serviceApiUrl;
	}	


}
