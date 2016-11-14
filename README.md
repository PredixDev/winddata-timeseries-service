<a href="../../../../pages/adoption/winddata-timeseries-service/javadocs/index.html" target="_blank" >
	<img height="50px" width="100px" src="images/javadoc.png" alt="view javadoc"></a>
&nbsp;
<a href="../../../../pages/adoption/winddata-timeseries-service" target="_blank">
	<img height="50px" width="100px" src="images/pages.jpg" alt="view github pages">
</a>
Predix Development Kit Winddata Time Series Service
==================================================

Welcome to Winddata Time Series Service. This is a back-end application built in Java and Spring. It is an extension of [predix-microservice-cf-jsr](https://github.com/PredixDev/predix-microservice-cf-jsr). The application exposes Rest APIs for getting time series tags, data points for one year, and the latest data point. 

NOTE: This application relies on setting up of UAA and Time Series service instances in the Predix Cloud. For further information on how to set up, please visit our [Exploring Time Series Guide](https://www.predix.io/resources/tutorials/journey.html#Journey.Exploring Time Series).

##Run integration tests

1. Ensure you have set up your predix.io username/encrypted password in the maven settings.xml. Instruction are found [here](https://www.predix.io/resources/tutorials/tutorial-details.html?tutorial_id=1560&tag=1608&journey=Development%20tools%20and%20tips&resources=1565,1560).

1. Edit config/application.properties as follows. For further information on configuring Predix Time Series service, please refer to the tutorials called [Exploring Time Series](https://www.predix.io/resources/tutorials/journey.html#Journey.Exploring Time Series).

```
 predix.oauth.issuerId.url=put.your.uaa.issuerId.here
 #you may put client:secret as unencoded cleartext by setting predix.oauth.clientIdEncode=true
 predix.oauth.clientIdEncode=false
 predix.oauth.clientId=you.should.base64encode(put.your.clientId:put.your.clientSecret separated by a colon)  

 predix.timeseries.queryUrl=https://put.your.timeseries.service.instance.here/v1/datapoints 
 predix.timeseries.zoneid=put.your.timeseries.zoneid.aka.instanceid.here
 predix.timeseries.websocket.uri=wss://put.your.timeseries.ingest.uri.here/v1/stream/messages 
```

## Running locally
1. Edit the manifest.yml with the names of your UAA and Time Series service instances. Also specify the     client_id, and secret.
2. From the command line, go the the project directory.
3. Run as

```
mvn spring-boot:run
```
NOTE: For more info on what to set up, look at manifest.yml.template

## Running in the cloud
1. Set up the manifest.yml for Cloud deployment. **Edit the manifest.yml with the names of your UAA and Time Series service instances. Also specify the client_id, and secret**.
2. NOTE: For more info on what to set up, look at manifest.yml.template
3. From the command line, login to your Cloud Foundry org and space where you would like to deploy.
4. Run as 
```
cf push <appName> -f manifest.yml
```

##Tech Stack

 - Spring
 - SpringBoot
 - SpringTest
 - Maven
 
##More Details

[Exploring Time Series](https://www.predix.io/resources/tutorials/journey.html#Journey.Exploring Time Series)

[![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/winddata-timeseries-service/readme?pixel)](https://github.com/PredixDev)
