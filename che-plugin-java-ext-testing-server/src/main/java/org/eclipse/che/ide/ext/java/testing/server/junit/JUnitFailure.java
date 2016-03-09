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

public class JUnitFailure implements Failure {

    private String message;
    private String trace;

    public JUnitFailure(String message,String trace) {
        this.message = message;
        this.trace = trace;
    }

    public String getMessage() {
        return message;
    }

    public String getTrace() {
        return trace;
    }
}
