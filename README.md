# Puzzle
这是一个简单的Android版拼图小游戏
这个apk处理UI，还有一个apk存储数据。
字符串和数字全部提取到了资源文件。界面里含有计时器，但是不是用谷歌控件实现的，是通过handle发送消息达到1000毫秒自加1实现的
游戏实现了各种异常中断的处理，保证了功能正常实现。
游戏实现排行榜功能，不过没有区分不同难度，可以自己增加难度字段来作区分。
可以保存游戏进度，关机重启后任然可以继续游戏。
使用今日头条的适配工具，实现了不同分辨率手机的适配。
简单的制作了一个小部件。
新添加了不同难度的排行榜显示。
