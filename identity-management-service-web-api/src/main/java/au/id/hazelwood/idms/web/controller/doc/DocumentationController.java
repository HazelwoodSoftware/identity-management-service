/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.id.hazelwood.idms.web.controller.doc;

import com.knappsack.swagger4springweb.controller.ApiDocumentationController;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.model.ApiListing;
import com.wordnik.swagger.model.ResourceListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Swagger documentation controller to configure {@link ApiDocumentationController}.
 *
 * @author Ricky Hazelwood
 */
@Controller
@RequestMapping(value = "")
public class DocumentationController extends ApiDocumentationController
{
    @Autowired
    public DocumentationController(ApiInfo apiInfo, @Value("${idms.api.version}") String apiVersion)
    {
        setApiInfo(apiInfo);
        setApiVersion(apiVersion);
        setBaseModelPackage("au.id.hazelwood.idms.web.dto");
        setBaseControllerPackage("au.id.hazelwood.idms.web.controller");
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResourceListing getResources(HttpServletRequest request)
    {
        return super.getResources(request);
    }

    @Override
    @RequestMapping(value = "/doc/**", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiListing getDocumentation(HttpServletRequest request)
    {
        return super.getDocumentation(request);
    }
}
