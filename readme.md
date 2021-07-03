# 项目介绍
这是一款支持富文本编辑的安卓笔记本项目。

手机自带的备忘录普遍存在难以查找笔记的问题，只能用于记录少量内容备忘。但记录的内容多了，这一问题依旧会影响使用体验。

该项目为了解决这一问题，为笔记添加分类。用户还可以为每一篇笔记增加标签，通过标签搜索同类笔记。



## 项目预览

<img src="https://github.com/nonGuo/Android-LifeSnapshot/raw/1.0/image-preview/preview-1.png" style="zoom:25%;" /><img src="https://github.com/nonGuo/Android-LifeSnapshot/raw/1.0/image-preview/preview-2.png" style="zoom:25%;" /><img src="https://github.com/nonGuo/Android-LifeSnapshot/raw/1.0/image-preview/preview-3.png" style="zoom:25%;" />



## 项目APK

由于时间问题暂未上架安卓商城

下载地址：https://github.com/nonGuo/Android-LifeSnapshot/releases/download/1.0/app-release.apk



## 实现方案

- 富文本编辑：本项目引用了[richeditor-android](https://github.com/wasabeef/richeditor-android)
- 笔记存储：使用SQLite保存笔记信息（分类，标签，创建时间等），笔记内容导出为本地HTML文件保存
- 笔记显示：目前三个分类（日记，计划，备忘）都是由同一 Fragment 实现。在Fragment中使用RecycleView动态添加笔记。
- 云端同步：使用腾讯云TCB作为后端。由于时间原因，目前仅实现了用户注册登录，备份功能尚未完成。



## 快速开始

[详情请看wiki](https://github.com/nonGuo/Android-LifeSnapshot/wiki)



## 感谢以下开源项目

- [SlidingRootNav](https://github.com/yarolegovich/SlidingRootNav)

- [Matisse](https://github.com/zhihu/Matisse)

- [glide](https://github.com/bumptech/glide)

- [richeditor-android](https://github.com/wasabeef/richeditor-android)

- [LovelyDialog](https://github.com/yarolegovich/LovelyDialog)

  