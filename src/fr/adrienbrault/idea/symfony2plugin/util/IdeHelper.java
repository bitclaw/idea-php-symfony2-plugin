package fr.adrienbrault.idea.symfony2plugin.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import fr.adrienbrault.idea.symfony2plugin.Settings;
import fr.adrienbrault.idea.symfony2plugin.SettingsForm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.HyperlinkEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class IdeHelper {

    public static void openUrl(String url) {
        if(java.awt.Desktop.isDesktopSupported() ) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

            if(desktop.isSupported(java.awt.Desktop.Action.BROWSE) ) {
                try {
                    java.net.URI uri = new java.net.URI(url);
                    desktop.browse(uri);
                } catch (URISyntaxException ignored) {
                } catch (IOException ignored) {

                }
            }
        }
    }
    @Nullable
    public static VirtualFile createFile(@Nullable VirtualFile root, @NotNull String fileNameWithPath) {
        return createFile(root, fileNameWithPath, null);
    }

    @Nullable
    public static VirtualFile createFile(@Nullable VirtualFile root, @NotNull String fileNameWithPath, @Nullable String content) {

        if(root == null) {
            return null;
        }

        String path = fileNameWithPath.substring(0, fileNameWithPath.lastIndexOf("/"));

        try {
            VfsUtil.createDirectoryIfMissing(root, path);
        } catch (IOException e) {
            return null;
        }

        VirtualFile twigDirectory = VfsUtil.findRelativeFile(root, path.split("/"));
        if(twigDirectory == null || !twigDirectory.exists()) {
            return null;
        }

        File f = new File(twigDirectory.getCanonicalPath() + "/" + fileNameWithPath.substring(fileNameWithPath.lastIndexOf("/") + 1));
        if(!f.exists()){
            try {
                if(!f.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }

        VirtualFile virtualFile = VfsUtil.findFileByIoFile(f, true);
        if(virtualFile == null) {
            return null;
        }

        if(content != null) {
            try {
                virtualFile.setBinaryContent(content.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return virtualFile;
    }

    public static RunnableCreateAndOpenFile getRunnableCreateAndOpenFile(@NotNull Project project, @NotNull VirtualFile rootVirtualFile, @NotNull String fileName) {
        return new RunnableCreateAndOpenFile(project, rootVirtualFile, fileName);
    }

    public static class RunnableCreateAndOpenFile implements Runnable {

        private final VirtualFile rootVirtualFile;
        private final String fileName;
        private final Project project;
        private String content;

        public RunnableCreateAndOpenFile(Project project, VirtualFile rootVirtualFile, String fileName) {
            this.project = project;
            this.rootVirtualFile = rootVirtualFile;
            this.fileName = fileName;
        }

        public RunnableCreateAndOpenFile setContent(@Nullable String content) {
            this.content = content;
            return this;
        }

        @Override
        public void run() {
            VirtualFile virtualFile = createFile(rootVirtualFile, fileName, this.content);
            if(virtualFile != null) {
                new OpenFileDescriptor(project, virtualFile, 0).navigate(true);
            }
        }
    }

    /**
     * pre phpstorm 7.1 dont support getStatusBar in this way
     */
    public static boolean supportsStatusBar() {
        try {
            WindowManager.getInstance().getClass().getMethod("getStatusBar", Project.class);
            StatusBar.class.getMethod("getWidget", String.class);
            
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static void notifyEnableMessage(final Project project) {

        Notification notification = new Notification("Symfony2 Plugin", "Symfony2 Plugin", "Enable the Symfony2 Plugin in <a href=\"config\">Project Settings</a> or <a href=\"dismiss\">dismiss</a> further messages", NotificationType.INFORMATION, new NotificationListener() {
            @Override
            public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent event) {

                // handle html click events
                if("config".equals(event.getDescription())) {

                    // open settings dialog and show panel
                    SettingsForm.show(project);

                } else if("dismiss".equals(event.getDescription())) {

                    // use dont want to show notification again
                    Settings.getInstance(project).dismissEnableNotification = true;
                }

                notification.expire();
            }

        });

        Notifications.Bus.notify(notification, project);
    }

}
