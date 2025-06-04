package com.lwk.localimagepreview

import com.intellij.psi.PsiElement
import com.jetbrains.lang.dart.psi.DartStringLiteralExpression
import java.util.regex.Pattern

class DartLineMarkerContributor : LineMakerContributor() {
    // Dart图片路径的正则表达式
    private val imagePathPattern = Pattern.compile(
        "\"([^\"]+\\.(png|jpg|jpeg|gif|webp|svg))\"|'([^']+\\.(png|jpg|jpeg|gif|webp|svg))'",
        Pattern.CASE_INSENSITIVE)

    override fun getInfo(element: PsiElement): Info? {
        // 检查元素本身是否有效
        if (!element.isValid) {
            return null
        }

        // 检查是否为叶子元素
        if (element.children.isNotEmpty()) {
            return null
        }

        if (element is DartStringLiteralExpression) {
            val text = element.text;
            if (text is String) {
                // 使用正则表达式查找所有可能的图片路径
                val matcher = imagePathPattern.matcher(text)
                while(matcher.find()) {
                    // 获取匹配的路径并去除引号
                    val imagePath = if (matcher.group(1) != null) matcher.group(1) else matcher.group(3)
                    if (imagePath != null) {
                        return getLineMaker(element.project, imagePath)
                    }
                }
                return null
            }
        }



        return null

        //if (element is JSVariable) {

//            if (!element.firstChild.isValid) {
//                return null
//            }

        //val document = PsiDocumentManager.getInstance(element.project).getDocument(element.containingFile) ?: return null
        //val lineNumber = document.getLineNumber(element.startOffset)
        //val imgUrl = getJsVariableContent(element)
        //setLineMapping(element.containingFile.virtualFile, lineNumber, removeUrlQuotes(imgUrl))

//            return getLineMaker(removeUrlQuotes(""))
        //}
        //return null
    }


}