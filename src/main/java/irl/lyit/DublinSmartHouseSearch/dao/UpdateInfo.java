package irl.lyit.DublinSmartHouseSearch.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
public class UpdateInfo {

    @Id
    @GeneratedValue
    private Long id;
    private String updateStartTime;
    private String updateEndTime;
    private String totalUpdateTime;
    private long housesAddedToDatabase;


    public UpdateInfo(String updateStartTime, String updateEndTime, String totalUpdateTime, long housesAddedToDatabase) {
        this.updateStartTime = updateStartTime;
        this.updateEndTime = updateEndTime;
        this.totalUpdateTime = totalUpdateTime;
        this.housesAddedToDatabase = housesAddedToDatabase;
    }

    public UpdateInfo() {
    }
}
