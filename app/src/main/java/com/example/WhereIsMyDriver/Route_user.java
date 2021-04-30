package com.example.WhereIsMyDriver;

import java.util.ArrayList;

public class Route_user {
    private static ArrayList<Route_user> Routes = new ArrayList<>();


    private String id;
    private String name;

    public Route_user(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void setRoutes(){
        com.example.WhereIsMyDriver.Route_user route_user1 = new com.example.WhereIsMyDriver.Route_user("0","Route1");
        Routes.add(route_user1);
        com.example.WhereIsMyDriver.Route_user route_user2 = new com.example.WhereIsMyDriver.Route_user("1","Route2");
        Routes.add(route_user2);
        com.example.WhereIsMyDriver.Route_user route_user3 = new com.example.WhereIsMyDriver.Route_user("2","Route3");
        Routes.add(route_user3);
        com.example.WhereIsMyDriver.Route_user route_user4 = new com.example.WhereIsMyDriver.Route_user("3","Route4");
        Routes.add(route_user4);
    }

    public int getImage()
    {
        switch (getId()){
            case "0":
                return R.drawable.profile;
            case "1":
                return R.drawable.profile;
            case "2":
                return R.drawable.profile;
            case "3":
                return R.drawable.profile;

        }
        return R.drawable.profile;
    }

    public static ArrayList<Route_user> getRoutes() {
        return Routes;
    }
}

