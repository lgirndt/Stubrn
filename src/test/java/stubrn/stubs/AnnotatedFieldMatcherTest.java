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
import org.testng.annotations.Test;
import stubrn.stubs.annotations.ByName;

import java.lang.reflect.Method;
import java.util.Map;

import static org.testng.Assert.*;


/**
 *
 */
public class AnnotatedFieldMatcherTest {

    private static class WithStringField {
        @ByName
        public String aString = "Foo";
    }

    private static interface WithStringMethod {
        String aString();
    }

    @Test
    public void testSimpleMatchMethod() throws Exception {
        assertReturnsString(getCallbackFromMatcher(new WithStringField()), "Foo");
    }


    private static class WithDefaultVisibleStringField {
        @ByName
        String aString = "Foo";
    }

    @Test
    public void testDefaultVisibilityOnFields() throws Exception {
        assertReturnsString(getCallbackFromMatcher(new WithDefaultVisibleStringField()),"Foo");
    }

    @Test
    public void testProtectedVisibilityOnFields() throws Exception {
        Callback callback = getCallbackFromMatcher(new Object(){
            @ByName
            protected String aString = "Foo";
        });
        assertNull(callback);
    }

    @Test
    public void testPrivateVisibilityOnFields() throws Exception {
        assertNull( getCallbackFromMatcher(new Object(){
             @ByName
             private String aString = "Foo";
        }) );
    }

    @Test(expectedExceptions = InvalidReturnTypeException.class)
    public void testWithWrongType() {
        getCallbackFromMatcher(new Object(){
            @ByName int aString = 42;
        });
    }


    private static interface WithMethods {
        String  aString();
        int     anInt();
        Integer anInteger();
    }

    @Test
    public void testWithSeveralFields() throws Exception {
        Map<String,Method> methods = createNameMethodMap(WithMethods.class.getMethods());

        AnnotatedFieldMatcher matcher = new AnnotatedFieldMatcher(new Object(){
            @ByName String aString = "Foo";
            @ByName int anInt = 42;
            @ByName Integer anInteger = 23;
        });

        Callback aStrCallback =
                matcher.matchMethod(methods.get("aString"));
        assertEquals(aStrCallback.call(null),"Foo");

        Callback anIntCallback =
                matcher.matchMethod(methods.get("anInt"));
        assertEquals(anIntCallback.call(null), 42);

        Callback anIntegerCallback =
                matcher.matchMethod(methods.get("anInteger"));
        assertEquals(anIntegerCallback.call(null),23);
    }

    private static interface WithOverloadedMethods {
        String aString();
        String aString(int a);
        String aString(String a);
    }

    @Test
    public void testWithOverloadedMethods(){
        Method [] methods = WithOverloadedMethods.class.getMethods();
        assertEquals(methods.length,3);

        AnnotatedFieldMatcher matcher = new AnnotatedFieldMatcher(new Object(){
           @ByName
           String aString = "Foo";
        });

        assertMatchingString(matcher, methods[0], "Foo");
        assertMatchingString(matcher, methods[1], "Foo");
        assertMatchingString(matcher, methods[2], "Foo");
    }

    @Test
    public void testUnassignedField(){
        Callback callback = getCallbackFromMatcher(new Object(){
            @ByName String aString;
        });
        assertNotNull(callback);
        assertNull(callback.call(null));
    }

    public interface WithVoid {
        void method();
    }

    @Test(expectedExceptions = InvalidReturnTypeException.class)
    public void testMethodWithVoid(){
        getCallbackFromMatcher(WithVoid.class.getMethods()[0],new Object(){
           @ByName int method = 42; 
        });
    }

    private void assertMatchingString(AnnotatedFieldMatcher matcher, Method m, String expected) {
        Callback callback = matcher.matchMethod(m);
        assertNotNull(callback);
        assertEquals(callback.call(null), expected);
    }

    private Map<String,Method> createNameMethodMap(Method [] methods){
        Map<String,Method> map = Maps.newHashMap();
        for(Method m : methods){
            map.put(m.getName(),m);
        }

        return map;
    }

    private void assertReturnsString(Callback callback, String expected) {
        assertNotNull(callback);
        String value = (String) callback.call(null);
        assertEquals(value, expected);
    }

    private Callback getCallbackFromMatcher(Object holder) {
        return getCallbackFromMatcher(getTestMethod(),holder);
    }

    private Callback getCallbackFromMatcher(Method method,Object holder) {
        AnnotatedFieldMatcher matcher = new AnnotatedFieldMatcher(holder);
        Callback callback = matcher.matchMethod(method);
        return callback;
    }

    private Method getTestMethod() {
        return WithStringMethod.class.getMethods()[0];
    }

}
