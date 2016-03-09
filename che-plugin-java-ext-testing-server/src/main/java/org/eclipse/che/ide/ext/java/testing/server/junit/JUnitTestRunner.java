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
package org.eclipse.che.ide.ext.java.testing.server.junit;

import org.eclipse.che.ide.ext.java.testing.server.api.Failure;
import org.eclipse.che.ide.ext.java.testing.server.api.TestResult;
import org.eclipse.che.ide.ext.java.testing.server.api.TestRunner;


import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class JUnitTestRunner extends TestRunner {

    public JUnitTestRunner(String projectPath) throws Exception {
        super(projectPath);
    }

    @Override
    public TestResult run(String testClass) throws Exception {
        URLClassLoader classLoader = new URLClassLoader(classUrls.toArray(new URL[classUrls.size()]), null);
        Class<?> clsTest = Class.forName(testClass, true, classLoader);
        Class<?> clsJUnitCore = Class.forName("org.junit.runner.JUnitCore", true, classLoader);
        Class<?> clsResult = Class.forName("org.junit.runner.Result", true, classLoader);
        Class<?> clsFailure = Class.forName("org.junit.runner.notification.Failure", true, classLoader);


        Object result =  clsJUnitCore.getMethod("runClasses", Class[].class)
                .invoke(null, new Object[]{new Class[]{clsTest}});


        boolean isSuccess = (Boolean) clsResult.getMethod("wasSuccessful",null).invoke(result,null);
        List failures = (List) clsResult.getMethod("getFailures",null).invoke(result,null);
        List<Failure> jUnitFailures = new ArrayList<>();
        for(Object failure : failures){
            String message =(String) clsFailure.getMethod("getMessage",null).invoke(failure,null);
            String trace =(String) clsFailure.getMethod("getTrace",null).invoke(failure,null);
            jUnitFailures.add(new JUnitFailure(message,trace));
        }
        return new JUnitTestResult(isSuccess,jUnitFailures);
    }
}
