## 도커로 Prometheus 설치 방법
- docker run --name prometheus -p 9090:9090 -v $(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus

## 도커로 Grafana 설치 방법
- 도커 이미지 설치

``
docker pull grafana/grafana
``

- 3000 포트에 설치

``
docker run -d -p 3000:3000 --name=grafana -e "GF_INSTALL_PLUGINS=grafana-clock-panel,grafana-simple-json-datasource" grafana/grafana
``

