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

import stubrn.stubs.annotations.ByName;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
class ExistingMethodMatcher implements MethodMatcher {

    private Map<Signature, Callback> signatureCallbacks;
    private Map<String, Callback> nameCallbacks;

    private Map<Class<?>,Object> dummyPrimitiveValues;

    public ExistingMethodMatcher(Object holder){
        fillCallbackMaps(holder);
        fillDummyPrimitiveValueMap();
    }

    private void fillCallbackMaps(Object holder) {
        signatureCallbacks = new HashMap<Signature,Callback>();
        nameCallbacks = new HashMap<String,Callback>();

        for (Method method : holder.getClass().getDeclaredMethods()) {

            if(Visibilities.isNotAccessible(method)){
                continue;
            }
            method.setAccessible(true);

            if (!method.isAnnotationPresent(ByName.class)) {
                fillMethodBySignature(holder, method);
            } else {
                fillMethodByName(holder, method);
            }
        }
    }

    private void fillMethodBySignature(Object holder, Method method) {
        Signature signature = Signature.create(method);
        signatureCallbacks.put(signature, createCallback(holder, method,false));
    }

    private void fillMethodByName(Object holder, Method method) {
        String name = method.getName();

        if (!nameCallbacks.containsKey(name)) {
            nameCallbacks.put(name, createCallback(holder, method,true));
        }
    }

    private void fillDummyPrimitiveValueMap(){
        dummyPrimitiveValues = new HashMap<Class<?>,Object>();

        Integer zero = 0;
        putPrimVal(byte.class,zero.byteValue() );
        putPrimVal(char.class,'0');
        putPrimVal(short.class, zero.shortValue());
        putPrimVal(int.class,zero);
        putPrimVal(long.class,zero.longValue());
        putPrimVal(double.class,zero.doubleValue());
        putPrimVal(float.class,zero.floatValue());
    }

    private void putPrimVal(Class<?> type,Object val){
        dummyPrimitiveValues.put(type,val);
    }

    private Object [] createDummyArgs(final Method method){
        ArrayList<Object> dummyArgsList = new ArrayList<Object>();
        for(Class<?> t : method.getParameterTypes() ){
            dummyArgsList.add(getDummyValue(t));
        }
        return dummyArgsList.toArray();
    }

    /**
     * Returns a representation of 0 for all primitive types otherwise null
     *
     * @param forType
     * @return
     */
    private Object getDummyValue(Class<?> forType){
        return dummyPrimitiveValues.get(forType);
    }

    private Callback createCallback(final Object holder, final Method method,final boolean ignoreParams) {
        return new Callback() {

            private Object [] defaultArgs;

            private Object [] getDefaultArgs(){
                if(defaultArgs == null){
                    defaultArgs = createDummyArgs(method);
                }
                return defaultArgs;
            }

            @Override
            public Class<?> getReturnType() {
                return method.getReturnType();
            }

            @Override
            public Object call(Object[] args) {

                try {
                    args = ignoreParams ? getDefaultArgs() : args;
                    return method.invoke(holder, args);
                } catch (IllegalAccessException e) {
                    throw new StubrnHandlingException(e);
                } catch (InvocationTargetException e) {
                    throw new StubrnHandlingException(e);
                }
            }
        };
    }

    @Override
    public Callback matchMethod(Method method) {
        final Callback signatureCallback = signatureCallbacks.get(Signature.create(method));
        if(signatureCallback != null){
            return signatureCallback;
        }
        Callback nameCallback = nameCallbacks.get(method.getName());
        if(nameCallback == null){
            return null;
        }

        Class<?> stubType = nameCallback.getReturnType();
        Class<?> methodType = method.getReturnType();

        if(!TypeCoercion.isConvertible(stubType, methodType)){
            return throwInvalidReturnTypeException(method, stubType, methodType);
        }

        return nameCallback;
    }

    private Callback throwInvalidReturnTypeException(Method method, Class<?> stubType, Class<?> methodType) {
        String msg = "Method return type '" +
            methodType.getName() +
            "' and Stub return type '" +
            stubType.getName() +
            "' of method '" +
            method.getName() +
            "' differ";
        throw new InvalidReturnTypeException();
    }
}
