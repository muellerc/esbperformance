/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.cmueller.camel.esbperf.smxosgi.xslt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Ignore
public class PerformanceTest extends CamelSpringTestSupport {
    
    private static final String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
            + "<soapenv:Header><routing xmlns=\"http://someuri\">xadmin;server1;community#1.0##</routing></soapenv:Header>"
            + "<soapenv:Body>"
            + "<m:buyStocks xmlns:m=\"http://services.samples/xsd\">"
            + "<order><symbol>IBM</symbol><buyerID>asankha</buyerID><price>140.34</price><volume>2000</volume></order>"
            + "<order><symbol>MSFT</symbol><buyerID>ruwan</buyerID><price>23.56</price><volume>8030</volume></order>"
            + "<order><symbol>SUN</symbol><buyerID>indika</buyerID><price>14.56</price><volume>500</volume></order>"
            + "<order><symbol>GOOG</symbol><buyerID>chathura</buyerID><price>60.24</price><volume>40000</volume></order>"
            + "<order><symbol>IBM</symbol><buyerID>asankha</buyerID><price>140.34</price><volume>2000</volume></order>"
            + "<order><symbol>MSFT</symbol><buyerID>ruwan</buyerID><price>23.56</price><volume>803000</volume></order>"
            + "<order><symbol>SUN</symbol><buyerID>indika</buyerID><price>14.56</price><volume>5000</volume></order>"
            + "</m:buyStocks>"
            + "</soapenv:Body>"
            + "</soapenv:Envelope>";
    
    @Test
    public void test() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        
        final AtomicLong counter = new AtomicLong();
        long start = System.currentTimeMillis();
        
        List<Future<?>> futures = new ArrayList<Future<?>>();
        for (int i = 0; i < 20; i++) {
            futures.add(executorService.submit(new Tester(counter)));
        }
        
        for (Future<?> future : futures) {
            future.get();
        }
        
        System.out.println(System.currentTimeMillis() - start);
        executorService.shutdown();
        
        assertEquals(4000, counter.get());
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/bundle-context.xml");
    }
    
    static class Tester implements Runnable {
        
        private AtomicLong counter;
        
        public Tester(AtomicLong counter) {
            this.counter = counter;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 200; i++) {
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    
                    HttpPost post = new HttpPost("http://localhost:8192/service/XSLTProxy");
                    post.addHeader("Accept" , "text/xml");
                    post.setEntity(new StringEntity(body, ContentType.create("text/xml", "ISO-8859-1")));
                    
                    HttpResponse response = httpclient.execute(post);
                        
                    assertEquals(200, response.getStatusLine().getStatusCode());
                    counter.incrementAndGet();
                    response.getEntity().getContent();
                    
                    httpclient.getConnectionManager().shutdown();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}