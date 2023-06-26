# Mit Infinispan

Bauen
```
mvn clean package
docker build -t cart-app:1.0-SNAPSHOT .
```

Hochfahren
```
kubectl apply -f kubernetes.yaml
```

Runterfahren
```
kubectl apply -f kubernetes.yaml
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
