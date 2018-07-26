package top.littledavid.musicservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var mBinder: MusicService.MusicBinder? = null

    var serviceConn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinder = service as MusicService.MusicBinder
        }
    }

    var isShutDown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnList = listOf<Int>(R.id.prepareBtn, R.id.pauseBtn, R.id.previousBtn, R.id.nextBtn, R.id.getInfoBtn, R.id.shutDownBtn)
        btnList.forEach {
            this.findViewById<Button>(it).setOnClickListener(this)
        }
    }

    //各个按钮的点击事件
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.prepareBtn -> {//准备播放
                val serviceIntent = Intent(this, MusicService::class.java)
                this.startService(serviceIntent)
                this.bindService(serviceIntent, this.serviceConn, Context.BIND_AUTO_CREATE)
            }
            R.id.pauseBtn -> { //开始 or 暂停播放
                //开始播放
                if (pauseBtn.text == "Start") {
                    mBinder!!.play()
                    pauseBtn.text = "Pause"
                } else {
                    mBinder!!.pause()
                    pauseBtn.text = "Start"
                }
            }
            R.id.previousBtn -> { //上一首
                mBinder!!.previous()
            }
            R.id.nextBtn -> {   //下一首
                mBinder!!.next()
            }
            R.id.getInfoBtn -> {
                mBinder!!.getSingInfo() show this
            }
            R.id.shutDownBtn -> {
                this.stopService(Intent(this, MusicService::class.java))
                this.unbindService(serviceConn)
                mBinder = null

                this.isShutDown = true
            }
        }
    }

    //回收mBinder
    override fun onDestroy() {
        super.onDestroy()
        if (!isShutDown) {
            this.unbindService(serviceConn)
            this.mBinder = null
        }
    }
}
