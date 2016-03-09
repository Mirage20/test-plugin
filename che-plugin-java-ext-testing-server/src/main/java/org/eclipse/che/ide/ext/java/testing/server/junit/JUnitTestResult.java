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

import java.util.ArrayList;
import java.util.List;

public class JUnitTestResult implements TestResult {

    private boolean isSuccess;
    private List<Failure> failures;

    public JUnitTestResult(boolean isSuccess,List<Failure> failures) {
        this.isSuccess = isSuccess;
        this.failures = failures;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public List<Failure> getFailures() {
        return failures;
    }

    public int getFailureCount() {
        return failures.size();
    }
}
