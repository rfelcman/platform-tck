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

package com.sun.ts.tests.jaxws.wsi.j2w.rpc.literal.R2022;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;
import com.sun.ts.tests.jaxws.sharedclients.rpclitclient.J2WRLSharedClient;
import com.sun.ts.tests.jaxws.wsi.constants.DescriptionConstants;
import com.sun.ts.tests.jaxws.wsi.utils.DescriptionUtils;

public class Client extends ServiceEETest implements DescriptionConstants {
  /**
   * The client.
   */
  private SOAPClient client;

  static J2WRLShared service = null;

  /**
   * Test entry point.
   * 
   * @param args
   *          the command-line arguments.
   */
  public static void main(String[] args) {
    Client test = new Client();
    Status status = test.run(args, System.out, System.err);
    status.exit();
  }

  /**
   * @class.testArgs: -ap jaxws-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws Fault
   */
  public void setup(String[] args, Properties properties) throws Fault {
    client = ClientFactory.getClient(J2WRLSharedClient.class, properties, this,
        service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testImportPlacement
   *
   * @assertion_ids: WSI:SPEC:R2022
   *
   * @test_Strategy: Retrieve the WSDL, generated by the Java-to-WSDL tool, and
   *                 examine all wsdl:import elements, ensuring that they appear
   *                 where they need to be.
   * 
   * @throws Fault
   */
  public void testImportPlacement() throws Fault {
    int imports = -1;
    int documentations = -1;
    int other = -1;
    Document document = client.getDocument();
    Element[] children = DescriptionUtils.getChildElements(
        document.getDocumentElement(), WSDL_NAMESPACE_URI, null);
    for (int i = 0; i < children.length; i++) {
      String localName = children[i].getLocalName();
      if (localName.equals(WSDL_IMPORT_LOCAL_NAME)) {
        if (other != -1) {
          throw new Fault(
              "wsdl:import element encountered that follows element other that wsdl:documentation (BP-R2022)");
        }
        if (documentations > imports) {
          throw new Fault(
              "wsdl:documentation element encountered that is embedded between wsdl:import elements (BP-R2022)");
        }
        imports = i;
      } else if (localName.equals(WSDL_DOCUMENTATION_LOCAL_NAME)) {
        documentations = i;
      } else {
        other = i;
      }
    }
  }
}
