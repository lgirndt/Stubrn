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

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static stubrn.stubs.TypeCoercion.isConvertible;

/*
*
*/
public class TypeCoercionTest {


    interface Interface {

    }

    class InterfaceImpl implements Interface {

    }


    class MyClass {

    }

    class DerivedClass extends MyClass {

    }

    @Test
    public void testIsConvertibleEqualPrimitive(){
        assertIsConvertible(Integer.TYPE,Integer.TYPE);
    }

    @Test
    public void testIsConvertibleOfPrimitivesToWrapper() throws Exception {
        assertIsConvertible(Boolean.TYPE, Boolean.class);
        assertIsConvertible(Boolean.class, Boolean.TYPE);
        assertIsConvertible(Byte.TYPE, Byte.class);
        assertIsConvertible(Byte.class, Byte.TYPE);
        assertIsConvertible(Character.TYPE, Character.class);
        assertIsConvertible(Character.class, Character.TYPE);
        assertIsConvertible(Short.TYPE, Short.class);
        assertIsConvertible(Short.class, Short.TYPE);
        assertIsConvertible(Integer.TYPE, Integer.class);
        assertIsConvertible(Integer.class, Integer.TYPE);
        assertIsConvertible(Long.TYPE, Long.class);
        assertIsConvertible(Long.class, Long.TYPE);
        assertIsConvertible(Double.TYPE, Double.class);
        assertIsConvertible(Double.class, Double.TYPE);
        assertIsConvertible(Float.TYPE, Float.class);
        assertIsConvertible(Float.class, Float.TYPE);
        assertIsConvertible(Integer.TYPE,Integer.class);
    }

    @Test
    public void testIsConvertibleOfClasses() {
        assertIsConvertible(MyClass.class, MyClass.class);
        assertIsConvertible(DerivedClass.class, MyClass.class);
        assertIsNotConvertible(MyClass.class, DerivedClass.class);

        assertIsConvertible(Interface.class,Interface.class);
        assertIsConvertible(InterfaceImpl.class, Interface.class);
        assertIsNotConvertible(Interface.class, InterfaceImpl.class);
    }
    
    private void assertIsConvertible(Class<?> from, Class<?> to){
        assertTrue( isConvertible(from, to) );
    }

    private void assertIsNotConvertible(Class<?> from, Class<?> to){
        assertFalse( isConvertible(from, to) );
    }


}
