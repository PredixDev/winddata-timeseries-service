Predix Development Kit Winddata Time Series Service
==================================================

Welcome to Winddata Time Series Service. This is a back-end application built in Java and Spring. It is an extension of [predix-microservice-cf-jsr](https://github.com/PredixDev/predix-microservice-cf-jsr). The application exposes Rest APIs for getting time series tags, data points for one year, and the latest data point. 

NOTE: This application relies on setting up of UAA and Time Series service instances in the Predix Cloud. For further information on how to set up, please visit our [Exploring Time Series Journey](https://www.predix.io/resources/tutorials/journey.html#Journey.Exploring Time Series).

##Run integration tests

1. Edit config/application-external.properties as follows. For further information on configuring Predix Time Series service, please refer to the tutorials called [Exploring Time Series](https://www.predix.io/resources/tutorials/journey.html#Journey.Exploring Time Series).

```
predix.oauth.restHost=put.your.uaa.uri.here <For example: 3d1e80a3-4f19-4c52-9a3c-62918ad2660f.predix-uaa-sysint.grc-apps.svc.ice.ge.com>
predix.oauth.clientId=put.your.clientId:put.your.clientSecret <For example: client_123:sssh_its_a_secret>

predix.timeseries.ingestUri=wss://put.your.timeseries.service.instance.here/v1/stream/messages <For example: wss://gateway-predix-sysint.grc-apps.svc.ice.ge.com/v1/stream/messages>


predix.timeseries.baseUrl=https://put.your.timeseries.service.instance.here <For example: https://time-series-store-predix-sysint.svc.ice.ge.com>
predix.timeseries.zoneid=put.your.timeseries.zoneid.aka.instanceid.here  <For example: ac970f91-d252-4e3b-a818-18de337c66fa>
```

## Running locally
1. Edit the manifest.yml with the names of your UAA and Time Series service instances. Also specify the predix_oauthRestHost,    client_id, and secret.
2. From the command line, go the the project directory.
3. Run as

```
mvn spring-boot:run".
```
NOTE: For more info on what to set up, look at manifest.yml.template

## Running in the cloud
1. Set up the manifest.yml for Cloud deployment. Edit the manifest.yml with the names of your UAA and Time Series service instances. Also specify the predix_oauthRestHost, client_id, and secret.
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
