
## Approach

1. Implement API endpoints for maintaining the CRUD operations for alerts.
Create a table with id, targetPrice,created and updated date
Foreign key: user_id and currency_id

2. Scheduler
Task running which checks the DB for all alerts with status new and checks the currencyPrice >= targetPrice
Alert is triggered

Secondary Task for sending reminders again if alert is triggered and the notification is not acknowledged.

## Alert CRUD - [Postman collection add for references]
- Alert CRUD [comments are present in the controller file]
prefixPath: /api/alerts
 - create 
    METHOD:POST 
    message body is as follows

    ```sh
        {
            "targetPrice":10,
            "currency":{
                "id":1
            },
            "user":{
                "id":1
            }
        }
    ```
  
 - read
    METHOD:GET 
    - getAllAlerts - gets all the alerts 
    - getAlertsForUser - gets alerts for a particalur user
        - path:/user/{user_id}
    - getAlert - gets a singleAlert
        - path: /{id}
    
 - update
     METHOD:PUT 
   - updateAlertById - updates an alert for a particalur user
        - path:/{alert_id}
        - body: AlertPayload
    - updateAlert - updates alert
        - body: AlertPayload

    - updateStatus - gets a singleAlert
        - path: /{alert_id}
        - body
            ```sh
            {
                "status":"ACKED"
           }
            ````
 - delete
    METHOD:DELETE
    - deleteAlert
        - path: /{alert_id} 

- Change in CurrencyService

    - getEnabledCurrencies was getting all currencies
        - Added a dedicated query which only gets currencies which are enabled.
        - METHOD: GET
        - PATH: /api/currencies
    - getAll gets all currencies
        - METHOD: GET
        - PATH: /api/currencies/all


## Start
Dummy data has been added to the changelog file
1. Start posgres
```sh
    docker compose up
```
2. Start server
```sh
    ./gradlew run
```
OR\
You can start using your IDE


## Run jar file
```sh
    java -jar  .\build\libs\bayz-tracker-0.0.1-SNAPSHOT.jar
```
