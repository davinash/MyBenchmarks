The benchmarks use [Java microbenchmark harness](http://openjdk.java.net/projects/code-tools/jmh/) to provide an accurate analysis.
This benchmark uses following various maps.
* ConcurrentSkipListMap
* [SnapTreeMap](https://github.com/nbronson/snaptree)
* [Object2ObjectAVLTreeMap-Synchronized](http://fastutil.di.unimi.it/)
* TreeMap
* [lockfreeskiptree](https://github.com/mspiegel/lockfreeskiptree)

All the maps are benchmarked against following.
* Get Operation
* Iterate Using EntrySet
* Iterate Using KeySet
* Iterate Using Values.

Map is populated in the `setup` method of the `jmh`. Map is populated with 1M keys.
Each key is 16 bytes long. Key is wrapped into Object of class `ByteArrayKey`

#### Machine Configuration
Desktop-class Machine
Intel(R) Core(TM) i7-5500U CPU @ 2.40GHz (4 core) 16 GB Ubuntu 16.04.2 LTS.

#### Purpose
Observe the behavior for pure get and scan operations under different number of threads.

#### How to Run 
##### Building
```bash
mvn clean install
```
##### Running the Benchmark
```bash
java -cp target/benchmarks.jar:<FAST_UTILS_HOME>fastutil-7.0.2.jar  org.openjdk.jmh.Main -t <NUM_OF_THREADS> -wi <WARMUP_ITERATION> -i <MESUREMENT_ITERATION>
```

## Results
#### Number Of Threads 1, 50 iterations

```bash
# Run complete. Total time: 00:21:35

Benchmark                                                         (mapClassName)  (mapSize)   Mode  Cnt       Score       Error  Units
ConcurrentMapBenchTest.testGet                             ConcurrentSkipListMap    1000000  thrpt   50  415926.636 ±  2312.720  ops/s
ConcurrentMapBenchTest.testGet                                       SnapTreeMap    1000000  thrpt   50  813152.767 ±  2734.919  ops/s
ConcurrentMapBenchTest.testGet              Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50  883205.764 ±  2583.381  ops/s
ConcurrentMapBenchTest.testGet                                           TreeMap    1000000  thrpt   50  421885.133 ±  2431.392  ops/s
ConcurrentMapBenchTest.testGet                                          skiptree    1000000  thrpt   50  892700.189 ± 23179.855  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                 ConcurrentSkipListMap    1000000  thrpt   50      14.048 ±     0.051  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                           SnapTreeMap    1000000  thrpt   50      13.655 ±     0.073  ops/s
ConcurrentMapBenchTest.testIterateEntrySet  Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50      12.758 ±     0.071  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                               TreeMap    1000000  thrpt   50      14.060 ±     0.087  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                              skiptree    1000000  thrpt   50      80.529 ±     0.198  ops/s
ConcurrentMapBenchTest.testIterateKeySet                   ConcurrentSkipListMap    1000000  thrpt   50      13.609 ±     0.061  ops/s
ConcurrentMapBenchTest.testIterateKeySet                             SnapTreeMap    1000000  thrpt   50      11.736 ±     0.018  ops/s
ConcurrentMapBenchTest.testIterateKeySet    Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50      12.132 ±     0.037  ops/s
ConcurrentMapBenchTest.testIterateKeySet                                 TreeMap    1000000  thrpt   50      13.746 ±     0.045  ops/s
ConcurrentMapBenchTest.testIterateKeySet                                skiptree    1000000  thrpt   50      29.873 ±     0.409  ops/s
ConcurrentMapBenchTest.testIterateValues                   ConcurrentSkipListMap    1000000  thrpt   50      13.486 ±     0.175  ops/s
ConcurrentMapBenchTest.testIterateValues                             SnapTreeMap    1000000  thrpt   50      11.915 ±     0.086  ops/s
ConcurrentMapBenchTest.testIterateValues    Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50      12.822 ±     0.047  ops/s
ConcurrentMapBenchTest.testIterateValues                                 TreeMap    1000000  thrpt   50      13.667 ±     0.050  ops/s
ConcurrentMapBenchTest.testIterateValues                                skiptree    1000000  thrpt   50      80.842 ±     0.561  ops/s

```

#### Number Of Threads 2, 50 iterations

```bash
# Run complete. Total time: 00:22:35

Benchmark                                                         (mapClassName)  (mapSize)   Mode  Cnt        Score       Error  Units
ConcurrentMapBenchTest.testGet                             ConcurrentSkipListMap    1000000  thrpt   50   775900.453 ± 21101.936  ops/s
ConcurrentMapBenchTest.testGet                                       SnapTreeMap    1000000  thrpt   50  1477096.538 ± 56953.643  ops/s
ConcurrentMapBenchTest.testGet              Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50   757954.624 ± 18789.309  ops/s
ConcurrentMapBenchTest.testGet                                           TreeMap    1000000  thrpt   50   765333.489 ± 24695.606  ops/s
ConcurrentMapBenchTest.testGet                                          skiptree    1000000  thrpt   50  1440315.228 ± 84809.635  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                 ConcurrentSkipListMap    1000000  thrpt   50       27.274 ±     0.224  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                           SnapTreeMap    1000000  thrpt   50       25.668 ±     0.783  ops/s
ConcurrentMapBenchTest.testIterateEntrySet  Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50       24.568 ±     0.305  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                               TreeMap    1000000  thrpt   50       27.607 ±     0.121  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                              skiptree    1000000  thrpt   50      162.982 ±     7.335  ops/s
ConcurrentMapBenchTest.testIterateKeySet                   ConcurrentSkipListMap    1000000  thrpt   50       26.628 ±     0.056  ops/s
ConcurrentMapBenchTest.testIterateKeySet                             SnapTreeMap    1000000  thrpt   50       22.496 ±     0.722  ops/s
ConcurrentMapBenchTest.testIterateKeySet    Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50       23.434 ±     0.302  ops/s
ConcurrentMapBenchTest.testIterateKeySet                                 TreeMap    1000000  thrpt   50       26.641 ±     0.026  ops/s
ConcurrentMapBenchTest.testIterateKeySet                                skiptree    1000000  thrpt   50       55.312 ±     2.772  ops/s
ConcurrentMapBenchTest.testIterateValues                   ConcurrentSkipListMap    1000000  thrpt   50       27.156 ±     0.194  ops/s
ConcurrentMapBenchTest.testIterateValues                             SnapTreeMap    1000000  thrpt   50       22.715 ±     0.468  ops/s
ConcurrentMapBenchTest.testIterateValues    Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50       24.965 ±     0.074  ops/s
ConcurrentMapBenchTest.testIterateValues                                 TreeMap    1000000  thrpt   50       26.995 ±     0.213  ops/s
ConcurrentMapBenchTest.testIterateValues                                skiptree    1000000  thrpt   50      164.065 ±     7.074  ops/s

```

#### Number Of Threads 4, 50 iterations

```bash
# Run complete. Total time: 00:23:02

Benchmark                                                         (mapClassName)  (mapSize)   Mode  Cnt        Score       Error  Units
ConcurrentMapBenchTest.testGet                             ConcurrentSkipListMap    1000000  thrpt   50  1212820.633 ± 38392.222  ops/s
ConcurrentMapBenchTest.testGet                                       SnapTreeMap    1000000  thrpt   50  2967924.727 ± 51095.645  ops/s
ConcurrentMapBenchTest.testGet              Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50   808006.493 ± 26278.517  ops/s
ConcurrentMapBenchTest.testGet                                           TreeMap    1000000  thrpt   50  1221276.910 ± 20091.101  ops/s
ConcurrentMapBenchTest.testGet                                          skiptree    1000000  thrpt   50  2631830.792 ± 69570.217  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                 ConcurrentSkipListMap    1000000  thrpt   50       53.841 ±     1.071  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                           SnapTreeMap    1000000  thrpt   50       40.254 ±     0.431  ops/s
ConcurrentMapBenchTest.testIterateEntrySet  Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50       47.038 ±     0.812  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                               TreeMap    1000000  thrpt   50       54.592 ±     0.484  ops/s
ConcurrentMapBenchTest.testIterateEntrySet                              skiptree    1000000  thrpt   50      212.631 ±     2.407  ops/s
ConcurrentMapBenchTest.testIterateKeySet                   ConcurrentSkipListMap    1000000  thrpt   50       48.366 ±     1.100  ops/s
ConcurrentMapBenchTest.testIterateKeySet                             SnapTreeMap    1000000  thrpt   50       50.686 ±     0.616  ops/s
ConcurrentMapBenchTest.testIterateKeySet    Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50       38.574 ±     0.627  ops/s
ConcurrentMapBenchTest.testIterateKeySet                                 TreeMap    1000000  thrpt   50       49.498 ±     0.824  ops/s
ConcurrentMapBenchTest.testIterateKeySet                                skiptree    1000000  thrpt   50       74.406 ±     1.287  ops/s
ConcurrentMapBenchTest.testIterateValues                   ConcurrentSkipListMap    1000000  thrpt   50       54.643 ±     1.135  ops/s
ConcurrentMapBenchTest.testIterateValues                             SnapTreeMap    1000000  thrpt   50       38.501 ±     0.305  ops/s
ConcurrentMapBenchTest.testIterateValues    Object2ObjectAVLTreeMap-Synchronized    1000000  thrpt   50       47.047 ±     0.560  ops/s
ConcurrentMapBenchTest.testIterateValues                                 TreeMap    1000000  thrpt   50       54.379 ±     1.134  ops/s
ConcurrentMapBenchTest.testIterateValues                                skiptree    1000000  thrpt   50      213.582 ±     1.575  ops/s

```