package com.xt.mijkplayer.ui.video;

import static android.content.ContentValues.TAG;

import static com.xt.baselib.IVideoPlayer.formatedDurationMilli;
import static com.xt.baselib.IVideoPlayer.formatedSize;
import static com.xt.baselib.IVideoPlayer.formatedSpeed;
import static com.xt.mijkplayer.util.IPUtils.getIpAddress;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xt.baselib.listener.OnMediaPlayer;
import com.xt.mijkplayer.R;
import com.xt.mijkplayer.databinding.ActivityVideoBinding;
import com.xt.mijkplayer.util.BitmapUtil;
import com.xt.mijkplayer.util.ScanDeviceTool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by xiaolu on 2022/8/16
 * Email: xiaolu_008@126.com
 */
public class VideoActivity extends Activity implements OnMediaPlayer {
    private ActivityVideoBinding binding;
    private VideoListAdapter mAdapter;
    private List<VideoPlayerBean> videoList = new ArrayList<>();

    private Button rtsp_run;

    private TextView rtsp_url;

    private boolean need_stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestMyPermissions();
        initView();
        initListener();
    }

    private void requestMyPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            Log.d(TAG, "requestMyPermissions: 有写SD权限");
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            Log.d(TAG, "requestMyPermissions: 有读SD权限");
        }
    }

    private void initView() {
//        String url = "https://stream7.iqilu.com/10339/upload_transcode/202002/09/20200209105011F0zPoYzHry.mp4";
//        String url = "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4";
//        String url = "rtmp://ns8.indexforce.com/home/mystream";
        binding.videoView.setIsOpenKeep(true); // 是否开启流量监控
        binding.videoView.setVideoControlBar(false); // 设置控制条是否显示
        binding.videoView.initPlayer(VideoActivity.this, "This is video title");
//        nurVideoPlayer.setVideoPath(url);
//        nurVideoPlayer.setVideoPath(this, url, "This is video title");
//        nurVideoPlayer.setV

//        videoList.add(new VideoPlayerBean("rtsp 点击播放", "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4"));
//        videoList.add(new VideoPlayerBean("rtsp 点击播放", "rtsp://192.168.2.133:8555/live"));
//        videoList.add(new VideoPlayerBean("rtmp 点击播放", "rtsp://192.168.2.178:8554/iphone"));
//        videoList.add(new VideoPlayerBean("http 点击播放", "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4"));
//        videoList.add(new VideoPlayerBean("https 点击播放", "https://www.apple.com/105/media/us/iphone-x/2017/01df5b43-28e4-4848-bf20-490c34a926a7/films/feature/iphone-x-feature-tpl-cc-us-20170912_1920x1080h.mp4"));
        ScanDeviceTool scanDeviceTool = new ScanDeviceTool();
        List<String> ip_list = scanDeviceTool.scan();
        while(ip_list.size() > 0){
            videoList.add(new VideoPlayerBean("", ip_list.get(0)));
            ip_list.remove(0);
        }

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new VideoListAdapter(videoList);
        binding.recycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<VideoPlayerBean> data = (List<VideoPlayerBean>) adapter.getData();
            binding.videoView.setVideoPath(data.get(position).getPath());
            binding.videoView.start();
            need_stop = true;
        });

        rtsp_url = findViewById(R.id.rtsp_url);
        String ip = "rtsp://" +  getIpAddress(this) + ":8555/live";
        rtsp_url.setText(ip);
        rtsp_run = findViewById(R.id.rtsp_run);
        rtsp_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = rtsp_url.getText().toString();
                binding.videoView.setVideoPath(url);
                binding.videoView.start();
                need_stop = true;
            }
        });
        discover("192.168.1.1");

    }

    private void initListener() {
        binding.imgVolume.setOnClickListener(v -> {
            if (binding.videoView.getIsMute()) {
                binding.videoView.setMono(false);
                binding.imgVolume.setImageResource(R.drawable.video_sound);
            } else {
                binding.videoView.setMono(true);
                binding.imgVolume.setImageResource(R.drawable.video_unsound);
            }
        });

        binding.imgVideoWhite.setOnClickListener(v -> {

        });

        binding.imgVideoRecord.setOnClickListener(v -> {

        });

        binding.imgScreenShot.setOnClickListener(v -> {
//            if (!binding.videoView.isPlaying()) {
//                Toast.makeText(this,"视频未播放！",Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Bitmap bitmap = binding.videoView.getScreenShot(); // 获取视频控件的 Bitmap
//            if (bitmap == null)
//                return;
//            BitmapUtil.saveImageToGallery(this, bitmap);
//            Uri path = BitmapUtil.bitmapToUri(this, bitmap);
//            binding.imgScreenShot.setImageURI(path);
//            showPicAnim(binding.imgScreenShot);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    binding.imgScreenShot.setImageDrawable(null);
//                    binding.imgScreenShot.setOnClickListener(null);
//                }
//            }, 2000);
        });

        binding.ivSetUp.setOnClickListener(v -> {

        });
    }

    /**
     * 缩放动画
     *
     * @param v
     */
    public void showPicAnim(View v) {
        Animation animation = new ScaleAnimation(0, 1.0f, 0f, 1.0f);
        animation.setDuration(1000);//动画时间
        animation.setRepeatCount(0);//动画的反复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        v.startAnimation(animation);//開始动画
    }

    @Override
    public void onBackPressed() {
        if (binding.videoView.getIsFullScreen()) {
            binding.videoView.setChangeScreen(false);
        } else
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.videoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (need_stop)
            binding.videoView.stopPlay();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean b = binding.videoView.onKeyDown(keyCode);
        return b || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onMediaPlayer(IjkMediaPlayer mp) {
        if (mp == null)
            return;

        float fpsOutput = mp.getVideoOutputFramesPerSecond();
        float fpsDecode = mp.getVideoDecodeFramesPerSecond();
        Log.i("IMediaPlayer", String.valueOf(tv.danmaku.ijk.media.example.R.string.fps) + " " + String.format(Locale.US, "%.2f / %.2f", fpsDecode, fpsOutput));

        long videoCachedDuration = mp.getVideoCachedDuration();
        long audioCachedDuration = mp.getAudioCachedDuration();
        long videoCachedBytes = mp.getVideoCachedBytes();
        long audioCachedBytes = mp.getAudioCachedBytes();
        long tcpSpeed = mp.getTcpSpeed();
        long bitRate = mp.getBitRate();
        long seekLoadDuration = mp.getSeekLoadDuration();
        Log.i("IMediaPlayer", String.format(Locale.US, "%s, %s", String.valueOf(tv.danmaku.ijk.media.example.R.string.v_cache) + " " + formatedDurationMilli(videoCachedDuration), formatedSize(videoCachedBytes)));
        Log.i("IMediaPlayer", String.format(Locale.US, "%s, %s", String.valueOf(tv.danmaku.ijk.media.example.R.string.a_cache) + " " + formatedDurationMilli(audioCachedDuration), formatedSize(audioCachedBytes)));
        Log.i("IMediaPlayer", String.valueOf(tv.danmaku.ijk.media.example.R.string.seek_load_cost) + " " + String.format(Locale.US, "%d ms", seekLoadDuration));
        Log.i("IMediaPlayer", String.valueOf(tv.danmaku.ijk.media.example.R.string.tcp_speed) + " " + String.format(Locale.US, "%s", formatedSpeed(tcpSpeed, 1000)));
        Log.i("IMediaPlayer", String.valueOf(tv.danmaku.ijk.media.example.R.string.bit_rate) + " " + String.format(Locale.US, "%.2f kbs", bitRate / 1000f));


        TextView tvVdec = findViewById(R.id.tv_vdec);
        TextView tvFps = findViewById(R.id.tv_fps);
        TextView tvVCache = findViewById(R.id.tv_v_cache);
        TextView tvACache = findViewById(R.id.tv_a_cache);
//        TextView tvLoadCost = findViewById(R.id.tv_load_cost);
//        TextView tvSeekCost = findViewById(R.id.tv_seek_cost);
//        TextView tvSeekLoadCost = findViewById(R.id.tv_seek_load_cost);
//        TextView tvTcpSpeed = findViewById(R.id.tv_tcp_speed);
        TextView tvBitRate = findViewById(R.id.tv_bit_rate);

        int vdec = mp.getVideoDecoder();
        switch (vdec) {
            case IjkMediaPlayer.FFP_PROPV_DECODER_AVCODEC:
                tvVdec.setText("avcodec");
                break;
            case IjkMediaPlayer.FFP_PROPV_DECODER_MEDIACODEC:
                tvVdec.setText("MediaCodec");
                break;
            default:
                tvVdec.setText("");
                break;
        }

        tvFps.setText(String.format(Locale.US, "%.2f / %.2f", fpsDecode, fpsOutput));
        tvVCache.setText(String.format(Locale.US, "%s, %s", formatedDurationMilli(videoCachedDuration), formatedSize(videoCachedBytes)));
        tvACache.setText(String.format(Locale.US, "%s, %s", formatedDurationMilli(audioCachedDuration), formatedSize(audioCachedBytes)));
//        tvLoadCost.setText(String.format(Locale.US, "%d ms", 0));
//        tvSeekCost.setText(String.format(Locale.US, "%d ms", 0));
//        tvSeekLoadCost.setText(String.format(Locale.US, "%d ms", seekLoadDuration));
//        tvTcpSpeed.setText(String.format(Locale.US, "%s", formatedSpeed(tcpSpeed)));
        tvBitRate.setText(String.format(Locale.US, "%.2f kbs", bitRate / 1000f));
    }

    // UDPThread
    public class UDPThread extends Thread {
        private String target_ip = "";

        public final byte[] NBREQ = { (byte) 0x82, (byte) 0x28, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x1,
                (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x20, (byte) 0x43, (byte) 0x4B,
                (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41,
                (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41,
                (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41,
                (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x0, (byte) 0x0, (byte) 0x21, (byte) 0x0, (byte) 0x1 };

        public static final short NBUDPP = 137;

        public UDPThread(String target_ip) {
            this.target_ip = target_ip;
        }

        @Override
        public synchronized void run() {
            if (target_ip == null || target_ip.equals("")) return;
            DatagramSocket socket = null;
            InetAddress address = null;
            DatagramPacket packet = null;
            try {
                address = InetAddress.getByName(target_ip);
                packet = new DatagramPacket(NBREQ, NBREQ.length, address, NBUDPP);
                socket = new DatagramSocket();
                socket.setSoTimeout(200);
                socket.send(packet);
                socket.close();
            } catch (SocketException se) {
            } catch (UnknownHostException e) {
            } catch (IOException e) {
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
        }
    }
    // 根据ip 网段去 发送arp 请求
    private void discover(String ip) {
        String newip = "";
        if (!ip.equals("")) {
            String ipseg = ip.substring(0, ip.lastIndexOf(".")+1);
            for (int i=2; i<255; i++) {
                newip = ipseg+String.valueOf(i);
                if (newip.equals(ip)) continue;
                Thread ut = new UDPThread(newip);
                ut.start();
            }
        }
    }

    private void readArp() {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("/proc/net/arp"));
            String line = "";
            String ip = "";
            String flag = "";
            String mac = "";

            while ((line = br.readLine()) != null) {
                try {
                    line = line.trim();
                    if (line.length() < 63) continue;
                    if (line.toUpperCase(Locale.US).contains("IP")) continue;
                    ip = line.substring(0, 17).trim();
                    flag = line.substring(29, 32).trim();
                    mac = line.substring(41, 63).trim();
                    if (mac.contains("00:00:00:00:00:00")) continue;
                    Log.e("scanner", "readArp: mac= "+mac+" ; ip= "+ip+" ;flag= "+flag);


                } catch (Exception e) {
                }
            }
            br.close();

        } catch(Exception e) {
        }
    }


}
