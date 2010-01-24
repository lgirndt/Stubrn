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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
*
*/
class TypeCoercion {

    static class CoercionPair {
        private Class<?> from;
        private Class<?> to;

        CoercionPair(Class<?> from, Class<?> to) {
            this.from = from;
            this.to = to;
        }

        public Class<?> getFrom() {
            return from;
        }

        public Class<?> getTo() {
            return to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CoercionPair that = (CoercionPair) o;

            if (from != null ? !from.equals(that.from) : that.from != null) return false;
            if (to != null ? !to.equals(that.to) : that.to != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = from != null ? from.hashCode() : 0;
            result = 31 * result + (to != null ? to.hashCode() : 0);
            return result;
        }

        public CoercionPair toInverse(){
            return new CoercionPair(this.to, this.from);
        }
    }

    private static final Set<CoercionPair> defaultCoercions = createConversions();

    private static Set<CoercionPair> createConversions(){
        Set<CoercionPair> set = new HashSet<CoercionPair>();

        addToSet(set, Boolean.TYPE, Boolean.class);
        addToSet(set, Byte.TYPE, Byte.class);
        addToSet(set, Character.TYPE, Character.class);
        addToSet(set, Short.TYPE, Short.class);
        addToSet(set, Integer.TYPE, Integer.class);
        addToSet(set, Long.TYPE, Long.class);
        addToSet(set, Double.TYPE, Double.class);
        addToSet(set, Float.TYPE, Float.class );

        Set<CoercionPair> inverse = new HashSet<CoercionPair>();
        for(CoercionPair p : set){
            inverse.add(p.toInverse());
        }

        set.addAll(inverse);

        return Collections.unmodifiableSet(set);
    }

    private static void addToSet(Set<CoercionPair> set, Class<?> from, Class<?> to) {
        set.add(new CoercionPair(from, to));
    }

    public static boolean isConvertible(Class<?> from, Class<?> to){
        if(from == null || to == null) throw new NullPointerException();

        if(from.equals(to)){
            return true;
        }

        if(from.isPrimitive() || to.isPrimitive()){
            return defaultCoercions.contains(new CoercionPair(from,to));
        }

        return to.isAssignableFrom(from);
    }
}
