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

import com.avdongre.skiptree.ConcurrentSkipTreeMap;
import com.avdongre.snaptree.SnapTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectSortedMaps;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Fork(1)
@Warmup(iterations = 10)
@Measurement(iterations = 50)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ConcurrentMapBenchTest {
  @Param({
      "ConcurrentSkipListMap",
      "SnapTreeMap",
      "Object2ObjectAVLTreeMap-Synchronized",
      "TreeMap",
      "skiptree"
  })
  static String mapClassName;
  @Param({"1000000"})
  static int mapSize;
  static Map<ByteArrayKey, Object> mapInstance;

  public static List<ByteArrayKey> keys;
  public static final Object VAL = new Object();

  @Setup(Level.Iteration)
  static public void shuffleInputKeys() {
    Collections.shuffle(keys);
  }

  @Setup(Level.Trial)
  static public void setup() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    if (mapClassName.equals("Object2ObjectAVLTreeMap-Synchronized")) {
      mapInstance = Object2ObjectSortedMaps.synchronize(new Object2ObjectAVLTreeMap<>());
    } else if (mapClassName.equals("SnapTreeMap")) {
      mapInstance = new SnapTreeMap<>();
    } else if (mapClassName.equals("ConcurrentSkipTreeMap")) {
      mapInstance = new ConcurrentSkipTreeMap<>();
    } else if (mapClassName.equals("ConcurrentHashMap")) {
      mapInstance = new ConcurrentHashMap();
    } else if ( mapClassName.equals("skiptree")) {
      mapInstance = new ConcurrentSkipTreeMap<>();
    } else {
      mapInstance = new ConcurrentSkipListMap<>();
    }

    Random random = new Random(System.nanoTime());

    keys = Stream.generate(() -> {
      byte[] key = new byte[16];
      random.nextBytes(key);
      return new ByteArrayKey(key);
    }).limit(mapSize).collect(Collectors.toList());

    keys.stream().forEach(k -> mapInstance.put(k, VAL));
  }

  // Randomize between invocations
  @State(Scope.Thread)
  public static class LocalRandom {
    Random random = new Random(System.nanoTime());
  }


  @Benchmark
  public static Object testGet(LocalRandom localRandom) {
    int k = localRandom.random.nextInt(mapSize);
    return mapInstance.get(keys.get(k));
  }

  @Benchmark
  public static void testIterateKeySet(Blackhole blackhole) {
    for (ByteArrayKey k : mapInstance.keySet()) {
      blackhole.consume(k);
    }
  }

  @Benchmark
  public static void testIterateEntrySet(Blackhole blackhole) {
    for (Map.Entry<ByteArrayKey, Object> e : mapInstance.entrySet()) {
      blackhole.consume(e.getKey());
    }
  }

  @Benchmark
  public static void testIterateValues(Blackhole blackhole) {
    for (Object v : mapInstance.values()) {
      blackhole.consume(v);
    }
  }
}


