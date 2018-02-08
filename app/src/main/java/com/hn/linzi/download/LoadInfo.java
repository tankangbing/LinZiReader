package com.hn.linzi.download;
/**
 *鑷畾涔夌殑涓�涓杞戒笅杞藉櫒璇︾粏淇℃伅鐨勭被 
 */
public class LoadInfo {
    public int fileSize;//鏂囦欢澶у皬
    private int complete;//瀹屾垚搴�
    private String urlstring;//涓嬭浇鍣ㄦ爣璇�
    public LoadInfo(int fileSize, int complete, String urlstring) {
        this.fileSize = fileSize;
        this.complete = complete;
        this.urlstring = urlstring;
    }
    public LoadInfo() {
    }
    public int getFileSize() {
        return fileSize;
    }
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
    public int getComplete() {
        return complete;
    }
    public void setComplete(int complete) {
        this.complete = complete;
    }
    public String getUrlstring() {
        return urlstring;
    }
    public void setUrlstring(String urlstring) {
        this.urlstring = urlstring;
    }
    @Override
    public String toString() {
        return "LoadInfo [fileSize=" + fileSize + ", complete=" + complete
                + ", urlstring=" + urlstring + "]";
    }
}
