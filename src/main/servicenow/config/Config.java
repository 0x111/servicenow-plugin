package main.servicenow.config;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.LocalTimeCounter;

public class Config {
    public static final String CONFIG_FILE = ".servicenow-plugin.json";

    public static Boolean exists(PsiDirectory directory) {
        PsiFile psiFile = directory.findFile(CONFIG_FILE);
        return psiFile != null;
    }

    public static PsiFile read(PsiDirectory directory) {
        return directory.findFile(CONFIG_FILE);
    }

    public static void write(Project project, String text, final PsiDirectory directory, Boolean exists) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            final PsiFile[] file = new PsiFile[1];
            if (exists) {
                file[0] = PsiFileFactory.getInstance(project).createFileFromText(
                        CONFIG_FILE,
                        JsonFileType.INSTANCE,
                        text);
            } else {
                file[0] = PsiFileFactory.getInstance(project).createFileFromText(
                        CONFIG_FILE,
                        JsonFileType.INSTANCE,
                        text,
                        LocalTimeCounter.currentTime(),
                        true);
            }
            directory.add(file[0]);
        });
    }
}
