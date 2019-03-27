package com.yihexinda.core.response;

import java.util.ArrayList;
import java.util.Map;

/**
 * 平峰返回的车辆路线信息，包含站点和站点link
 */
public class RouteListObj {
    public String vehicleID;
    public ArrayList<String> stationList;
    public Map<String,ArrayList<String>> linkMap;


    public RouteListObj(){}

    /**
     *
     * @param vehicleID:车辆ID
     * @param stationList：站点顺序
     * @param linkMap：link顺序
     */
    public RouteListObj(String vehicleID, ArrayList<String> stationList, Map<String, ArrayList<String>> linkMap) {
        this.vehicleID = vehicleID;
        this.stationList = stationList;
        this.linkMap = linkMap;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public ArrayList<String> getStationList() {
        return stationList;
    }

    public void setStationList(ArrayList<String> stationList) {
        this.stationList = stationList;
    }

    public Map<String, ArrayList<String>> getLinkMap() {
        return linkMap;
    }

    public void setLinkMap(Map<String, ArrayList<String>> linkMap) {
        this.linkMap = linkMap;
    }

    @Override
    public String toString() {
        return "RouteListObj{" +
                "vehicleID='" + vehicleID + '\'' +
                ", stationList=" + stationList +
                ", linkMap=" + linkMap +
                '}';
    }
}
