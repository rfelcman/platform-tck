/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.jsptagexception;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jsp_jsptagexc_web");
    setTestJsp("JspTagExceptionTest");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: jspTagExceptionDefaultCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:116
   * 
   * @test_Strategy: Validate default constructor of JspTagException
   */
  public void jspTagExceptionDefaultCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspTagExceptionDefaultCtorTest");
    invoke();
  }

  /*
   * @testName: jspTagExceptionMessageCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:115
   * 
   * @test_Strategy: Validate contructor taking single string argument as the
   * message of the Exception.
   */
  public void jspTagExceptionMessageCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspTagExceptionMessageCtorTest");
    invoke();
  }

  /*
   * @testName: jspTagExceptionCauseCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:118
   * 
   * @test_Strategy: Validate constructor taking a Throwable signifying the root
   * cause of the this JspTagException.
   */
  public void jspTagExceptionCauseCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspTagExceptionCauseCtorTest");
    invoke();
  }

  /*
   * @testName: jspTagExceptionCauseMessageCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:117
   * 
   * @test_Strategy: Validate constructor taking both a message and a Throwable
   * signifying the root cause of the JspTagException.
   */
  public void jspTagExceptionCauseMessageCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspTagExceptionCauseMessageCtorTest");
    invoke();
  }
}
