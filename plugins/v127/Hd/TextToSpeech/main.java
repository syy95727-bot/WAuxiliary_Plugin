
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

    // 音色下拉菜单
    var spinnerVoice = new Spinner(ctx)
    var voiceList = java.util.Arrays.asList(
            "zh-male-1 (中文男性-1)",
            "zh-male-2 (中文男性-2)",
            "zh-male-3 (中文男性-3)",
            "zh-female-1 (中文女性-1)",
            "zh-female-2 (中文女性-2)",
            "zh-female-3 (中文女性-3)",
            "en-male-1 (英文男性-1)",
            "en-female-1 (英文女性-1)",
            "ja-male-1 (日文男性-1)",
            "ja-female-1 (日文女性-1)"
    )
    var adapter = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, voiceList)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinnerVoice.setAdapter(adapter)
    
    // 恢复之前选择的音色
    var savedVoice = getString("voice", "zh-male-1 (中文男性-1)")
    for (var i = 0; i < voiceList.size(); i++) {
        if (voiceList.get(i).startsWith(savedVoice.split(" ")[0])) {
            spinnerVoice.setSelection(i)
            break
        }
    }
    
    layout.addView(spinnerVoice, lpEdt)

    // API 密钥输入框
    var edtKey = new EditText(ctx)
    edtKey.setHint("请输入Fish Audio API密钥")
    edtKey.setText(getString("key", ""))
    layout.addView(edtKey, lpEdt)

    // 语速输入框
    var edtSpeed = new EditText(ctx)
    edtSpeed.setHint("语速 (0.5-2.0, 默认1.0)")
    edtSpeed.setText(getString("speed", "1.0"))
    edtSpeed.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL)
    layout.addView(edtSpeed, lpEdt)

    new AlertDialog.Builder(ctx)
            .setTitle("文转音设置 (Fish Audio)")
            .setView(layout)
            .setPositiveButton("保存", (dialog, which) -> {
                var selectedVoice = voiceList.get(spinnerVoice.getSelectedItemPosition())
                // 只保存音色代码部分 (如: zh-male-1)
                var voiceCode = selectedVoice.split(" ")[0]
                putString("voice", voiceCode)
                putString("voiceDisplay", selectedVoice)
                
                var key = edtKey.getText().toString().trim()
                putString("key", key)
                
                var speed = edtSpeed.getText().toString().trim()
                if (speed.equals("")) speed = "1.0"
                try {
                    var speedValue = Double.parseDouble(speed)
                    if (speedValue < 0.5 || speedValue > 2.0) {
                        toast("语速需要在0.5-2.0之间")
                        return
                    }
                    putString("speed", speed)
                } catch (Exception e) {
                    toast("语速格式错误")
                    return
                }
                
                if (key.equals("")) {
                    toast("API密钥不能为空")
                    return
                }
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

        // 显示正在处理中
        toast("正在生成语音...")
        
        post("https://api.fish.audio/v1/tts", headers, jsonBody.toString(), respContent -> {
            try {
                var jsonObj = new JSONObject(respContent)
                
                // Fish Audio 返回音频数据或URL
                if (jsonObj.has("data")) {
                    var audioUrl = jsonObj.optString("data")
                    if (!audioUrl.equals("")) {
                        var path = "${cacheDir}/voice.mp3"
                        download(audioUrl, path, null, file -> {
                            var talker = getTargetTalker()
                            sendVoice(talker, file.getAbsolutePath())
                            toast("语音已发送")
                        })
                    }
                } else if (jsonObj.has("audio_url")) {
                    var audioUrl = jsonObj.optString("audio_url")
                    var path = "${cacheDir}/voice.mp3"
                    download(audioUrl, path, null, file -> {
                        var talker = getTargetTalker()
                        sendVoice(talker, file.getAbsolutePath())
                        toast("语音已发送")
                    })
                } else {
                    var errMsg = jsonObj.optString("message", jsonObj.optString("error", "未知错误"))
                    toast("生成失败: " + errMsg)
                }
            } catch (Exception e) {
                toast("错误: " + e.getMessage())
            }
        })
        return true
    }
    return false
}
