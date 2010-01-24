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
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;

/**
 *
 */
public class MethodCallbackDispatcherTest {

    interface I {
        String aMethod();
        int anotherMethod();
        void notStubbed();
    }

    @Test
    public void testDetermineCallback() throws Exception {

        final Callback defaultCallback = new ReturnValueCallback("Foo",String.class);
        final Callback aCallback = new ReturnValueCallback("Bar",String.class);
        final Callback anotherCallback = new ReturnValueCallback(42,Integer.class);

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

        MethodCallbackDispatcher<I> dispatcher =
                new MethodCallbackDispatcher<I>(
                        I.class,
                        defaultCallback,
                        new ThrowingProblemPolicy(),
                        Lists.<MethodMatcher>newArrayList(matcherStub));

        Method[] methods = I.class.getMethods();
        
        assertEquals(dispatcher.determineCallback(methods[0]), aCallback);
        assertEquals(dispatcher.determineCallback(methods[1]), anotherCallback);
        assertEquals(dispatcher.determineCallback(methods[2]), defaultCallback);
    }
}
