# Localimage Preview

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)

**简介**  
这是一个用于预览Flutter项目dart文件中的本地图片的IDEA插件项目，它能根据文件中的本地图片地址在Gutter中显示图片的预览，帮助用户快速预览图片和打开。

## 特性

- ✅ 功能：预览
![效果图](https://github.com/lan-wk/localimage-preview/blob/main/view.png)

## 开始

```bash
# 在build.gradle.kts选择合适您Android Studio的dart插件版本
dependencies {
    intellijPlatform {
        //create("IC", "2024.2.5")
        //androidStudio("2022.2.1.1", useInstaller = false)
        local(File("D:\\AS"))
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        // 新增 Dart 插件依赖
        plugin("Dart:243.23654.44")
    }
}

# 打包
gradle-Tasks-intellij platform-buildPlugin