package com.safetynetalert.projet5.model;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;
import java.util.Objects;

@JsonView(View.FilterFloodStations.class)
public class InfoByStation {
    private int station;
    private List<InfoByAddress> listInfo;

    public InfoByStation(List<InfoByAddress> listInfoByAddresses, int station) {
        this.listInfo = listInfoByAddresses;
        this.station = station;
    }

    public InfoByStation(int i, List<FullInfoPerson> emptyList) {

    }

    public List<InfoByAddress> getInfoByAddressList() {
        return listInfo;
    }

    public void setInfoByAddressList(List<InfoByAddress> listInfo) {
        this.listInfo = listInfo;
    }

    public int getStationNumber() {
        return station;
    }

    public void setStationNumber(int station) {
        this.station = station;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoByStation that = (InfoByStation) o;
        return station == that.station &&
                Objects.equals(listInfo, that.listInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(station, listInfo);
    }

    @Override
    public String toString() {
        return "InfoByStation{" +
                "listInfoByAddresses=" + listInfo +
                ", station=" + station +
                '}';
    }
}
