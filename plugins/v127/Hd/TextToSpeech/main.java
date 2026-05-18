
void openSettings() {
    var ctx = getTopActivity()

    var layout = new LinearLayout(ctx)
    layout.setOrientation(LinearLayout.VERTICAL)
    layout.setPadding(32, 32, 32, 0)

    var lpEdt = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    )
    lpEdt.setMargins(0, 16, 0, 16)

    var edtVoice = new EditText(ctx)
    edtVoice.setHint("请输入音色 (如: zh-male-1)")
    edtVoice.setText(getString("voice", ""))
    layout.addView(edtVoice, lpEdt)

    var edtKey = new EditText(ctx)
    edtKey.setHint("请输入Fish Audio API密钥")
    edtKey.setText(getString("key", ""))
    layout.addView(edtKey, lpEdt)

    var edtSpeed = new EditText(ctx)
    edtSpeed.setHint("语速 (1.0为正常, 可选)")
    edtSpeed.setText(getString("speed", "1.0"))
    edtSpeed.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL)
    layout.addView(edtSpeed, lpEdt)

    new AlertDialog.Builder(ctx)
            .setTitle("文转音设置 (Fish Audio)")
            .setView(layout)
            .setPositiveButton("保存", (dialog, which) -> {
                var voice = edtVoice.getText().toString().trim()
                putString("voice", voice)
                var key = edtKey.getText().toString().trim()
                putString("key", key)
                var speed = edtSpeed.getText().toString().trim()
                if (speed.equals("")) speed = "1.0"
                putString("speed", speed)
                toast("保存成功")
            })
            .setNegativeButton("取消", null)
            .show()
}

boolean onClickSendBtn(String text) {
    var cmd = "#tts "
    if (text.startsWith(cmd)) {
        var str = text.substring(cmd.length())
        var voice = getString("voice", "")
        var key = getString("key", "")
        var speed = getString("speed", "1.0")
        
        if (voice.equals("") || key.equals("")) {
            toast("请先配置音色及API密钥")
            return true
        }

        var headers = new java.util.HashMap<String, String>()
        headers.put("Authorization", "Bearer " + key)
        headers.put("Content-Type", "application/json")

        var jsonBody = new JSONObject()
        jsonBody.put("text", str)
        jsonBody.put("voice", voice)
        jsonBody.put("speed", Double.parseDouble(speed))
        jsonBody.put("audio_format", "mp3")

        post("https://api.fish.audio/v1/tts", headers, jsonBody.toString(), respContent -> {
            try {
                var jsonObj = new JSONObject(respContent)
                
                // Fish Audio 返回音频二进制流或URL
                if (jsonObj.has("data")) {
                    var audioUrl = jsonObj.optString("data")
                    if (!audioUrl.equals("")) {
                        var path = "${cacheDir}/voice.mp3"
                        download(audioUrl, path, null, file -> {
                            var talker = getTargetTalker()
                            sendVoice(talker, file.getAbsolutePath())
                        })
                    }
                } else if (jsonObj.has("audio_url")) {
                    var audioUrl = jsonObj.optString("audio_url")
                    var path = "${cacheDir}/voice.mp3"
                    download(audioUrl, path, null, file -> {
                        var talker = getTargetTalker()
                        sendVoice(talker, file.getAbsolutePath())
                    })
                } else {
                    toast("API 返回异常: " + respContent)
                }
            } catch (Exception e) {
                toast("错误: " + e.getMessage())
            }
        })
        return true
    }
    return false
}
