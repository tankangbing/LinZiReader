package com.hn.linzi.data;

/**
 * Created by Administrator on 2018/2/23 0023.
 */

public class LocalData {

    /**
     * result : 请求成功
     * status : 0
     * coordinateLatitude : 23.130001
     * coordinateLongitude : 113.326644
     * agent_url : null
     * hospitalName : 广州市妇女儿童医疗中心珠江新城院区
     */

    private String result;
    private String status;
    private String coordinateLatitude;
    private String coordinateLongitude;
    private Object agent_url;
    private String hospitalName;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoordinateLatitude() {
        return coordinateLatitude;
    }

    public void setCoordinateLatitude(String coordinateLatitude) {
        this.coordinateLatitude = coordinateLatitude;
    }

    public String getCoordinateLongitude() {
        return coordinateLongitude;
    }

    public void setCoordinateLongitude(String coordinateLongitude) {
        this.coordinateLongitude = coordinateLongitude;
    }

    public Object getAgent_url() {
        return agent_url;
    }

    public void setAgent_url(Object agent_url) {
        this.agent_url = agent_url;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
