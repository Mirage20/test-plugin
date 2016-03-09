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
package org.eclipse.che.ide.ext.java.testing.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.project.tree.VirtualFile;
import org.eclipse.che.ide.rest.AsyncRequestCallback;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.DtoUnmarshallerFactory;
import org.eclipse.che.ide.rest.StringUnmarshaller;
import org.eclipse.che.ide.rest.Unmarshallable;
import org.eclipse.che.ide.ui.loaders.request.LoaderFactory;
import org.eclipse.che.ide.util.loging.Log;
import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.Map;

@Singleton
public class TestServiceClient {
    private final AsyncRequestFactory asyncRequestFactory;
    private final LoaderFactory loaderFactory;
    private final String extPath;
    private final String wsID;


    @Inject
    public TestServiceClient(@Named("cheExtensionPath") String extPath,
                             AsyncRequestFactory asyncRequestFactory,
                             AppContext appContext,
                             LoaderFactory loaderFactory) {
        this.asyncRequestFactory = asyncRequestFactory;
        this.loaderFactory = loaderFactory;

        // extPath gets the relative path of Che app from the @Named DI in constructor
        // appContext is a Che class that provides access to workspace
//        helloPath = extPath + "/testing/" + appContext.getWorkspace().getId();
        this.extPath =extPath;
        this.wsID = appContext.getWorkspace().getId();

    }

    // Invoked by our TestAction class
    // Invokes the request to the server
    public Promise<String> computeProposals(String projectPath, String fqn) {
        String url = extPath + "/testing/" + wsID + "/run/?projectpath=" + projectPath + "&fqn=" + fqn;

        return asyncRequestFactory.createGetRequest(url)
                .loader(loaderFactory.newLoader("Loading your response..."))
                .send(new StringUnmarshaller());
    }
}
