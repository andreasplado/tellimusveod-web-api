### LocaWork

# Run: 
```mvn spring-boot:run```

#Connect to db:
```heroku pg:psql postgresql-shallow-93753 --app tellimusveod-web-api```

##Adding Posgre SQL extensions:
```CREATE EXTENSION earthdistance;```
```CREATE EXTENSION cube;```

#Update or create with Flyway
### change application.properties
```spring.jpa.hibernate.ddl-auto=create```

#View server logs
```heroku logs --tail -a tellimusveod-web-api```

#Endpoints

###Gettin offers by location in a distance
Gettin offers by location in a distance. Distance is in METERS! Add your longitude and latitude after '='.
Example with Longitude 152.522067 and latitude 58.698017<br>

```ggg```




