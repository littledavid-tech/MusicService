package top.littledavid.musicservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import kotlin.concurrent.thread


/**
 * 用于音乐播放的服务
 * */
class MusicService : Service() {
    //与Service想关联的Binder
    private val mBinder = MusicBinder()
    //音乐列表
    private var singList: List<String>? = null
    //音乐的索引
    private var index = -1
    //表示音乐播放是否暂停
    private var paused = false
    //表示服务是否处于运行状态
    private var isRunning = false
    //播放事件的标记，每首歌为10秒
    var playingCountDown = 0

    override fun onBind(intent: Intent?): IBinder {
        return this.mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //开启线程播放音乐
        thread {
            while (isRunning) {
                if (paused) {
                    "Music playing has been paused!".logE()
                } else {
                    "${this.singList!![index]} are playing".logE()
                    playingCountDown++
                    //每十秒切换下一首音乐
                    if (playingCountDown >= 10) {
                        playingCountDown = 0
                        index++
                        if (index >= this.singList!!.size)
                            index = 0
                    }
                }
                //线程睡眠一秒
                SystemClock.sleep(1000)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    //当服务创建的时候初始化一些数据
    override fun onCreate() {
        super.onCreate()
        this.singList = listOf("My heart will go on", "清明上河图", "夜的钢琴曲 五", "彩云追月")
        this.index = 0
        this.isRunning = true
    }

    //当服务销毁的时候回收资源
    override fun onDestroy() {
        super.onDestroy()
        this.isRunning = false
        this.singList = null
    }

    inner class MusicBinder : Binder() {

        fun pause() {
            this@MusicService.paused = true
        }

        fun play() {
            this@MusicService.paused = false
        }

        fun next() {
            this@MusicService.index++
            if (this@MusicService.index >= this@MusicService.singList?.size!!) {
                this@MusicService.index = 0
            }
            this@MusicService.playingCountDown = 0
        }

        fun previous() {
            this@MusicService.index--
            if (this@MusicService.index < 0)
                index = this@MusicService.singList!!.size - 1
            this@MusicService.playingCountDown = 0
        }

        fun getSingInfo(): String {
            return "Current sing is ${singList!![index]}"
        }
    }
}