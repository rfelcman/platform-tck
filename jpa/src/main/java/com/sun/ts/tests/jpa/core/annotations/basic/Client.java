/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.jpa.core.annotations.basic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

public class Client extends PMClientBase {

  public Client() {
  }
  
  public void createTestData() {
    try {
      getEntityTransaction().begin();
      final Integer integer = 1234;
      final short basicShort = 12;
      final Short basicBigShort = basicShort;
      final float basicFloat = 12.3f;
      final Float basicBigFloat = basicFloat;
      final long basicLong = 1234l;
      final Long basicBigLong = basicLong;
      final double basicDouble = 1234.5;
      final Double basicBigDouble = basicDouble;
      final char[] charArray = { 'a', 'b', 'c' };
      final Character[] bigCharacterArray = { 'a', 'b', 'c' };
      final byte[] byteArray = "abc".getBytes();
      final Byte[] bigByteArray = { (byte) 111, (byte) 101, (byte) 100 };
      final BigInteger bigInteger = new BigInteger("12345");
      final BigDecimal bigDecimal = new BigDecimal(bigInteger);
      final Date date = new Date();
      final long timeInSeconds = date.getTime();
      final Time time = new Time(timeInSeconds);
      final Timestamp timeStamp = new Timestamp(timeInSeconds);
      final Calendar calendar = Calendar.getInstance();

      A aRef = new A("9", null, 9, integer, basicShort, basicBigShort,
          basicFloat, basicBigFloat, basicLong, basicBigLong, basicDouble,
          basicBigDouble, 'a', charArray, bigCharacterArray, byteArray,
          bigByteArray, bigInteger, bigDecimal, date, time, timeStamp,
          calendar);

      getEntityManager().persist(aRef);
      getEntityManager().flush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

  }

  protected void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM A_BASIC")
          .executeUpdate();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }
  }
}
