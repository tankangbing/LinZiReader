package com.hn.linzi.download;
 
 import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
 import android.os.Handler;
import android.os.Message;
import android.util.Log;
 
public class Downloader {
    private String urlstr;// 涓嬭浇鐨勫湴鍧�
    private String localfile;// 淇濆瓨璺緞
    private int threadcount;// 绾跨▼鏁�
    private Handler mHandler;// 娑堟伅澶勭悊鍣� 
    private int fileSize;// 鎵�瑕佷笅杞界殑鏂囦欢鐨勫ぇ灏�
    private Context context; 
    private List<DownloadInfo> infos;// 瀛樻斁涓嬭浇淇℃伅绫荤殑闆嗗悎
    private static final int INIT = 1;//瀹氫箟涓夌涓嬭浇鐨勭姸鎬侊細鍒濆鍖栫姸鎬侊紝姝ｅ湪涓嬭浇鐘舵�侊紝鏆傚仠鐘舵��
    private static final int DOWNLOADING = 2;
    private static final int PAUSE = 3;
    private int state = INIT;

    public Downloader(String urlstr, String localfile, int threadcount,
            Context context, Handler mHandler) {
        this.urlstr = urlstr;
        this.localfile = localfile;
        this.threadcount = threadcount;
        this.mHandler = mHandler;
        this.context = context;
    }
    /**
     *鍒ゆ柇鏄惁姝ｅ湪涓嬭浇 
     */
    public boolean isdownloading() {
        return state == DOWNLOADING;
    }
    /**
     * 寰楀埌downloader閲岀殑淇℃伅
     * 棣栧厛杩涜鍒ゆ柇鏄惁鏄涓�娆′笅杞斤紝濡傛灉鏄涓�娆″氨瑕佽繘琛屽垵濮嬪寲锛屽苟灏嗕笅杞藉櫒鐨勪俊鎭繚瀛樺埌鏁版嵁搴撲腑
     * 濡傛灉涓嶆槸绗竴娆′笅杞斤紝閭ｅ氨瑕佷粠鏁版嵁搴撲腑璇诲嚭涔嬪墠涓嬭浇鐨勪俊鎭紙璧峰浣嶇疆锛岀粨鏉熶负姝紝鏂囦欢澶у皬绛夛級锛屽苟灏嗕笅杞戒俊鎭繑鍥炵粰涓嬭浇鍣�
     */
    public LoadInfo getDownloaderInfors() {
        if (isFirst(urlstr)) {
            Log.v("TAG", "isFirst");
            init();
            int range = fileSize / threadcount;
            infos = new ArrayList<DownloadInfo>();
            for (int i = 0; i < threadcount - 1; i++) {
                DownloadInfo info = new DownloadInfo(i, i * range, (i + 1)* range - 1, 0, urlstr);
                infos.add(info);
            }
            DownloadInfo info = new DownloadInfo(threadcount - 1,(threadcount - 1) * range, fileSize - 1, 0, urlstr);
            infos.add(info);
            //淇濆瓨infos涓殑鏁版嵁鍒版暟鎹簱
            Dao.getInstance(context).saveInfos(infos);
            //鍒涘缓涓�涓狶oadInfo瀵硅薄璁拌浇涓嬭浇鍣ㄧ殑鍏蜂綋淇℃伅
            LoadInfo loadInfo = new LoadInfo(fileSize, 0, urlstr);
            return loadInfo;
        } else {
            //寰楀埌鏁版嵁搴撲腑宸叉湁鐨剈rlstr鐨勪笅杞藉櫒鐨勫叿浣撲俊鎭�
            infos = Dao.getInstance(context).getInfos(urlstr);
            Log.v("TAG", "not isFirst size=" + infos.size());
            int size = 0;
            int compeleteSize = 0;
            for (DownloadInfo info : infos) {
                compeleteSize += info.getCompeleteSize();
                size += info.getEndPos() - info.getStartPos() + 1;
            }
            return new LoadInfo(size, compeleteSize, urlstr);
        }
    }

    /**
     * 鍒濆鍖�
     */
    private void init() {
        try {
            URL url = new URL(urlstr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            fileSize = connection.getContentLength();

            File file = new File(localfile);
            if (!file.exists()) {
                file.createNewFile();
            }
            // 鏈湴璁块棶鏂囦欢
            RandomAccessFile accessFile = new RandomAccessFile(file, "rws");
            accessFile.setLength(fileSize);
            accessFile.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  
    /**
     * 鍒ゆ柇鏄惁鏄涓�娆� 涓嬭浇
     */
    private boolean isFirst(String urlstr) {
        return Dao.getInstance(context).isHasInfors(urlstr);
    }

    /**
     * 鍒╃敤绾跨▼寮�濮嬩笅杞芥暟鎹�
     */
    public void download() {
        if (infos != null) {
            if (state == DOWNLOADING)
                return;
            state = DOWNLOADING;
            for (DownloadInfo info : infos) {
                new MyThread(info.getThreadId(), info.getStartPos(),
                        info.getEndPos(), info.getCompeleteSize(),
                        info.getUrl()).start();
            }
        }
    }
    public class MyThread extends Thread {
        private int threadId;
        private int startPos;
        private int endPos;
        private int compeleteSize;
        private String urlstr;

        public MyThread(int threadId, int startPos, int endPos,
                int compeleteSize, String urlstr) {
            this.threadId = threadId;
            this.startPos = startPos;
            this.endPos = endPos;
            this.compeleteSize = compeleteSize;
            this.urlstr = urlstr;
        }
        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile randomAccessFile = null;
            InputStream is = null;
            try {
                URL url = new URL(urlstr);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                // 璁剧疆鑼冨洿锛屾牸寮忎负Range锛歜ytes x-y;
                connection.setRequestProperty("Range", "bytes="+(startPos + compeleteSize) + "-" + endPos);

                randomAccessFile = new RandomAccessFile(localfile, "rws");
                randomAccessFile.seek(startPos + compeleteSize);
                // 灏嗚涓嬭浇鐨勬枃浠跺啓鍒颁繚瀛樺湪淇濆瓨璺緞涓嬬殑鏂囦欢涓�
                is = connection.getInputStream();
                byte[] buffer = new byte[4096];
                int length = -1;
                while ((length = is.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, length);
                    compeleteSize += length;
                    // 鏇存柊鏁版嵁搴撲腑鐨勪笅杞戒俊鎭�
                    Dao.getInstance(context).updataInfos(threadId, compeleteSize, urlstr);
                    // 鐢ㄦ秷鎭皢涓嬭浇淇℃伅浼犵粰杩涘害鏉★紝瀵硅繘搴︽潯杩涜鏇存柊
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = urlstr;
                    message.arg1 = length;
                    mHandler.sendMessage(message);
                    if (state == PAUSE) {
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }  
        }
    }
    //鍒犻櫎鏁版嵁搴撲腑urlstr瀵瑰簲鐨勪笅杞藉櫒淇℃伅
    public void delete(String urlstr) {
   	 Dao.getInstance(context).delete(urlstr);
    }
    //璁剧疆鏆傚仠
    public void pause() {
        state = PAUSE;
    }
    //閲嶇疆涓嬭浇鐘舵��
    public void reset() {
        state = INIT;
    }
}
