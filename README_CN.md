####【写在前面】
##这个项目是克隆自 Aspsine的 SwipeToLoadLayout ,我不是这个项目的原作者，如果你想了解更多关于原作品的信息，请访问 ##https://github.com/Aspsine/SwipeToLoadLayout
##我创建这个分支是因为我觉得原作者更新太慢，如果你也喜欢这个作品并且希望它能变得更好，欢迎Star我或者提交你的idea到这个项目，我会尽我最快的速度更新！


###【区别】
##1、添加一个选择器，可以当你上拉刷新或者加载更多时隐藏头布局或者尾布局，而不是一直保持它们。

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SwipeToLoadLayout-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2812)
# SwipeToLoadLayout
SwipeToLoadLayout是一个可以帮助你实现下拉刷新和加载更多的Layout

##支持的View
理论上支持所有的View



##Demo 截图 & 视频(Youku)
- ListView & GridView

[![ListView & GridView](http://img.youtube.com/vi/ThIKO3vz6Bs/0.jpg)](http://player.youku.com/embed/XMTM5MjA1MTIyOA==) 

- RecyclerView(With all kinds of layoutManagers)

[![RecyclerView](http://img.youtube.com/vi/ZVYkoi84Vr8/0.jpg)](http://player.youku.com/embed/XMTM5MjA1Mjc3Mg==) 

- WebView & ScrollView & Other Views

[![WebView & ScrollView & other views](http://img.youtube.com/vi/RGtWvdrVmGM/0.jpg)](http://player.youku.com/embed/XMTM5MjA1MzIxNg==) 

- Google SwipeRefreshLayout style

[![Google Style](http://img.youtube.com/vi/38NbDiUoXmg/0.jpg)](http://player.youku.com/embed/XMTM5MjA1MzU4NA==) 

- JD.com Style

[![JD Style](http://img.youtube.com/vi/QrsZ5nygTp0/0.jpg)](https://youtu.be/QrsZ5nygTp0) 

- Yalantis Phoenix Style

[![Yalantis Phoenix Style](http://img.youtube.com/vi/FAqrzSjt85c/0.jpg)](http://player.youku.com/embed/XMTM5MjA1NDQ4MA==) 

你也可以自定义自己喜欢的头部和尾部，插件试替换，非常简单哦！

##How to

- Step 1. 把 JitPack repository 添加到build.gradle文件中 repositories的末尾:
```
repositories {
    maven { url "https://jitpack.io" }
}
```
- Step 2. 在你的app build.gradle 的 dependencies 中添加依赖
```
dependencies {
	compile 'com.github.PigFlyy:SwipeToLoadLayout:v0.0.1'
}
```
- Step 3. 查看[快速集成文档](https://github.com/PigFlyy/SwipeToLoadLayout/wiki/Quick-Setup)

##致谢
- Aspsine SwipeRefreshLayout
- [liaohuqiu android-Ultra-Pull-To-Refresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh)
- [Yalantis Phoenix](https://github.com/Yalantis/Phoenix)

##联系我
- Github:   github.com/aspsine
- Email:    littleximail@gmail.com
- Linkedin: cn.linkedin.com/in/aspsine

##License

    Copyright 2015 Aspsine. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

