# 文转音 (Fish Audio)

使用 Fish Audio 的文本转语音 API 将文字转换为高质量的语音。

## 用法

### 1. 配置设置

- **音色**: Fish Audio 支持的语音模型，例如 `zh-male-1`、`zh-female-1` 等
- **API密钥**: 从 [Fish Audio 官网](https://www.fish.audio/) 获取你的 API Key
- **语速**: 语音播放速度，1.0 为正常速度（可选，默认为 1.0）

### 2. 命令

```
#tts 你好
```

### 3. 触发

在设置配置好音色和 API 密钥后，单击发送按钮即可发送文转音命令。

### 4. 结果

插件将自动合成语音并以 MP3 格式发送。

## Fish Audio API 参数说明

| 参数 | 说明 | 示例 |
|------|------|------|
| text | 需要转换的文本 | "你好" |
| voice | 语音模型 | "zh-male-1" |
| speed | 语速（可选） | 1.0 |
| audio_format | 音频格式 | "mp3" |

## 支持的音色

- `zh-male-1` - 中文男性声音1
- `zh-male-2` - 中文男性声音2
- `zh-female-1` - 中文女性声音1
- `zh-female-2` - 中文女性声音2
- 其他音色请查看 [Fish Audio 官方文档](https://www.fish.audio/)

## 获取 API 密钥

1. 访问 [Fish Audio 官网](https://www.fish.audio/)
2. 注册账号或登录
3. 进入开发者中心
4. 创建新的 API Key
5. 复制 API Key 到插件设置中

## 注意事项

- 请确保 API Key 有效且在配额范围内
- 文本长度过长时可能导致请求失败
- 语速范围通常为 0.5 - 2.0
