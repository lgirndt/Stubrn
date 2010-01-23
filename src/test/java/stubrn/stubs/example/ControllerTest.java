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

package stubrn.stubs.example;

import com.google.common.collect.Lists;
import org.testng.Assert;
import org.testng.annotations.Test;
import stubrn.stubs.Stubbery;
import stubrn.stubs.annotations.ByName;

import java.util.List;

/**
 *
 */
public class ControllerTest {

    @Test
    public void simpleUsage(){

        Stubbery stubbery = new Stubbery();

        EntityDAO stub = stubbery.stubFor(EntityDAO.class,new Object(){
            List<Entity> findByName(String name) {
                return Lists.newArrayList(new Entity("foo"),new Entity("foo"));
            }
        });

        Controller sud = new Controller();
        sud.setEntityDAO(stub);

        Assert.assertEquals(sud.listEntities("foo").size(),2);
    }

    @Test
    public void usageWithFields(){

        Stubbery stubbery = new Stubbery();

        EntityDAO stub = stubbery.stubFor(EntityDAO.class,new Object(){
            @ByName List<Entity> findByName = Lists.newArrayList(new Entity("foo"));
        });

        Controller sud = new Controller();
        sud.setEntityDAO(stub);

        Assert.assertEquals(sud.listEntities("foo").size(),1);
    }
}
