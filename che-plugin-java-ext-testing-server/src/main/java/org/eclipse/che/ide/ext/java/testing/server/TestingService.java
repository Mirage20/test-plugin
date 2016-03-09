/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.java.testing.server;

import org.eclipse.che.ide.ext.java.testing.server.api.Failure;
import org.eclipse.che.ide.ext.java.testing.server.api.TestResult;
import org.eclipse.che.ide.ext.java.testing.server.api.TestRunner;
import org.eclipse.che.ide.ext.java.testing.server.junit.JUnitTestRunner;
import org.eclipse.core.resources.ResourcesPlugin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("testing/{ws-id}")
public class TestingService {

    @GET
    @Path("run")
    @Produces("text/plain")
    public String reconcile(@QueryParam("projectpath") String projectPath, @QueryParam("fqn") String fqn) {
        String absoluteProjectPath = ResourcesPlugin.getPathToWorkspace() + projectPath;
        TestRunner testRunner = null;
        try {
            testRunner = new JUnitTestRunner(absoluteProjectPath);
            TestResult result = testRunner.run(fqn);
            if (result.isSuccess()) {
                return "Success";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(result.getFailureCount()).append(" Tests are failed.\n");
                for (Failure failure : result.getFailures()) {
                    sb.append("Fail: ").append(failure.getMessage())
                            .append(" \nStack Trace \n").append(failure.getTrace());
                }
                return sb.toString();
            }
        } catch (Exception e) {
            return "Error while running test cases: " +e.getMessage();
        }
    }
}
