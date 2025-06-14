package com.lwk.localimagepreview

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.swing.Icon
import javax.swing.ImageIcon

abstract class LineMakerContributor: RunLineMarkerContributor() {
    //private val imagePool = ImagePool

    fun getLineMaker (project: Project, imagePath: String): Info? {
        val imageFile = findImageFile(project, imagePath)
        if (imageFile != null && imageFile.exists()) {
            val icon = loadScaledImageIcon(imageFile)

            val openImageAction = object : AnAction("Preview", "Preview", AllIcons.Debugger.Watch) {
                override fun actionPerformed(e: AnActionEvent) {
                    print("122")
                    val project = e.project ?: return
                    try {
                        // 在编辑器中打开这个虚拟文件
                        //FileEditorManager.getInstance(project).openFile(createImageVirtualFile(imagePath), true)

                        openImageInEditor(project, imageFile)

                    } catch (ex: Exception) {
                        Messages.showErrorDialog(project, "Failed to open image: ${ex.message}", "Error")
                    }
                }
            }
            return  Info(icon, arrayOf(openImageAction))
        }


        return null
    }

    private fun findImageFile(project: Project, imagePath: String): File? {
        // 尝试在项目目录下直接查找
        val projectBasePath = project.basePath ?: return null
        val directFile = File(projectBasePath, imagePath)
        if (directFile.exists()) return directFile

        // 尝试在assets目录下查找
        val pubspecFile = findPubspecFile(project)
        if (pubspecFile != null) {
            val parentPath = pubspecFile.parent ?: return null
            val assetsDir = File(parentPath.path, "assets")
            val assetFile = File(assetsDir, imagePath)
            if (assetFile.exists()) return assetFile
        }

        // 尝试通过文件名索引查找
//        val fileName = imagePath.substringAfterLast("/")
//        val virtualFiles = FilenameIndex.getVirtualFilesByName(
//            project, fileName, GlobalSearchScope.projectScope(project)
//        )
//
//        for (file in virtualFiles) {
//            if (file.path.endsWith(imagePath)) {
//                return File(file.path)
//            }
//        }

        return null
    }

    private fun findPubspecFile(project: Project): VirtualFile? {
        val virtualFile = FilenameIndex.getVirtualFilesByName(
            "pubspec.yaml", GlobalSearchScope.projectScope(project)
        )
        return virtualFile.firstOrNull();
    }

    private fun loadScaledImageIcon(file: File): Icon? {
        return try {
            scaleIcon(ImageIcon(file.path))
            //ImageIcon(file.path)
            //IconLoader.getIcon(file.path, this.javaClass)
            // 缩略图最大高度
//            val maxHeight = JBUIScale.scale(20)
//
//            if (originalIcon.iconHeight <= maxHeight) {
//                originalIcon
//            } else {
//                val scaledFactor = maxHeight.toDouble()
//                val scaledWidth = (originalIcon.iconWidth * scaledFactor).toInt()
//                val scaledHeight = (originalIcon.iconHeight * scaledFactor).toInt()
//
//                val scaledImage = (originalIcon.image as Image).getScaledInstance(
//                    scaledWidth, scaledHeight, Image.SCALE_SMOOTH
//                )
//                ImageIcon(scaledImage)
//            }

        } catch (e: Exception) {
            null
        }
    }

    fun scaleIcon(icon: Icon, width: Int = 30, height: Int = 30): Icon {
        if (icon is ImageIcon) {
            val scaledImage = icon.image.getScaledInstance(width, height, Image.SCALE_SMOOTH)
            return ImageIcon(scaledImage)
        }

        // 如果不是ImageIcon，尝试转换为BufferedImage
        val bufferedImage = BufferedImage(icon.iconWidth, icon.iconHeight, BufferedImage.TYPE_INT_ARGB)
        val g = bufferedImage.createGraphics()
        icon.paintIcon(null, g, 0, 0)
        g.dispose()

        val scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        return ImageIcon(scaledImage)
    }

    // 在编辑器中打开图片
    private fun openImageInEditor(project: Project, imageFile: File) {

        // 通过路径获取 VirtualFile 对象
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(imageFile)
        virtualFile?.let { file ->
            // 使用 FileEditorManager 打开文件
            FileEditorManager.getInstance(project).openFile(file, true)
        } ?: return
    }

}