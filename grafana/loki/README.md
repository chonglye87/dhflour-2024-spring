wget 설정
```
wget https://raw.githubusercontent.com/grafana/loki/v3.0.0/cmd/loki/loki-local-config.yaml -O loki-config.yaml
wget https://raw.githubusercontent.com/grafana/loki/v3.0.0/clients/cmd/promtail/promtail-docker-config.yaml -O promtail-config.yaml
```

curl 설정
```
curl -o loki-config.yaml https://raw.githubusercontent.com/grafana/loki/v3.0.0/cmd/loki/loki-local-config.yaml
curl -o promtail-config.yaml https://raw.githubusercontent.com/grafana/loki/v3.0.0/clients/cmd/promtail/promtail-docker-config.yaml
```

docker run
```
docker run --name loki -d -v $(pwd):/mnt/config -p 3100:3100 grafana/loki:3.0.0 -config.file=/mnt/config/loki-config.yaml
docker run --name promtail -d -v $(pwd):/mnt/config -v $(pwd)/../../logs:/mnt/logs -p 9080:9080 --link loki grafana/promtail:3.0.0 -config.file=/mnt/config/promtail-config.yaml
```

```

```