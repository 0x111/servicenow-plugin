package main.servicenow.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import main.servicenow.config.Config;
import main.servicenow.http.HttpClient;
import main.servicenow.notificationmanager.Notification;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScriptUploadAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        final Project project = anActionEvent.getProject();

        assert project != null;

        VirtualFile baseDir = anActionEvent.getProject().getBaseDir();
        PsiDirectory directory = PsiManager.getInstance(anActionEvent.getProject()).findDirectory(baseDir);

        if (!Config.exists(directory)) {
            SwingUtilities.invokeLater(() -> Notification.showNotification(project, "Config file does not exists! Please fill it out and try again!", MessageType.ERROR, 3500));
            ActionManager.getInstance().getAction("ConfigEditorAction").actionPerformed(anActionEvent);
            return;
        }

        VirtualFile virtualFile = (VirtualFile) anActionEvent.getDataContext().getData(PlatformDataKeys.VIRTUAL_FILE.getName());
        assert virtualFile != null;
        String filename = virtualFile.getName();
        String contents = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(virtualFile.getPath()));
            String currentLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((currentLine = br.readLine()) != null) {
                stringBuilder.append(currentLine);
                stringBuilder.append("\n");
            }
            contents = stringBuilder.toString();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            HttpClient http = new HttpClient(anActionEvent.getProject());
            http.put(filename, contents, anActionEvent.getProject(), 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
