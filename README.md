# Bitcoin Information

This project aims to know the current market price of bitcoins

## Endpoints

- ##### /coin/find/all
    Allows to obtain all saved bitcoin data price  
- ##### /coin/find
    Allows to find a particular bitcoin with a timestamp as parameter
- ##### /coin/find/average
    Gives info between (included) two given timestamps
    - average price
    - max porcentual difference between average and max saved price

## Installation Pre requisites
    - install docker

## Installation Instructions
   - execute: docker run -p 27018:27017 --name mongo-coin -d mongo:4
   - open the project in your preferred editor
   - run class CoinApplication 

## Attached files
    You can find a sample Postman collection for endpoint testing
    in resources folder

## Known Bugs
