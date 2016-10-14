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

import java.util.Arrays;
import java.util.List;

import org.kurento.client.internal.NotEnoughResourcesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 *
 * Implementation of the VNFRService interface
 * 
 */
@Service
public class VNFRServiceImpl implements VNFRService{

	private static final Logger logger = LoggerFactory.getLogger(VNFRServiceImpl.class);

	private VNFRServiceProfile serviceProfile;
	@Autowired private RestTemplate restTemplate;
	@Autowired private Gson mapper;

	public VNFRServiceImpl() {
		this.serviceProfile = VNFRServiceProfile.getInstance();
		restTemplate = new RestTemplate();
	}
	

	/**
	 * Registers a new App to the VNFR with a specific VNFR ID
	 * 	
	 * @param externalAppId - application identifier
	 * @param points - capacity
	 */
	public ApplicationRecord registerApplication(String externalAppId, int points) throws NotEnoughResourcesException
	{
		try 
		{   
			if(serviceProfile == null)
			{
				logger.info("Service Profile not set. make sure the VNFR_ID, VNFM_IP and VNFM_PORT are available ");
				return null;
			}
			String URL = serviceProfile.getServiceApiUrl();	
			ApplicationRecordBody bodyObj = new ApplicationRecordBody(externalAppId, points);
			Gson mapper = new GsonBuilder().create();
			String body = mapper.toJson(bodyObj, ApplicationRecordBody.class);
					
			logger.info("registering new application: \nURL: "+URL+"\n + "+body);
			HttpHeaders creationHeader = new HttpHeaders();
	        creationHeader.add("Accept","application/json");
	        creationHeader.add("Content-type","application/json");
	        
			HttpEntity<String> registerEntity = new HttpEntity<String>(body,creationHeader);
	        ResponseEntity response = restTemplate.exchange(URL, HttpMethod.POST,registerEntity,String.class);
	        
	        logger.info("response from VNFM "+response);
	        HttpStatus status = response.getStatusCode();
	        if(status.equals(HttpStatus.CREATED)  || status.equals(HttpStatus.OK))
	        {
	        	logger.info("Deployment status: "+status + " response: " + response);
		        ApplicationRecord  responseBody = mapper.fromJson((String) response.getBody(), ApplicationRecord.class);
				
				logger.info("returned object "+ responseBody.toString());
				return responseBody;
	        }
	        else if(status.equals(HttpStatus.UNPROCESSABLE_ENTITY)){
	        	
	        	throw new NotEnoughResourcesException("Not enough resource "+ response.getBody());	        	
	        }	        		
		} catch(NotEnoughResourcesException e){
			logger.error(e.getMessage());		
		}
		catch (RestClientException e) {
			logger.error("Error registering application to VNFR - " + e.getMessage());			
		}
		return null;
	}

	/**
	 * Unregisters an App to the VNFR with the specific internal application identify
	 * 
	 * @param internalAppId - identifier of the registered application on the VNFR
	 */
	public void unregisterApplication(String internalAppId){
		String webServiceUrl = serviceProfile.getServiceApiUrl()+"/"+internalAppId;
		logger.info("unregistering application "+ webServiceUrl);
		
		ResponseEntity<Void> response = restTemplate.exchange(webServiceUrl, HttpMethod.DELETE,
                null, Void.class);
		Void body = response.getBody();
	}

	/**
	 * Returns the list of applications (virtual network function records) registered on the VNF
	 * @return list of application
	 */
	public List<VirtualNetworkFunctionRecord> getListRegisteredVNFR() {
		VirtualNetworkFunctionRecord[] list = restTemplate.getForObject(serviceProfile.getServiceBaseUrl(), VirtualNetworkFunctionRecord[].class);
	   return Arrays.asList(list);		
	}

	/**
	 * Returns the list of registered application to the given virtual record identifier
	 * @param vnfrId - the virtual network function record identifier
	 * @return list - of application records
	 */	
	public List<ApplicationRecord> getListRegisteredApplications(String vnfrId) {
		ApplicationRecord[] list = restTemplate.getForObject(serviceProfile.getServiceApiUrl(), ApplicationRecord[].class);
		   return Arrays.asList(list);
	}


	/**
	 * Sends heart beat as keep alive mechanism to Virtual Network Function
	 * @param internalAppId - the application identifier
	 */	
	public void sendHeartBeat(String internalAppId) {
		// PUT on /vnfr/<vnfr_id>/app/<app_id>/heartbeat
		String webServiceUrl = serviceProfile.getServiceApiUrl()+"/"+internalAppId+"/heartbeat";
		logger.info("sending heartbeat to EMM "+ webServiceUrl);
		
		ResponseEntity<Void> response = restTemplate.exchange(webServiceUrl, HttpMethod.PUT,
                null, Void.class);
		Void body = response.getBody();
		logger.info("response :"+ response);
	}
}
