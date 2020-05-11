# Covid19NJStats

Covid19NJStats it is a android app, which had the propose of learning how to create apps in android. It tracks covid19 stats in New Jersey and Essex county gathering data from a Rails API.  
The app could not be published because it needs a especial permission from the government to use the "Covid19" term.

## how it works/features

Main activity has the last updates (number of positive,negative and deaths) from the state and county and a chart with comparison between both. Also there is another chart with the growth percentage of positive cases per day on State/Essex.

When new data is added on the API, all covid19NJstats users receive a notification in their phones.

The app also uses Biometric Authentication to open the app (useless just for fun) and the API and the App are using Auth0 authentication. 

The API is deployed on heroku. 
```
https://covid19njapi.herokuapp.com/...
```  

## Setup 

1. Clone the repo
2. Add apikey.properties with auth0 API creds:
```
CLIENT_ID=
CLIENT_SECRET=
AUDIENCE=
GRANT_TYPE=
```

3. Add app/google-services.json with firebase configuration

## TODO

- There is more data on the API than what is showed in the APP, more charts could be added.
- More tests should be added.
- Better design is needed.  