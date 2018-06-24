# sid-rest
## WaterPoint
This porject is a project for sid-indonesia.org
We need to creater a mining data from https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json and provide api that shows:
- The number of water points that are functional
- The number of water points per community
- The rank for each community by the percentage of broken water points

### Dependency
We need:
- Spring Boot
- Couchbase

### Setup Couchbase
1) You need Couchbase either install it into your system or using docker
Docker Command
```
docker run -d --name db -p 8091-8094:8091-8094 -p 11210:11210 couchbase/server:community-5.1.1
```
2) Use configuration Couchbase:
```
host:localhost
username:root
password:123456
```
3) Create bucket with the name `WaterPoint`
4) Add index with command 
```
CREATE PRIMARY INDEX `communities_villages` ON `WaterPoint` USING GSI;
```

### Execute The Project
For run the project you can simply use `make run`.
For test the project you can simply use `make test`.
For create jar of the project you can simply use `make build`.

### Accessing Api
Api list:

- **stat** - `http://localhost:8080/stat` - used to get overall statistic of all water point, water point per community, and community rank
- **root** - `http://localhost:8080/` - used for re-mining data from source
- **wp/{param}** - `http://localhost:8080/wp/yes` - used for get water point statistic
parameter `yes` means percentage shown are percentage of working water point. `no` means otherwise.
- **wp-per-comm/{param}** - `http://localhost:8080/wp-per-comm/yes` - used for get water point statistic per community
parameter `yes` means percentage shown are percentage of working water point. `no` means otherwise. The community then sort based on the percentage.
On the other hand, you can use `total` as parameter. Then it will sort based on total water point per community.

