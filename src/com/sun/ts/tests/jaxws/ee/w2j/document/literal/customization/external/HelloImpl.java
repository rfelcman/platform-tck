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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.external;

import com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.external.custom.pkg.CustomizationExternalTestException;
import com.sun.ts.tests.samples.ejb.ee.simpleHello.Hello;

import jakarta.jws.WebService;

@WebService(portName = "HelloPort", serviceName = "myService", targetNamespace = "http://customizationexternaltest.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JDLCustomizationExternalTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.external.Hello")

public class HelloImpl implements Hello {
  public void myHello(jakarta.xml.ws.Holder<HelloElement> helloArgument)
      throws CustomizationExternalTestException {
    System.out.println("in CustomizationExternalTestService:HelloImpl:myHello");
    if (helloArgument.value.getArgument().equals("Exception Case")) {
      HelloFaultMessage hfm = new HelloFaultMessage();
      hfm.setFault1("foo");
      hfm.setFault2("bar");
      throw new CustomizationExternalTestException(
          "This is the CustomizationExternalTestException fault", hfm);
    } else
      helloArgument.value
          .setArgument(helloArgument.value.getArgument() + ", World!");
  }

}
