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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 *
 */
public class SignatureTest {

    interface A {
        String aMethod(int a,Double b,Object [] c);
    }

    interface B {
        String aMethod(int a,Double b,Object [] c);
    }

    @Test
    public void testCreation(){
        Signature s = Signature.create(A.class.getMethods()[0]);
        assertEquals(s.getReturnType(), String.class);
        assertEquals(s.getName(),"aMethod");
        assertEquals(s.getParameterTypes().size(),3);
        assertEquals(s.getParameterTypes().get(0),int.class);
        assertEquals(s.getParameterTypes().get(1),Double.class);
        
    }

    @Test
    public void testEquality(){
        Signature a = Signature.create(A.class.getMethods()[0]);
        Signature b = Signature.create(B.class.getMethods()[0]);
        assertEquals(a,b);
        assertTrue(a.hashCode() == b.hashCode());
    }
}
