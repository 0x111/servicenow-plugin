package main.servicenow.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import main.servicenow.config.Config;
import net.miginfocom.swing.MigLayout;
import com.google.gson.JsonObject;

import javax.swing.*;

public class ConfigEditorAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        DialogBuilder builder = new DialogBuilder(getEventProject(anActionEvent));
        JPanel layout = new JPanel(new MigLayout());
        JLabel label = new JLabel("<html><p>A config file does not exists. Please fill out the form with your details!</p></html>");

        JTextField snowDomain = new JTextField(20);
        JTextField snowUsername = new JTextField(20);
        JTextField snowPassword = new JTextField(20);

        assert anActionEvent.getProject() != null;
        PsiDirectory directory = PsiManager.getInstance(anActionEvent.getProject()).findDirectory(anActionEvent.getProject().getBaseDir());
        Boolean configExists = Config.exists(directory);
        if (configExists) {
            PsiFile file = Config.read(directory);
            JsonObject jsObj = new JsonParser().parse(file.getText()).getAsJsonObject();
            snowDomain.setText(jsObj.get("domain").getAsString());
            snowUsername.setText(jsObj.get("username").getAsString());
            snowPassword.setText(jsObj.get("password").getAsString());
        }
        layout.add(label, "span");
        JLabel domainLabel = new JLabel("<html>Your domain:</html>");
        layout.add(domainLabel, "cell 0 1");
        layout.add(snowDomain, "cell 1 1");
        JLabel usernameLabel = new JLabel("<html>Your Username:</html>");
        layout.add(usernameLabel, "cell 0 2");
        layout.add(snowUsername, "cell 1 2");
        JLabel passwordLabel = new JLabel("<html>Your Password:</html>");
        layout.add(passwordLabel, "cell 0 3");
        layout.add(snowPassword, "cell 1 3");
        builder.setCenterPanel(layout);
        builder.setTitle("Write Config");
        builder.removeAllActions();
        builder.addOkAction();
        builder.addCancelAction();

        boolean isOk = builder.show() == DialogWrapper.OK_EXIT_CODE;

        if (isOk) {
            try {
                String serviceNowDomain = snowDomain.getText();
                String serviceNowUsername = snowUsername.getText();
                String serviceNowPassword = snowPassword.getText();
                JsonObject obj = new JsonObject();
                obj.addProperty("domain", serviceNowDomain);
                obj.addProperty("username", serviceNowUsername);
                obj.addProperty("password", serviceNowPassword);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String prettyJsonString = gson.toJson(new JsonParser().parse(obj.toString()));

                PsiFile file = Config.read(directory);
                if (file != null) {
                    WriteCommandAction.runWriteCommandAction(anActionEvent.getProject(), file::delete);
                    SwingUtilities.invokeLater(() -> {
                        Config.write(anActionEvent.getProject(), prettyJsonString, directory, true);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        Config.write(anActionEvent.getProject(), prettyJsonString, directory, false);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
