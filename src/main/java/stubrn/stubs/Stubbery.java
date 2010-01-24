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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 *
 */
public class Stubbery {

    private final ClassLoader classLoader;

    public Stubbery(ClassLoader classLoader){
        this.classLoader = classLoader;
    }

    public Stubbery(){
        this(Stubbery.class.getClassLoader());
    }

    private <T> InvocationHandler createInvocationHandler(Object holder,Class<T> forClass){

        Collection<MethodMatcher> matchers = createMatchers(holder);

        MethodCallbackDispatcher<T> dispatcher =
                new MethodCallbackDispatcher<T>(
                        forClass, matchers);
        return new stubrn.stubs.InvocationHandler(dispatcher);
    }

    @SuppressWarnings("unchecked")
    private Collection<MethodMatcher> createMatchers(Object holder){
        if(holder instanceof Map){
            return createMatchersForMaps((Map<String,Object>) holder);
        }
        else {
            return createMatchersForObjects(holder);
        }
    }

    private Collection<MethodMatcher> createMatchersForObjects(Object holder) {
        return asList(
                new AnnotatedFieldMatcher(holder),
                new ExistingMethodMatcher(holder)
        );
    }

    private Collection<MethodMatcher> createMatchersForMaps(Map<String,Object> holder){
        return Arrays.<MethodMatcher>asList(new MapMatcher(holder));
    }

    /**
     *
     * @param forClass
     * @param o
     * @param forClass
     * @return
     *
     * @throws InvalidReturnTypeException TODO
     */
    @SuppressWarnings("unchecked")
    public <T> T stubFor(Class<T> forClass, Object o){

        return (T) Proxy.newProxyInstance(
                classLoader,
                new Class[] {forClass},createInvocationHandler(o,forClass));
    }

    public <T> T stubFor(Class<T> forClass,String methodName,Object returnValue){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(methodName,returnValue);
        return stubFor(forClass,map);
    }
}
