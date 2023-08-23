/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.metamodelapi.setattribute;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.Type;

public class Client extends PMClientBase {

  public Client() {
  }


  public void setup() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

  /*
   * @testName: getSet
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1271;
   *
   * @test_Strategy:
   *
   */
  public void getSet() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<A> mType = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.setattribute.A.class);
      if (mType != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SetAttribute<? super A, Address> setAttrib = mType.getSet("address",
            com.sun.ts.tests.jpa.core.metamodelapi.setattribute.Address.class);
        Type t = setAttrib.getElementType();
        if (t != null) {
          TestUtil.logTrace("element Java Type  = " + t.getJavaType());
          if (t.getJavaType().getName().equals(
              "com.sun.ts.tests.jpa.core.metamodelapi.setattribute.Address")) {
            pass = true;
          }
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getSet Test  failed");
    }
  }

  /*
   * @testName: getCollectionType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1455;
   *
   * @test_Strategy:
   *
   */
  public void getCollectionType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<A> mType = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.setattribute.A.class);
      if (mType != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SetAttribute<? super A, Address> setAttrib = mType.getSet("address",
            com.sun.ts.tests.jpa.core.metamodelapi.setattribute.Address.class);

        SetAttribute.CollectionType setAttribColType = setAttrib
            .getCollectionType();
        TestUtil.logTrace("collection Type = " + setAttrib.getCollectionType());
        if (setAttribColType == SetAttribute.CollectionType.SET) {
          TestUtil.logTrace("Received expected result = " + setAttribColType);
          pass = true;
        } else {
          TestUtil.logErr("Received unexpected result = " + setAttribColType);
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getCollectionType Test  failed");
    }
  }

  /*
   * @testName: getElementType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1456;
   *
   * @test_Strategy:
   *
   */
  public void getElementType() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<A> mType = metaModel.managedType(
          com.sun.ts.tests.jpa.core.metamodelapi.setattribute.A.class);
      if (mType != null) {
        TestUtil.logTrace("Obtained Non-null ManagedType");
        SetAttribute<? super A, Address> setAttrib = mType.getSet("address",
            com.sun.ts.tests.jpa.core.metamodelapi.setattribute.Address.class);

        TestUtil.logTrace("collection Element Type = "
            + setAttrib.getElementType().getJavaType().getName());
        String elementTypeName = setAttrib.getElementType().getJavaType()
            .getName();
        if (elementTypeName.equals(
            "com.sun.ts.tests.jpa.core.metamodelapi.setattribute.Address")) {
          TestUtil.logTrace("Received expected result = " + elementTypeName);
          pass = true;
        } else {
          TestUtil.logErr("Received unexpected result = " + elementTypeName);
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getElementType Test  failed");
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
  }
}
