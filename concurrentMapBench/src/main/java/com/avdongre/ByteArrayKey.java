package com.avdongre;

import com.avdongre.utils.Bytes;

import java.util.Arrays;

/**
 * Created by adongre on 14/6/17.
 */
public class ByteArrayKey implements Comparable<ByteArrayKey> {
  private final byte[] key;

  public ByteArrayKey(byte[] k) {
    this.key = k;
  }

  @Override
  public int compareTo(ByteArrayKey right) {
    return Bytes.compareTo(this.key, right.key);
  }

  @Override
  public boolean equals(Object right) {
    return right instanceof ByteArrayKey && (Bytes.compareTo(this.key, ((ByteArrayKey) right).key) == 0);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.key);
  }
}
