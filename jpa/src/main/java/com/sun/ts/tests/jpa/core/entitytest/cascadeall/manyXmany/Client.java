/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */
package com.sun.ts.tests.jpa.core.entitytest.cascadeall.manyXmany;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

public class Client extends PMClientBase {

  public Client() {
  }


  public void setup() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeTestData();
    } catch (Exception e) {
      throw new Exception("Setup failed:", e);

    }
  }

  /*
   * BEGIN Test Cases
   */
  /*
   * @testName: cascadeAllMXMTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1093; PERSISTENCE:SPEC:618;
   * PERSISTENCE:SPEC:623; PERSISTENCE:JAVADOC:107; PERSISTENCE:JAVADOC:108;
   * PERSISTENCE:JAVADOC:109
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows: The perist
   * operation is cascaded to entities referenced by X, if the relationship from
   * X to these other entities is annotated with cascade=ALL annotation member.
   *
   * Invoke persist on a ManyToMany relationship from X annotated with
   * cascade=ALL and ensure the persist operation is cascaded.
   *
   */
  public void cascadeAllMXMTest1() throws Exception {

    TestUtil.logTrace("Begin cascadeAllMXMTest1");
    boolean pass = false;
    A aRef;
    Collection newCol;

    try {
      TestUtil.logTrace("New instances");
      final B b1 = new B("1", "b1", 1);
      final B b2 = new B("2", "b2", 1);
      final B b3 = new B("3", "b3", 1);
      final B b4 = new B("4", "b4", 1);
      Vector v1 = new Vector();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);
      aRef = new A("1", "bean1", 1, v1);
      createA(aRef);

      newCol = aRef.getBCol();

      dumpCollectionDataB(newCol);

      if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3)
          && newCol.contains(b4)) {
        pass = true;
      } else {
        TestUtil.logErr("Test failed");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Exception("cascadeAllMXMTest1 failed");
    }
  }

  /*
   * @testName: cascadeAllMXMTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:624
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows:
   *
   * If X is a removed entity, it becomes managed.
   *
   * Create an entity, persist it, remove it, and invoke persist again. Check
   * that it is managed and is accessible.
   *
   */
  public void cascadeAllMXMTest2() throws Exception {
    TestUtil.logTrace("Begin cascadeAllMXMTest2");
    boolean pass = false;
    A aRef;
    Collection newCol;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      final B b1 = new B("1", "b1", 2);
      final B b2 = new B("2", "b2", 2);
      final B b3 = new B("3", "b3", 2);
      final B b4 = new B("4", "b4", 2);
      Vector v1 = new Vector();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);
      aRef = new A("2", "bean2", 2, v1);
      getEntityManager().persist(aRef);

      newCol = aRef.getBCol();

      dumpCollectionDataB(newCol);

      if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3)
          && newCol.contains(b4)) {
        try {
          A newA = findA("2");
          TestUtil.logTrace("Remove newA ");
          getEntityManager().remove(newA);
          getEntityManager().flush();
          TestUtil.logTrace("Persist a removed entity ");
          getEntityManager().persist(newA);
          pass = getInstanceStatus(newA);
        } catch (Exception ee) {
          TestUtil.logErr(
              "Unexpected exception trying to persist a " + "removed entity",
              ee);
          pass = false;
        }
      } else {
        TestUtil.logTrace("Instance is not already persisted. Test Fails.");
        pass = false;
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Exception("cascadeAllMXMTest2 failed");
    }
  }

  /*
   * @testName: cascadeAllMXMTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:636
   * 
   * @test_Strategy: The flush method can be used for force synchronization. The
   * semantics of the flush operation applied to an entity X is as follows:
   *
   * If X is a managed entity, it is synchronized to the database.
   *
   */
  public void cascadeAllMXMTest3() throws Exception {
    boolean pass = false;
    A aRef;
    A a2;
    B b1;

    try {
      TestUtil.logTrace("New instances");
      b1 = new B("4", "b4", 4);
      Vector v1 = new Vector();
      v1.add(b1);
      aRef = new A("4", "bean4", 4, v1);
      createA(aRef);

      getEntityTransaction().begin();
      a2 = findA("4");

      if (getEntityManager().contains(a2)) {
        a2.setAName("newBean4");
        getEntityManager().flush();
        TestUtil.logTrace("getAName returns: " + a2.getAName());
        if (a2.getAName().equals("newBean4")) {
          pass = true;
        }
      } else {
        TestUtil.logErr("Entity not managed - test fails.");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Exception("cascadeAllMXMTest3 failed");
    }
  }

  /*
   * @testName: cascadeAllMXMTest4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:642
   * 
   * @test_Strategy: A managed entity instance becomes removed by invoking the
   * remove method on it or by cascading the remove operation. The semantics of
   * the remove operation, applied to an entity X are as follows:
   *
   * Test the remove semantics of a ManyToMany relationship when the related
   * entity has NOT been annotated with REMOVE.
   *
   */
  public void cascadeAllMXMTest4() throws Exception {

    TestUtil.logTrace("Begin cascadeAllMXMTest4");
    boolean pass = false;
    A a1;
    Collection newCol;
    try {
      final B b1 = new B("1", "b1", 5);
      final B b2 = new B("2", "b2", 5);
      final B b3 = new B("3", "b3", 5);
      final B b4 = new B("4", "b4", 5);
      Vector v1 = new Vector();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      getEntityTransaction().begin();
      TestUtil.logTrace("New A instance");
      a1 = new A("5", "bean5", 5, v1);
      getEntityManager().persist(a1);

      newCol = a1.getBCol();

      dumpCollectionDataB(newCol);

      if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3)
          && newCol.contains(b4)) {
        try {
          TestUtil.logTrace("Remove instances");
          getEntityManager().remove(findB("1"));
          getEntityManager().remove(findB("2"));
          getEntityManager().remove(findB("3"));
          getEntityManager().remove(findB("4"));
          getEntityManager().remove(a1);
          if ((!getEntityManager().contains(a1))) {
            pass = true;
          }

          getEntityTransaction().commit();
        } catch (Exception fe) {
          TestUtil.logErr(
              "Unexpected exception caught trying to remove entity instance :",
              fe);
        }
      } else {
        TestUtil.logErr("Test failed");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Exception("cascadeAllMXMTest4 failed");
    }
  }

  /*
   * @testName: cascadeAllMXMTest5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:668
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns true: If the entity has been
   * retrieved from the database and has not been removed or detached.
   *
   */
  public void cascadeAllMXMTest5() throws Exception {

    TestUtil.logTrace("Begin cascadeAllMXMTest5");
    boolean pass = false;
    A a1;
    A a2;
    Collection newCol;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New B instances");
      final B b1 = new B("1", "b1", 6);
      final B b2 = new B("2", "b2", 6);
      final B b3 = new B("3", "b3", 6);
      final B b4 = new B("4", "b4", 6);
      Vector v1 = new Vector();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      TestUtil.logTrace("New A instance");
      a1 = new A("6", "bean6", 6, v1);
      getEntityManager().persist(a1);
      getEntityManager().flush();

      a2 = findA("6");
      newCol = a2.getBCol();

      dumpCollectionDataB(newCol);

      if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3)
          && newCol.contains(b4)) {
        try {
          pass = getInstanceStatus(a2);
        } catch (Exception e) {
          TestUtil.logErr("Unexpected exception occurred", e);
        }
      }

      getEntityTransaction().commit();

    } catch (Exception fe) {
      TestUtil.logErr("Unexpected exception caught trying to remove "
          + "entity instance :" + fe);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Exception("cascadeAllMXMTest5 failed");
    }
  }

  /*
   * @testName: cascadeAllMXMTest6
   * 
   * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:669
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns true: If the entity instance is
   * new and the persist method has been called on the entity.
   *
   */
  public void cascadeAllMXMTest6() throws Exception {
    TestUtil.logTrace("Begin cascadeAllMXMTest6");
    boolean pass = false;
    A a1;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New B instances");
      final B b1 = new B("1", "b1", 7);
      final B b2 = new B("2", "b2", 7);
      final B b3 = new B("3", "b3", 7);
      final B b4 = new B("4", "b4", 7);
      Vector v1 = new Vector();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      TestUtil.logTrace("New A instance");
      a1 = new A("7", "bean7", 7, v1);
      getEntityManager().persist(a1);
      pass = getInstanceStatus(a1);

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Exception("cascadeAllMXMTest6 failed");
    }
  }

  /*
   * @testName: cascadeAllMXMTest7
   * 
   * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:670
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns true: If the entity instance is
   * new and the persist operation has been cascaded to it.
   *
   */
  public void cascadeAllMXMTest7() throws Exception {

    TestUtil.logTrace("Begin cascadeAllMXMTest7");
    boolean pass = false;
    A a1;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New A instance");
      final B b1 = new B("1", "b1", 8);
      final B b2 = new B("2", "b2", 8);
      final B b3 = new B("3", "b3", 8);
      final B b4 = new B("4", "b4", 8);
      Vector v1 = new Vector();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      TestUtil.logTrace("New B instances");
      a1 = new A("8", "bean8", 8, v1);
      getEntityManager().persist(a1);

      pass = (getEntityManager().contains(b1) && getEntityManager().contains(b2)
          && getEntityManager().contains(b3)
          && getEntityManager().contains(b4));
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Exception("cascadeAllMXMTest7 failed");
    }
  }

  /*
   * @testName: cascadeAllMXMTest8
   * 
   * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:675
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns false: If the entity instance is
   * new and the persist operation has not been called on it.
   *
   */
  public void cascadeAllMXMTest8() throws Exception {

    TestUtil.logTrace("Begin cascadeAllMXMTest8");
    boolean pass = true;
    A a1;
    try {
      getEntityTransaction().begin();
      final B b1 = new B("1", "b1", 9);
      final B b2 = new B("2", "b2", 9);
      final B b3 = new B("3", "b3", 9);
      final B b4 = new B("4", "b4", 9);
      Vector v1 = new Vector();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      TestUtil.logTrace("New A instance");
      a1 = new A("9", "bean9", 9, v1);

      pass = getInstanceStatus(a1);
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (pass) {
      throw new Exception("cascadeAllMXMTest8 failed");
    }
  }

  /*
   * @testName: cascadeAllMXMTest9
   * 
   * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:676
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns false: If the entity instance is
   * new and the persist operation has not been cascaded to it.
   *
   */
  public void cascadeAllMXMTest9() throws Exception {

    TestUtil.logTrace("Begin cascadeAllMXMTest9");
    boolean pass1 = true;
    boolean pass2 = true;
    boolean pass = true;
    try {
      final B b1 = new B("1", "b1", 10);
      final B b2 = new B("2", "b2", 10);
      final B b3 = new B("3", "b3", 10);
      final B b4 = new B("4", "b4", 10);
      Vector v1 = new Vector();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      getEntityTransaction().begin();
      TestUtil.logTrace("New A instance");
      A a1 = new A("10", "bean10", 10, v1);

      pass1 = (!getEntityManager().contains(b1)
          && !getEntityManager().contains(b2)
          && !getEntityManager().contains(b3)
          && !getEntityManager().contains(b4));

      pass2 = getEntityManager().contains(a1);

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass1 && !pass2 && !pass) {
      TestUtil.logErr("pass=" + pass + ", pass1=" + pass1 + ", pass2=" + pass2);
      throw new Exception("cascadeAllMXMTest9 failed");
    }
  }

  /*
   * @testName: cascadeAllMXMTest10
   * 
   * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:622
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows: The perist
   * operation is cascaded to entities referenced by X, if the relationship from
   * X to these other entities is annotated with cascade=ALL annotation member.
   *
   * If X is a pre-existing managed entity, it is ignored by the persist
   * operation. However, the persist operation is cascaded to entities
   * referenced by X, if the relationships from X to these other entities is
   * annotated with cascade=ALL annotation member value.
   *
   */
  public void cascadeAllMXMTest10() throws Exception {
    boolean pass = false;
    A a1;
    B bRef;
    B bRef1 = null;

    try {
      getEntityTransaction().begin();
      a1 = new A("11", "a11", 11);
      bRef = new B("11", "bean11", 11);
      getEntityManager().persist(bRef);

      if (getEntityManager().contains(bRef)) {
        bRef1 = findB("11");
        Vector v1 = new Vector();
        v1.add(a1);
        bRef1.setACol(v1);
        getEntityManager().persist(bRef1);
        getEntityManager().flush();
        pass = getEntityManager().contains(a1);
        TestUtil.logTrace("try to find A");
        A a2 = findA("11");
        if (null != a2) {
          TestUtil.logTrace("A2 is not null");
        }
      }

      Vector nullCol = new Vector();
      if (bRef1 != null) {
        bRef1.setACol(nullCol);
        getEntityManager().merge(bRef);
        a1.setBCol(nullCol);
        getEntityManager().merge(a1);

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("bRef1 was null");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass) {
      throw new Exception("cascadeAllMXMTest10 failed");
    }
  }

  /*
   * @testName: cascadeAllMXMTest11
   * 
   * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:644
   * 
   * @test_Strategy: The flush method can be used for force synchronization. The
   * semantics of the flush operation applied to an entity X is as follows:
   *
   * For all entities Y referenced by a relationship from X, if the relationship
   * to Y has been annotated with the cascade member value cascade=ALL the
   * persist operation is applied to Y.
   *
   */
  public void cascadeAllMXMTest11() throws Exception {
    boolean pass = false;
    A a1;

    try {
      TestUtil.logTrace("New instances");
      getEntityTransaction().begin();
      final B b1 = new B("1", "b1", 12);
      final B b2 = new B("2", "b2", 12);
      final B b3 = new B("3", "b3", 12);
      final B b4 = new B("4", "b4", 12);
      Vector v1 = new Vector();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      TestUtil.logTrace("New A instance");
      a1 = new A("12", "bean12", 12, v1);

      getEntityManager().persist(a1);
      getEntityManager().flush();
      pass = getEntityManager().contains(a1);

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Exception("cascadeAllMXMTest11 failed");
    }
  }

  /*
   *
   * Business Methods to set up data for Test Cases
   */
  private void createA(final A a) {
    TestUtil.logTrace("Entered createA method");
    getEntityTransaction().begin();
    getEntityManager().persist(a);
    getEntityManager().flush();
    getEntityTransaction().commit();
  }

  private A findA(String id) {
    TestUtil.logTrace("Entered findA method");
    return getEntityManager().find(A.class, id);
  }

  private B findB(final String id) {
    TestUtil.logTrace("Entered findB method");
    return getEntityManager().find(B.class, id);
  }

  private boolean getInstanceStatus(Object o) {
    TestUtil.logTrace("Entered getInstanceStatus method");
    return getEntityManager().contains(o);
  }

  private void dumpCollectionDataB(Collection c) {
    TestUtil.logTrace("collection Data");
    TestUtil.logTrace("---------------");
    TestUtil.logTrace("- size=" + c.size());
    Iterator i = c.iterator();
    int elem = 1;
    while (i.hasNext()) {
      B v = (B) i.next();
      TestUtil.logTrace("- Element #" + elem++);
      TestUtil.logTrace("  id=" + v.getBId() + ", name=" + v.getBName()
          + ", value=" + v.getBValue());
    }
  }

  public void cleanup() throws Exception {
    TestUtil.logTrace("Cleanup data");
    removeTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM FKEYS_MXM_BI_BTOB")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM AEJB_MXM_BI_BTOB")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM BEJB_MXM_BI_BTOB")
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
