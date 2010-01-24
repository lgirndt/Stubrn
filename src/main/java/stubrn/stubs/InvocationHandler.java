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

import java.lang.reflect.Method;

/**
 *
 */
class InvocationHandler implements java.lang.reflect.InvocationHandler {

    private final MethodCallbackDispatcher dispatcher;

    public InvocationHandler(MethodCallbackDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Callback callback = dispatcher.determineCallback(method);
        if(callback == null){
            throw new NotStubbedMethodException(
                    "There is no stub method registered for " + method.toString());
        }
        return callback.call(objects);
    }
}
