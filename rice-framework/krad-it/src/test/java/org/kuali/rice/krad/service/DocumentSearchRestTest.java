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

public class DocumentSearchRestTest extends RestTest {

    private static final String REST_URL = getRestUrl() + RiceRestConstants.API_URL + "/documents";

    @Test
    public void testUnauthorized() {
        String response = getWithToken(REST_URL, "", null);
        Assert.assertEquals(response, "401");

        response = getWithToken(REST_URL, "", "bad_token");
        Assert.assertEquals(response, "401");
    }

    @Test
    public void testDocumentSearch() {
        String response = getWithToken(REST_URL, "", getToken());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.contains("content"));

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray content = jsonObject.getJSONArray("content");
            Assert.assertNotNull(content);
            Assert.assertTrue(content.length() > 0);
            JSONObject document = content.getJSONObject(0);
            Assert.assertNotNull(document);
            Assert.assertNotNull(document.getString("documentId"));
        }
        catch (Exception e) {
            Assert.fail("There was a problem parsing the json response: " + e.getMessage());
        }
    }

    @Test
    public void testFilteredDocumentSearch() {
        String response = getWithToken(REST_URL, "filter=title::New*", getToken());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.contains("content"));

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray content = jsonObject.getJSONArray("content");
            Assert.assertNotNull(content);
            Assert.assertTrue(content.length() == 11);
            JSONObject document = content.getJSONObject(0);
            Assert.assertNotNull(document);
            Assert.assertTrue(document.getString("title").startsWith("New"));

        }
        catch (Exception e) {
            Assert.fail("There was a problem parsing the json response: " + e.getMessage());
        }
    }

    @Test
    public void testUnmatchedFilteredDocumentSearch() {
        String response = getWithToken(REST_URL, "filter=title::BAD_TITLE", getToken());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.contains("content"));

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray content = jsonObject.getJSONArray("content");
            Assert.assertNotNull(content);
            Assert.assertTrue(content.length() == 0);

        }
        catch (Exception e) {
            Assert.fail("There was a problem parsing the json response: " + e.getMessage());
        }
    }

    @Test
    public void testLimitedAndPagedDocumentSearch() {
        String response = getWithToken(REST_URL, "limit=5&startIndex=0", getToken());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.contains("content"));

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray content = jsonObject.getJSONArray("content");
            Assert.assertNotNull(content);
            Assert.assertTrue(content.length() == 5);
            JSONObject document = content.getJSONObject(0);
            String firstPageDocId =  document.getString("documentId");

            response = getWithToken(REST_URL, "limit=5&startIndex=5", getToken());
            jsonObject = new JSONObject(response);
            content = jsonObject.getJSONArray("content");
            Assert.assertNotNull(content);
            Assert.assertTrue(content.length() == 5);
            document = content.getJSONObject(0);
            Assert.assertNotEquals(firstPageDocId, document.getString("documentId"));


        }
        catch (Exception e) {
            Assert.fail("There was a problem parsing the json response: " + e.getMessage());
        }
    }

    @Test
    public void testGetDocument() {
        String docId = "3046";
        String response = getWithToken(REST_URL + "/" + docId, "", getToken());
        Assert.assertNotNull(response);

        try {
            JSONObject document = new JSONObject(response);
            Assert.assertNotNull(document);
            String fetchedDocId = document.getString("documentId");
            Assert.assertEquals(docId, fetchedDocId);
        }
        catch (Exception e) {
            Assert.fail("There was a problem parsing the json response: " + e.getMessage());
        }
    }
}
