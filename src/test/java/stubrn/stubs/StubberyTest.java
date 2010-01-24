/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package stubrn.stubs;

import com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;
import stubrn.stubs.annotations.ByName;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 *
 */
public class StubberyTest {

    interface I {
        String getString();

        int getIntValue();
    }

    @Test
    public void testStubCreation() {

        Stubbery stubbery = new Stubbery();

        I i = stubbery.stubFor(I.class, new Object() {
            @ByName String getString = "foo";
        });

        assertNotNull(i);
        assertEquals(i.getString(), "foo");
    }

    @Test
    public void testComplexStub() {
        Stubbery stubbery = new Stubbery();
        I i = stubbery.stubFor(I.class, new Object() {
            @ByName
            String getString = "foo";

            public int getIntValue() {
                return 42;
            }
        });
        assertNotNull(i);
        assertEquals(i.getString(), "foo");
        assertEquals(i.getIntValue(), 42);
    }

    interface CorrectOrder {
        int method();
        int method(int a);
        int method(String a);
        int method(double a);
    }

    @Test
    public void testPrecedencePickByNameFieldsFirst(){

        Stubbery stubbery = new Stubbery();

        Object holder = new Object(){
            @ByName int method = 1;
            int method() {
                return 2;
            }
        };

        CorrectOrder stub = stubbery.stubFor(CorrectOrder.class,holder);
        assertEquals(1,stub.method());
        assertEquals(1,stub.method(42));
        assertEquals(1,stub.method("foo"));
        assertEquals(1,stub.method(23.42));
    }

    @Test
    public void testMapHolder(){
        Stubbery stubbery = new Stubbery();

        Map<String,Object> map = ImmutableMap.<String,Object>builder()
                .put("getString","foo")
                .put("getIntValue",42)
                .build();

        I i = stubbery.stubFor(I.class,map);
        
        assertEquals(i.getString(),"foo");
        assertEquals(i.getIntValue(),42);
    }

    @Test
    public void testStubForWithNameObject(){
        Stubbery stubbery = new Stubbery();
        I i = stubbery.stubFor(I.class,"getString","foo");
        assertEquals(i.getString(),"foo");
    }
}
