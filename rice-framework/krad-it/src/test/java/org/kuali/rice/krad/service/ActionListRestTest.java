/*
 * Copyright 2006-2015 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kuali.rice.krad.service;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.rest.RiceRestConstants;

public class ActionListRestTest extends RestTest{

    private static final String REST_URL = getRestUrl() + RiceRestConstants.API_URL + "/actionlist";

    @Test
    public void testUnauthorized() {
        String response = getWithToken(REST_URL, "", null);
        Assert.assertEquals(response, "401");

        response = getWithToken(REST_URL, "", "bad_token");
        Assert.assertEquals(response, "401");
    }

    @Test
    public void testGetActionList() {
        String response = getWithToken(REST_URL + "/admin", "", getToken());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.startsWith("{"));
        Assert.assertTrue(!response.contains("error:"));
    }

}
