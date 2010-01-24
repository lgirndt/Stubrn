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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates Method Callbacks for fields in the given Object, which are marked
 * with then respective stubrn annoations.
 */
class AnnotatedFieldMatcher implements MethodMatcher {

    private static final Class<ByName> ANNOTATION = ByName.class;

    private final Object holder;
    private final Map<String,Field> annotatedFields;

    public AnnotatedFieldMatcher(Object holder) {
        this.holder = holder;
        this.annotatedFields = createAnnotatedFieldMapping();
    }

    private Map<String,Field> createAnnotatedFieldMapping(){

        Map<String,Field> map = new HashMap<String,Field>();
        for(Field f : holder.getClass().getDeclaredFields()){
            if(Visibilities.isNotAccessible(f)){
                continue;
            }
            f.setAccessible(true);

            if(f.isAnnotationPresent(ANNOTATION)){
                map.put(f.getName(),f);
            }
        }
        return map;
    }


    @Override
    public  Callback matchMethod(Method method) {
        try {
            Field f = annotatedFields.get(method.getName());

            if(f == null) {
                return null;
            }

            ReturnValueCallback callback = new ReturnValueCallback(f.get(holder),f.getType());

            Class<?> toType = method.getReturnType();
            Class<?> fromType = callback.getReturnType();

            if(!TypeCoercion.isConvertible(fromType,toType)){
                String msg = constructProblemMsg(method, toType, fromType);
                throw new InvalidReturnTypeException(msg);
            }

            return callback;
            
        } catch (IllegalAccessException e) {
            throw new StubrnHandlingException(e);
        }
    }

    private String constructProblemMsg(Method method, Class<?> methodType, Class<?> fieldType) {
        return "Method return type '" +
                methodType.getName() +
                "' and Stub return type '" +
                fieldType.getName() +
                "' of method '" +
                method.getName() +
                "' differ";
    }
}
