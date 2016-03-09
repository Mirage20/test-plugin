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

import com.google.inject.Inject;
import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.action.Action;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.ide.api.project.tree.VirtualFile;
import org.eclipse.che.ide.ext.java.client.action.JavaEditorAction;
import org.eclipse.che.ide.ext.java.client.projecttree.JavaSourceFolderUtil;
//import org.eclipse.che.ide.ext.java.client.projecttree.JavaSourceFolderUtil;
//import org.eclipse.che.ide.ext.java.client.action.JavaEditorAction;

public class TestAction extends JavaEditorAction {

    private final NotificationManager notificationManager;
    private final EditorAgent editorAgent;
    private final TestServiceClient testServiceClient;


    @Inject
    public TestAction(TestResources resources, NotificationManager notificationManager, EditorAgent editorAgent,
                      TestServiceClient testServiceClient) {
        super("Run as JUnit Test...", "Start the testing.", resources.TestIcon(), editorAgent);
        this.notificationManager = notificationManager;
        this.editorAgent = editorAgent;
        this.testServiceClient = testServiceClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // This calls the service in the workspace.
        // This method is in our TestServiceClient class
        // This is a Promise, so the .then() method is invoked after the response is made
        EditorPartPresenter editorPart = editorAgent.getActiveEditor();
        final VirtualFile file = editorPart.getEditorInput().getFile();
        final String projectPath = file.getProject().getProjectConfig().getPath();

        String fqn = JavaSourceFolderUtil.getFQNForFile(file);
//        String fqn = editorPart.getEditorInput().getFile().getPath();
//        final String projectPath = "/console-java-simple";
//        String fqn = "org.eclipse.che.examples.HelloWorld";
        testServiceClient.computeProposals(projectPath, fqn)
                .then(new Operation<String>() {
                    @Override
                    public void apply(String arg) throws OperationException {
                        if (arg.equals("Success")) {
                            // This passes the response String from the method return to the notification manager.
                            notificationManager.notify(arg, StatusNotification.Status.SUCCESS, true);
                        } else {
                            notificationManager.notify(arg, StatusNotification.Status.FAIL, true);
                        }
                    }
                })
                .catchError(new Operation<PromiseError>() {
                    @Override
                    public void apply(PromiseError arg) throws OperationException {
                        notificationManager.notify("Fail", StatusNotification.Status.FAIL, true);
                    }
                });
    }
}
