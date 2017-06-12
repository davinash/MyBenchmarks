/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.avdongre.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class UnsafeAvailChecker {

  private static final String CLASS_NAME = "sun.misc.Unsafe";
  private static boolean avail = false;
  private static boolean unaligned = false;

  static {
    avail = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
      @Override
      public Boolean run() {
        try {
          Class<?> clazz = Class.forName(CLASS_NAME);
          Field f = clazz.getDeclaredField("theUnsafe");
          f.setAccessible(true);
          return f.get(null) != null;
        } catch (Throwable e) {
          e.printStackTrace();
        }
        return false;
      }
    });
    // When Unsafe itself is not available/accessible consider unaligned as false.
    if (avail) {
      String arch = System.getProperty("os.arch");
      if ("ppc64".equals(arch) || "ppc64le".equals(arch)) {
        // java.nio.Bits.unaligned() wrongly returns false on ppc (JDK-8165231),
        unaligned = true;
      } else {
        try {
          // Using java.nio.Bits#unaligned() to check for unaligned-access capability
          Class<?> clazz = Class.forName("java.nio.Bits");
          Method m = clazz.getDeclaredMethod("unaligned");
          m.setAccessible(true);
          unaligned = (Boolean) m.invoke(null);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * @return true when running JVM is having sun's Unsafe package available in it and it is
   *         accessible.
   */
  public static boolean isAvailable() {
    return avail;
  }

  /**
   * @return true when running JVM is having sun's Unsafe package available in it and underlying
   *         system having unaligned-access capability.
   */
  public static boolean unaligned() {
    return unaligned;
  }

  private UnsafeAvailChecker() {
    // private constructor to avoid instantiation
  }
}
