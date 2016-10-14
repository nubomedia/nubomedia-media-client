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

package de.fhg.fokus.nubomedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import de.fhg.fokus.nubomedia.kmc.KmsUrlProvider;
import de.fhg.fokus.nubomedia.paas.ApplicationRecord;
import de.fhg.fokus.nubomedia.paas.VNFRService;
import de.fhg.fokus.nubomedia.paas.VNFRServiceImpl;

/**
 * Hello world!
 *
 */
public class App implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String args[]) {
        SpringApplication.run(App.class);
    	
    }
      
    
    @Override
    public void run(String... strings) throws Exception {
    	registerApplication();
    	//getEnvironmentVariabes();
    }
    
    private void getEnvironmentVariabes() {
    	
    	String serverAddress = System.getenv("VNFM_IP");
    	int serverPort = Integer.parseInt(System.getenv("VNFM_PORT"));
		String vnfrId = System.getenv("VNFR_ID");
		
    	log.info("Instatiating the VNFR service profile with ff System Properties;\n VNFM_IP: "
    			+ serverAddress+"\nVNFM_PORT ....: "
    			+ serverPort+"\n"
    			+ vnfrId);
		
    	
	}


	public void makeFile(){
    	try {
            String fileName = System.getProperty("user.home")+"/.kurento/config.properties";
            
            log.info("saving the property file here : " + fileName);
            
            java.util.Properties p = new java.util.Properties();
            p.setProperty("kms.url.provider", "de.fhg.fokus.nubomedia.kmc.KmsProvider");
            
            File file = new File(fileName);
			FileOutputStream fileOut = new FileOutputStream(file);
			p.store(fileOut, "config.properties");
			fileOut.close();

        } catch (FileNotFoundException e) {
            System.out.println("property file could not be found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void registerApplication(){
    	
    	KmsUrlProvider provider = new KmsUrlProvider();
		String appId = "abcirephmfpf-098823";
		String url = provider.reserveKms(appId);
		if(url !=null)
    	{
    		provider.releaseKms(appId);
    	}
    }
}