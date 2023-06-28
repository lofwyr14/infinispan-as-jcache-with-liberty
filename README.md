# Mit Infinispan

Bauen
```
mvn clean package
docker build -t cart-app:1.0-SNAPSHOT .
```

oder mit Tobago
```
mvn clean package -Ptobago
docker build -t cart-app:1.0-SNAPSHOT .
```

Hochfahren
```
kubectl apply -f kubernetes.yaml
```

Runterfahren
```
kubectl delete -f kubernetes.yaml
```

Browser
```
http://localhost:31000/openapi/ui/
```

Beispielaufrufe in den Dateien
```
rest-api.http
jsf.http
```
