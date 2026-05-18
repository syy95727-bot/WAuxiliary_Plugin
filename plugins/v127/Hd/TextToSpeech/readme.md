# 文转音 (Fish Audio)

## 功能介绍

使用 Fish Audio 高质量文本转语音 API，支持多种音色、语言和语速调节。

## 使用步骤

### 1. 获取 API 密钥

1. 访问 [Fish Audio 官网](https://fish.audio/)
2. 注册账户并登录
3. 进入开发者中心获取 API Key
4. 复制 API Key 备用

### 2. 插件配置

点击"设置"按钮，配置以下参数：

| 参数 | 说明 | 示例 |
|------|------|------|
| **音色** | 从下拉菜单选择预设音色 | zh-male-1 (中文男性-1) |
| **API密钥** | Fish Audio 的 API Key | your-api-key-here |
| **语速** | 语速调节 (0.5-2.0) | 1.0 (默认正常速度) |

### 3. 使用命令

在微信聊天框输入命令触发：

```
#tts 你好，欢迎使用Fish Audio
#tts Hello, welcome to Fish Audio
```

点击发送按钮后，机器人会自动生成语音并发送。

## 支持的音色

### 中文
- **男性**: zh-male-1, zh-male-2, zh-male-3
- **女性**: zh-female-1, zh-female-2, zh-female-3

### 英文
- **男性**: en-male-1
- **女性**: en-female-1

### 日文
- **男性**: ja-male-1
- **女性**: ja-female-1

> 更多音色可访问 [Fish Audio 音色库](https://fish.audio/voice-library/) 查看

## 参数说明

| 参数 | 范围 | 说明 |
|------|------|------|
| 语速 | 0.5 - 2.0 | 0.5为最慢，2.0为最快，1.0为正常速度 |
| 音色 | - | 系统预设音色列表 |

## 常见问题

### Q: 如何获得更多音色？
A: 可以访问 [Fish Audio 官网](https://fish.audio/voice-library/) 查看完整的音色库，并在官网上的设置中添加自定义音色。

### Q: 生成语音失败怎么办？
A: 
1. 检查 API Key 是否正确
2. 检查网络连接
3. 确认账户是否有足够的额度
4. 查看错误提示信息

### Q: 支持哪些语言？
A: Fish Audio 支持多种语言，您可以输入对应语言的文本，系统会自动识别并使用相应的音色生成语音。

## 更新日志

- **v1.1.0** (2026-05-18)
  - 改用 Fish Audio API
  - 添加音色下拉菜单选择功能
  - 支持语速调节 (0.5-2.0)
  - 改进错误处理和提示

## 相关链接

- [Fish Audio 官网](https://fish.audio/)
- [Fish Audio 文档](https://docs.fish.audio/)
- [Fish Audio 音色库](https://fish.audio/voice-library/)
