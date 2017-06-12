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

import com.avdongre.snaptree.SnapTreeMap;
import com.avdongre.utils.Bytes;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectSortedMaps;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Fork(1)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ConcurrentMapBenchTest {
  @Param({
      "ConcurrentSkipListMap",
      "SnapTreeMap",
      "Object2ObjectSortedMaps"
  })
  static String mapClassName;
  @Param({"10000"})
  static long mapSize;
  static SortedMap<byte[], Object> MAP;

  public static List<byte[]> KEYS;
  public static final Object VAL = new Object();


  @Setup(Level.Trial)
  static public void setup() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    if (mapClassName.equals("Object2ObjectSortedMaps")) {
      MAP = Object2ObjectSortedMaps.synchronize(new Object2ObjectAVLTreeMap<>(Bytes.BYTES_COMPARATOR));
    } else if ( mapClassName.equals("SnapTreeMap")) {
      MAP = new SnapTreeMap<>(Bytes.BYTES_COMPARATOR);
    } else {
      MAP = new ConcurrentSkipListMap<>(Bytes.BYTES_COMPARATOR);
    }

    Random random = new Random(System.nanoTime());

    KEYS = Stream.generate(() -> {
      byte[] key = new byte[16];
      random.nextBytes(key);
      return key;
    }).limit(mapSize).collect(Collectors.toList());

    KEYS.stream().forEach(k -> MAP.put(k, VAL));
  }

  @Benchmark
  @Threads(10)
  public static void testGet(Blackhole blackhole) {
    KEYS.stream().forEach(k -> blackhole.consume(MAP.get(k)));
  }

  @Benchmark
  @Threads(10)
  public static long testIterateKeySet(Blackhole blackhole) {
    long sum = 0;
    for (byte[] k : MAP.keySet()) {
      blackhole.consume(k);
    }
    return sum;
  }

  @Benchmark
  @Threads(10)
  public static long testIterateEntrySet(Blackhole blackhole) {
    long sum = 0;
    for (Map.Entry<byte[], Object> e : MAP.entrySet()) {
      blackhole.consume(e.getKey());
    }
    return sum;
  }


  @Benchmark
  @Threads(10)
  public static long testIterateValues(Blackhole blackhole) {
    long sum = 0;
    for (Object v : MAP.values()) {
      blackhole.consume(v);
    }
    return sum;
  }
}


