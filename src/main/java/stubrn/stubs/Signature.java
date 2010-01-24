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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 *
 */
class Signature {

    private final Class<?> returnType;
    private final String name;
    private final List<Class<?>> parameterTypes;

    public Signature(Class<?> returnType, String name, List<Class<?>> parameterTypes) {
        this.returnType = returnType;
        this.name = name;
        this.parameterTypes = Collections.unmodifiableList(Lists.newArrayList(parameterTypes));
    }

    public Signature(Class<?> returnType, String name, Class<?> ... parameterTypes) {
        this(returnType,name, Lists.newArrayList(parameterTypes));
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public List<Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Signature signature = (Signature) o;

        if (name != null ? !name.equals(signature.name) : signature.name != null) return false;
        if (parameterTypes != null ? !parameterTypes.equals(signature.parameterTypes) : signature.parameterTypes != null)
            return false;
        if (returnType != null ? !returnType.equals(signature.returnType) : signature.returnType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = returnType != null ? returnType.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parameterTypes != null ? parameterTypes.hashCode() : 0);
        return result;
    }

    public static Signature create(Method method){
        return new Signature(method.getReturnType(),method.getName(),method.getParameterTypes());
    }
}
