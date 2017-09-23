GEODE_HOME=/home/adongre/source/open/geode/geode-assembly/build/install/apache-geode
GFSH_COMMAND=$GEODE_HOME/bin/gfsh
rm -rf locator server0
mvn clean install
$GFSH_COMMAND -e "start locator --name=locator" -e "start server --name=server0 --initial-heap=4g --max-heap=4g --max-connections=10000" -e "create region --name=TEST_REGION --type=PARTITION --total-num-buckets=1"
java -cp target/benchmarks.jar:$GEODE_HOME/lib/* org.openjdk.jmh.Main -t 1 -wi 10 -i 20
$GFSH_COMMAND -e "connect" -e "shutdown --include-locators=true"

