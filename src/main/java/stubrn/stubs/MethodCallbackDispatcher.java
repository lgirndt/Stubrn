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

import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 *
 */
class MethodCallbackDispatcher<T> {

    private final Class<T> onType;


    private final ProblemPolicy problemPolicy;

    private final Map<Signature, Callback> signatureCallbacks;

    private final Callback defaultCallback;

    public MethodCallbackDispatcher(
            Class<T> onType,
            Callback defaultCallback,
            ProblemPolicy problemPolicy,
            Collection<MethodMatcher> methodMatchers) {
        this.onType = onType;

        this.defaultCallback = defaultCallback;

        this.problemPolicy = problemPolicy;
        this.signatureCallbacks =
                createSignatureCallbacks(methodMatchers);
    }        

    /**
     * Iterates through the given matchers and registers their MethodCallbacks for the respective
     * Methods. The order of the MethodMatchers in the collection determines the priority of
     * which Callback will be taken. If there is already a registered callback for the method, the
     * current matcher is not asked.
     *
     */
    Map<Signature, Callback> createSignatureCallbacks(
            Collection<MethodMatcher> matchers){

        Map<Signature, Callback> map = Maps.newHashMap();
        for(MethodMatcher matcher : matchers){
            for(Method method : onType.getMethods()){

                Signature signature = Signature.create(method);

                if(!map.containsKey(signature)){
                    Callback callback = matcher.matchMethod(method,problemPolicy);
                    if(callback != null){
                        map.put(signature,callback);
                    }
                }
            }
        }
        return map;
    }

    /**
     * Gets the callback for the given method. Either a callback is registered, or the default
     * behaviour will be applied
     *
     * @param method
     * @return
     */
    public Callback determineCallback(Method method){
        Signature s = Signature.create(method);
        Callback callback = signatureCallbacks.get(s);
        return (callback != null) ? callback : defaultCallback;
    }
}
