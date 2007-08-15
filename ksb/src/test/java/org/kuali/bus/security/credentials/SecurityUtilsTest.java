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
package org.kuali.bus.security.credentials;

import junit.framework.TestCase;

import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.x509.X509AuthenticationToken;
import org.kuali.bus.security.credentials.X509CredentialsSourceTest.KualiX509Certificate;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.2.2.1 $ $Date: 2007-08-15 16:04:59 $
 * @since 0.9
 *
 */
public class SecurityUtilsTest extends TestCase {
	
	public void testUsernamePasswordCredentials() {
		final UsernamePasswordCredentials c = new UsernamePasswordCredentials("test", "test");
		final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityUtils.convertCredentialsToSecurityContext(c);
		
		assertEquals(token.getPrincipal(), c.getUsername());
		assertEquals(token.getCredentials(), c.getPassword());
	}
	
	public void testX509CertificateCredentials() {
		final X509Credentials c = new X509Credentials(new KualiX509Certificate());
		final X509AuthenticationToken token = (X509AuthenticationToken) SecurityUtils.convertCredentialsToSecurityContext(c);
		
		assertEquals(c.getX509Certificate(), token.getCredentials());
	}

}
