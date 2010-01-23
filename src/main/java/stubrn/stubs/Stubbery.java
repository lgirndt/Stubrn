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
import stubrn.stubs.handling.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;

/**
 *
 */
public class Stubbery {

    private final ClassLoader classLoader;

    private Callback defaultCallback;

    private ProblemPolicy problemPolicy;

    public Stubbery(ClassLoader classLoader){
        this.classLoader = classLoader;
        this.problemPolicy = new ThrowingProblemPolicy();
        this.defaultCallback = new ByPolicyCallback(problemPolicy);
    }

    public Stubbery(){
        this(Stubbery.class.getClassLoader());
    }

    private <T> InvocationHandler createInvocationHandler(Object holder,Class<T> forClass){

        Collection<MethodMatcher> matchers = Lists.<MethodMatcher>newArrayList(
                new AnnotatedFieldMatcher(holder),
                new ExistingMethodMatcher(holder)
        );

        MethodCallbackDispatcher dispatcher =
                new MethodCallbackDispatcher(
                        forClass,holder,defaultCallback,problemPolicy,matchers);
        return new stubrn.stubs.handling.InvocationHandler(dispatcher);
    }

    public <T> T stubFor(Class<T> forClass, Object o){

        T proxy = (T) Proxy.newProxyInstance(
                classLoader,
                new Class[] {forClass},createInvocationHandler(o,forClass));
        return proxy;
    }
}
