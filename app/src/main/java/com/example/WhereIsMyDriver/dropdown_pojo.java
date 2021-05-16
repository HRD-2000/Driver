package com.example.WhereIsMyDriver;

public class dropdown_pojo {
    public String driver_id,driver_profile_pic,driver_start_loc,driver_end_loc;

    public String getDriver_id() {
        return "Route "+ driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_profile_pic() {
        return driver_profile_pic;
    }

    public void setDriver_profile_pic(String driver_profile_pic) {
        this.driver_profile_pic = driver_profile_pic;
    }

    public String getDriver_start_loc() {
        return driver_start_loc;
    }

    public void setDriver_start_loc(String driver_start_loc) {
        this.driver_start_loc = driver_start_loc;
    }

    public String getDriver_end_loc() {
        return driver_end_loc;
    }

    public void setDriver_end_loc(String driver_end_loc) {
        this.driver_end_loc = driver_end_loc;
    }
}
