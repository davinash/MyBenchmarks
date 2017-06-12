/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.avdongre;

import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectSortedMaps;
import org.openjdk.jmh.annotations.*;

import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ConcurrentMapBenchTest {
  @Param({
      "java.util.concurrent.ConcurrentSkipListMap",
      "com.avdongre.snaptree.SnapTreeMap",
      "Object2ObjectSortedMaps"
  })
  static String mapClassName;
  @Param({"10000000"})
  static long mapSize;
  static SortedMap<Long, Long> map;

  @Setup(Level.Trial)
  static public void setup() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    if (mapClassName.equals("Object2ObjectSortedMaps")) {
      map = Object2ObjectSortedMaps.synchronize(new Object2ObjectAVLTreeMap<>());
    } else {
      Class<SortedMap<Long, Long>> mapClass =
          (Class<SortedMap<Long, Long>>) Class.forName(mapClassName);
      map = mapClass.newInstance();
    }
    Random random = new Random(System.nanoTime());
    System.gc();
    // Load up the Map
    for (long i = 0; i < mapSize; i++) {
      long v = random.nextLong();
      map.put(v, v);
    }
    System.gc();
  }

  // Randomize between invocations
  @State(Scope.Thread)
  public static class LocalRandom {
    Random random = new Random(System.nanoTime());
  }

//  // We have to do the removes too, to keep the Map the same size.
//  @Benchmark
//  public static long testPutAndRemove(LocalRandom localRandom) {
//    long k = localRandom.random.nextLong();
//    map.put(k, k);
//    map.remove(k);
//    return k;
//  }

  @Benchmark
  @Threads(10)
  public static long testGet(LocalRandom localRandom) {
    long k = localRandom.random.nextLong();
    // v is almost always null. This biases hashmaps measurements
    Long v = map.get(k);
    if (v != null)
      System.out.println("v != null");
    return v == null ? 0 : v.longValue();
  }

//  @Benchmark
//  public static long testIterateKeySet() {
//    long sum = 0;
//    for (Long k : map.keySet()) {
//      sum += k.longValue();
//    }
//    return sum;
//  }
//
//  @Benchmark
//  public static long testIterateEntrySet() {
//    long sum = 0;
//    for (Map.Entry<Long, Long> e : map.entrySet()) {
//      sum += e.getKey().longValue();
//    }
//    return sum;
//  }
//
//  @Benchmark
//  public static long testIterateValues() {
//    long sum = 0;
//    for (Long v : map.values()) {
//      sum += v.longValue();
//    }
//    return sum;
//  }
}


