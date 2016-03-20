/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.java.testing.client.view;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import org.eclipse.che.api.git.shared.CheckoutRequest;
import org.eclipse.che.api.project.gwt.client.ProjectServiceClient;
import org.eclipse.che.api.workspace.shared.dto.ProjectConfigDto;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.event.project.OpenProjectEvent;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.ide.api.project.tree.VirtualFile;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.ide.ext.java.client.projecttree.JavaSourceFolderUtil;
import org.eclipse.che.ide.ext.java.testing.client.TestServiceClient;
import org.eclipse.che.ide.extension.machine.client.processes.ConsolesPanelPresenter;
import org.eclipse.che.ide.part.explorer.project.ProjectExplorerPresenter;
import org.eclipse.che.ide.rest.AsyncRequestCallback;
import org.eclipse.che.ide.rest.DtoUnmarshallerFactory;

import org.eclipse.che.ide.util.loging.Log;
import org.eclipse.che.ide.websocket.rest.RequestCallback;
import org.eclipse.che.ide.websocket.rest.StringUnmarshallerWS;
import org.eclipse.che.ide.websocket.rest.Unmarshallable;

import static org.eclipse.che.ide.api.notification.StatusNotification.Status.FAIL;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.PROGRESS;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.SUCCESS;

/**
 * Presenter for checkout reference(branch, tag) name or commit hash.
 *
 * @author Roman Nikitenko
 */
@Singleton
public class TestRunnerPresenter implements TestRunnerView.ActionDelegate {
    public static final String CHECKOUT_COMMAND_NAME = "Git checkout";

    private final NotificationManager notificationManager;
    private final TestServiceClient service;
    private final AppContext appContext;
    private final TestRunnerView view;
    private final ProjectExplorerPresenter projectExplorer;
    private final DtoFactory dtoFactory;
    private final EditorAgent editorAgent;
    private final EventBus eventBus;
    private final ProjectServiceClient projectService;
    private final DtoUnmarshallerFactory dtoUnmarshallerFactory;
    private final ConsolesPanelPresenter consolesPanelPresenter;

    @Inject
    public TestRunnerPresenter(TestRunnerView view,
                               TestServiceClient service,
                               AppContext appContext,

                               NotificationManager notificationManager,
                               ProjectExplorerPresenter projectExplorer,
                               DtoFactory dtoFactory,
                               EditorAgent editorAgent,
                               EventBus eventBus,
                               ProjectServiceClient projectServiceClient,
                               DtoUnmarshallerFactory dtoUnmarshallerFactory,
                               ConsolesPanelPresenter consolesPanelPresenter) {
        this.view = view;
        this.projectExplorer = projectExplorer;
        this.dtoFactory = dtoFactory;
        this.editorAgent = editorAgent;
        this.eventBus = eventBus;
        this.view.setDelegate(this);
        this.service = service;
        this.appContext = appContext;

        this.notificationManager = notificationManager;
        this.projectService = projectServiceClient;
        this.dtoUnmarshallerFactory = dtoUnmarshallerFactory;
        this.consolesPanelPresenter = consolesPanelPresenter;
    }

    /**
     * Show dialog.
     */
    public void showDialog() {
        view.showDialog();
    }

    @Override
    public void onCancelClicked() {
        view.close();
    }

    @Override
    public void onRunClicked() {
        view.setText("Running testttts....");
        final StatusNotification notification = new StatusNotification("Running Test...", PROGRESS, true);
        notificationManager.notify(notification);
        final ProjectConfigDto project = appContext.getCurrentProject().getRootProject();
        EditorPartPresenter editorPart = editorAgent.getActiveEditor();
        final VirtualFile file = editorPart.getEditorInput().getFile();
        String fqn = JavaSourceFolderUtil.getFQNForFile(file);
        Unmarshallable<String> unmarshaller = new StringUnmarshallerWS();

        service.runTest(appContext.getWorkspaceId(),
                project,
                fqn,
                new RequestCallback<String>(unmarshaller) {
                    @Override
                    protected void onSuccess(String result) {
                        notification.setTitle("Test runner executed successfully");
                        notification.setContent(result);
                        notification.setStatus(SUCCESS);
                        view.setText(result);
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        final String errorMessage = (exception.getMessage() != null)
                                ? exception.getMessage()
                                : "Faild to run test cases";
                        notification.setContent(errorMessage);
                        notification.setStatus(FAIL);
                    }
                }
        );
    }


    @Override
    public void onEnterClicked() {
        onRunClicked();
    }


}
