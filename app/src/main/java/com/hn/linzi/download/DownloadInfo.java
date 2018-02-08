package com.hn.linzi.download;
/**
 *鍒涘缓涓�涓笅杞戒俊鎭殑瀹炰綋绫�
 */
public class DownloadInfo {
    private int threadId;//涓嬭浇鍣╥d
    private int startPos;//寮�濮嬬偣
    private int endPos;//缁撴潫鐐�
    private int compeleteSize;//瀹屾垚搴�
    private String url;//涓嬭浇鍣ㄧ綉缁滄爣璇�
    public DownloadInfo(int threadId, int startPos, int endPos,
            int compeleteSize,String url) {
        this.threadId = threadId;
        this.startPos = startPos;
        this.endPos = endPos;
        this.compeleteSize = compeleteSize;
        this.url=url;
    }
    public DownloadInfo() {
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getThreadId() {
        return threadId;
    }
    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
    public int getStartPos() {
        return startPos;
    }
    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }
    public int getEndPos() {
        return endPos;
    }
    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }
    public int getCompeleteSize() {
        return compeleteSize;
    }
    public void setCompeleteSize(int compeleteSize) {
        this.compeleteSize = compeleteSize;
    }

    @Override
    public String toString() {
        return "DownloadInfo [threadId=" + threadId
                + ", startPos=" + startPos + ", endPos=" + endPos
                + ", compeleteSize=" + compeleteSize +"]";
    }
}
