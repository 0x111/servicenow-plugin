package main.servicenow.action;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiDirectory;

import javax.swing.*;

import main.servicenow.config.Config;
import main.servicenow.http.HttpClient;
import main.servicenow.notificationmanager.Notification;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class DialogManagerAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final Project project = anActionEvent.getProject();
        assert project != null;
        VirtualFile baseDir = anActionEvent.getProject().getBaseDir();
        PsiDirectory directory = PsiManager.getInstance(anActionEvent.getProject()).findDirectory(baseDir);

        if (!Config.exists(directory)) {
            SwingUtilities.invokeLater(() -> Notification.showNotification(anActionEvent.getProject(), "Config file does not exists! Please fill it out and try again!", MessageType.ERROR, 3500));
            ActionManager.getInstance().getAction("ConfigEditorAction").actionPerformed(anActionEvent);
            return;
        }

        DialogBuilder builder = new DialogBuilder(getEventProject(anActionEvent));
        JPanel panel = new JPanel(new MigLayout());
        JLabel label = new JLabel("<html>" +
                "<p>Enter the data in the following format tablename:field_name:sys_id</p>" +
                "<p>(E.g. script:sys_script:457744279fb302c01526317f842e7063)</p>" +
                "</html>");

        JTextField input = new JTextField(30);
        panel.add(label, "span");
        panel.add(input, "align center");
        input.setToolTipText("Paste the data here");
        builder.setCenterPanel(panel);
        builder.setTitle("Download a Script From ServiceNow");
        builder.removeAllActions();
        builder.addOkAction();
        builder.addCancelAction();

        if (builder.show() == DialogWrapper.OK_EXIT_CODE) {
            SwingUtilities.invokeLater(() -> Notification.showNotification(project, "Downloading your script...", MessageType.INFO, 3000));
            try {
                String filename = input.getText();
                HttpClient http = new HttpClient(anActionEvent.getProject());
                http.DownloadScript(filename, anActionEvent.getProject(), directory);
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> Notification.showNotification(project, "There was a problem while downloading your script", MessageType.ERROR, 3000));
                e.printStackTrace();
            }
        }
    }
}