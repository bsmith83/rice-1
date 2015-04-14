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

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.rest.RiceRestConstants;

public class KimGroupRestTest extends RestTest {

    private static final String REST_URL = getRestUrl() + RiceRestConstants.API_URL + "/kimgroups";

    @Test
    public void testUnauthorized() {
        String response = getWithToken(REST_URL, "", null);
        Assert.assertEquals(response, "401");

        response = getWithToken(REST_URL, "", "bad_token");
        Assert.assertEquals(response, "401");
    }

    @Test
    public void testGetGroupsForPrincipal() {
        String response = getWithToken(REST_URL, "principalId=admin", getToken());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.startsWith("["));
        Assert.assertTrue(!response.contains("error:"));

        try {
            JSONArray groups = new JSONArray(response);
            Assert.assertTrue(groups.length() > 0);
        }
        catch (Exception e) {
            Assert.fail("There was a problem parsing the json response: " + e.getMessage());
        }

    }

    @Test
    public void testGetGroupById() {
        String response = getWithToken(REST_URL + "/1", "", getToken());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.startsWith("{"));
        Assert.assertTrue(!response.contains("error:"));

        try {
            JSONObject group = new JSONObject(response);
            Assert.assertNotNull(group);
            Assert.assertEquals(group.getString("id"), "1");
        }
        catch (Exception e) {
            Assert.fail("There was a problem parsing the json response: " + e.getMessage());
        }

    }

    @Test
    public void testCreateUpdateGroup() {
        try {
            JSONObject newGroup = new JSONObject();
            newGroup.put("namespaceCode", "TEST-CODE");
            newGroup.put("name", "TEST-NAME");
            newGroup.put("description","TEST-DESC");
            newGroup.put("active", true);
            newGroup.put("kimTypeId", "1");

            String response = postWithToken(REST_URL, newGroup.toString(), getToken());

            Assert.assertNotNull(response);
            Assert.assertTrue(response.startsWith("{"));
            Assert.assertTrue(!response.contains("error:"));

            JSONObject group = new JSONObject(response);
            Assert.assertNotNull(group);
            Assert.assertEquals(group.getString("name"), "TEST-NAME");
        }
        catch (Exception e) {
            Assert.fail("There was a problem parsing the json response: " + e.getMessage());
        }

    }

}
