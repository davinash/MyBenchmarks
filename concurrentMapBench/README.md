**Benchmarking for various sorted maps**

To Build use command

mvn clean install

To Run use command

java -cp target/benchmarks.jar:<Path to fastutil-7.0.2.jar> org.openjdk.jmh.Main -rf <format> -t <number_Of_threads>

To Run Individual Benchmark Run following command

java -cp target/benchmarks.jar:<Path to fastutil-7.0.2.jar> org.openjdk.jmh.Main -rf <format> -t <number_Of_threads> <benchmark Name>

