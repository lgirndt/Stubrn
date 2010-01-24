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

import com.google.common.collect.Lists;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 *
 */
public class MethodCallbackDispatcherTest {
    private Callback aCallback;
    private Callback anotherCallback;
    private MethodCallbackDispatcher<I> dispatcher;

    interface I {
        String aMethod();
        int anotherMethod();
        void notStubbed();
    }

    @BeforeTest
    private void setupTest() {
        aCallback = new ReturnValueCallback("Bar",String.class);
        anotherCallback = new ReturnValueCallback(42,Integer.class);

        // we should eat our own dogfood!
        MethodMatcher matcherStub =
                new MethodMatcher(){
                    @Override
                    public Callback matchMethod(Method method) {
                        String name = method.getName();
                        if(name.equals("aMethod")){
                            return aCallback;
                        }
                        else if(name.equals("anotherMethod")){
                            return anotherCallback;
                        }
                        else {
                            return null;
                        }
                    }
                };

        this.dispatcher =
                new MethodCallbackDispatcher<I>(
                        I.class,
                        Lists.<MethodMatcher>newArrayList(matcherStub));
    }


    @Test
    public void testDetermineCallback() throws Exception {
        Method[] methods = I.class.getMethods();
        
        assertEquals(dispatcher.determineCallback(methods[0]), aCallback);
        assertEquals(dispatcher.determineCallback(methods[1]), anotherCallback);
    }

    @Test
    public void testNotStubbedMethod(){
        assertNull(dispatcher.determineCallback(I.class.getMethods()[2]));
    }

}
