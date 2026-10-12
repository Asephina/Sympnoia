# Sympnoia

一个 Android 应用。记下你今天做过的事，攒成「行动熵」，看着一朵斐波那契之花一天天开起来；
再加一个会主动找你说句话的 AI 伙伴。

## 它有什么

- **熵记**：记下一件事、写文章 / 日记、记录心情，各自换算成行动熵
- **行迹**：番茄时钟、待办、行动熵的统计与圆环
- **唤名**：轻唤名字，看花房那朵花按斐波那契的方式长出新的一瓣
- **AI 对话**：自带 API Key（任何 OpenAI 兼容接口），可自建人设卡、可开主动问候与主动互动
- **主题配色**：8 套配色，运行时可切换并记住
- **高度自定义**：连点顶栏标题五次进设置页，名字、称呼、语录、节日节气语录、
  刮刮乐各奖项寄语等等都能改

## 自己编译

需要 Android Studio（或 JDK 17 + Android SDK）。

```
gradlew assembleDebug
```

要出**发布版**得有你自己的一套签名。在项目根目录建 `keystore.properties`
（这个文件已被 `.gitignore` 排除，不会进仓库）：

```properties
storeFile=D:\\path\\to\\your.jks
storePassword=你的库密码
keyAlias=你的别名
keyPassword=你的密钥密码
```

然后：

```
gradlew assembleRelease
```

> 仓库里**不含**签名文件与密码。没有 `keystore.properties` 时只能编 debug 包 ——
> 这是故意的：签名密钥一旦泄露，别人就能签出跟你同名的安装包。

## 数据

- 应用不联网上传任何东西；所有内容（含 API Key、人设卡、记录）只存在手机本地的
  应用私有目录里。
- AI 相关功能需要你自己填写服务商的 Base URL / API Key / 模型名。

## 许可

见 [LICENSE](LICENSE)。
