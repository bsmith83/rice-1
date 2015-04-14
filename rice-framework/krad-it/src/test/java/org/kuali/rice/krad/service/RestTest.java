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

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.krad.test.KRADTestCase;
import org.kuali.rice.test.SQLDataLoader;
import org.kuali.rice.test.runners.BootstrapTest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@BootstrapTest(RestTest.BootstrapTest.class)
public abstract class RestTest extends KRADTestCase {

    private static final String SQL_FILE = "classpath:org/kuali/rice/krad/test/RestSuiteTestData.sql";
    private static final String XML_FILE = "classpath:org/kuali/rice/krad/test/DefaultSuiteTestData.xml";
    private static final String KRAD_MODULE_NAME = "REST";

    public static String getAppUrl()  {
        return "http://localhost:8080/kr-dev";
    }

    public static String getRestUrl()  {
        return getAppUrl() + "/rest";
    }

    public String getToken() {
        /*        String clientId = ((ConfigurationService) GlobalResourceLoader.getService("kualiConfigurationService")).getPropertyValueAsString(ClientDetailsServiceConfig.CONFIG_CLIENT_ID);
                String secret = ((ConfigurationService) GlobalResourceLoader.getService("kualiConfigurationService")).getPropertyValueAsString(ClientDetailsServiceConfig.CONFIG_CLIENT_SECRET);*/

        String clientId = "rice";
        String secret = "secret123";
        String urlParameters =
                "grant_type=client_credentials&client_secret=" + secret + "&client_id=" + clientId + "&scope=access";
        String location = getRestUrl() + "/oauth/token";

        String response = request("POST", location, urlParameters, null, "application/x-www-form-urlencoded");
        Assert.assertNotNull(response);
        Assert.assertTrue(response.startsWith("{"));
        Assert.assertTrue(response.contains("access_token"));
        Assert.assertTrue(response.contains("token_type"));
        String token = null;

        try {
            JSONObject jsonObject = new JSONObject(response);
            token = jsonObject.getString("access_token");
        }
        catch (Exception e) {
            Assert.fail("There was a problem parsing the json: " + e.getMessage());
        }
        return token;
    }

    public String getWithToken(String location, String urlParameters, String token) {
        return request("GET", location, urlParameters, token, null);
    }

    public String postWithToken(String location, String urlParameters, String token) {
        return request("POST", location, urlParameters, token, "application/json");
    }

    public String putWithToken(String location, String urlParameters, String token) {
        return request("PUT", location, urlParameters, token, "application/json");
    }

    public String request(String requestMethod, String location, String queryContent, String token, String contentType) {
        String responseJSON = null;
        URL url;
        HttpURLConnection connection = null;

        try {

            //Create connection


            if (requestMethod.equalsIgnoreCase("post") || requestMethod.equalsIgnoreCase("put")) {
                url = new URL(location);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(requestMethod.toUpperCase());
                connection.setRequestProperty("Content-Type", contentType);

                if (StringUtils.isNotBlank(token)) {
                    connection.setRequestProperty("Authorization", "bearer " + token);
                }

                connection.setRequestProperty("Content-Length", "" + Integer.toString(queryContent.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches(false);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(queryContent);
                wr.flush();
                wr.close();
            }
            else if (requestMethod.equalsIgnoreCase("get")){
                url = new URL(location + "?" + queryContent);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(requestMethod.toUpperCase());
                connection.setRequestProperty("Accept-Language", "en-US");
                connection.setRequestProperty("Accept", "*/*");

                if (StringUtils.isNotBlank(token)) {
                    connection.setRequestProperty("Authorization", "bearer " + token);
                }
            }

            //Get Response
            if (connection.getResponseCode() != 200) {
                return "" + connection.getResponseCode();
            }
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            responseJSON = response.toString();
        } catch (Exception e) {
            Assert.fail("There was a problem with the url request: " + e.getMessage());
        }

        return responseJSON;
    }

    @Override
    protected void loadSuiteTestData() throws Exception {
        super.loadSuiteTestData();
        new SQLDataLoader(SQL_FILE, ";").runSql();
    }

    public static final class BootstrapTest extends RestTest {
        @Test
        public void bootstrapTest() {};
    }
}
