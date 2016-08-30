package main.servicenow.command;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.LocalTimeCounter;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.command.WriteCommandAction;

public class CreateFileCommand {
    public static PsiFile createTempPsiFile(@NotNull final Project project, @NotNull final String text, @NotNull final String filename, final PsiDirectory directory) {
        String[] arr = filename.split(":");
        if (arr.length < 3) {
            System.exit(1);
        }

        try {
            final PsiFile[] file = new PsiFile[1];
            final PsiFile[] ffile = new PsiFile[1];
            WriteCommandAction.runWriteCommandAction(project, () -> {
                file[0] = PsiFileFactory.getInstance(project).createFileFromText(arr[0] + "." + arr[1] + "." + arr[2] + ".js",
                        StdFileTypes.JS,
                        text, LocalTimeCounter.currentTime(), true);
                ffile[0] = (PsiFile) directory.add(file[0]);
            });
            return ffile[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
