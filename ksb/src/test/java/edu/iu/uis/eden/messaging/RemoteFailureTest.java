/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.iu.uis.eden.messaging;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.bus.test.KSBTestCase;
import org.kuali.rice.resourceloader.GlobalResourceLoader;

import edu.iu.uis.eden.messaging.remotedservices.EchoService;

public class RemoteFailureTest extends KSBTestCase {

	public boolean startClient1() {
		return true;
	}

	@Test public void testEchoService() throws Exception {
		EchoService echoService = (EchoService)GlobalResourceLoader.getService(new QName("TestCl1", "echoService"));
		assertNotNull(echoService);
		String echoValue = "echoValue";
		String result = echoService.echo(echoValue);
		assertEquals(echoValue, result);

		// now shut down the test client and try to access the echo service
		getTestClient1().stop();
		try {
			result = echoService.echo(echoValue);
			fail("Exception should have been thrown");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// start it back up and make sure we can use our service again
		getTestClient1().start();
		result = echoService.echo(echoValue);
		assertEquals(echoValue, result);
	}
	
	@Test public void testSOAPEchoService() throws Exception {
		EchoService echoService = (EchoService)GlobalResourceLoader.getService(new QName("TestCl1", "soap-echoService"));
		assertNotNull(echoService);
		String echoValue = "echoValue";
		String result = echoService.echo(echoValue);
		assertEquals(echoValue, result);
		// now shut down the test client and try to access the echo service
		getTestClient1().stop();
		try {
			result = echoService.echo(echoValue);
			fail("Exception should have been thrown");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// start it back up and make sure we can use our service again
		getTestClient1().start();
		result = echoService.echo(echoValue);
		assertEquals(echoValue, result);
	}
	
}
