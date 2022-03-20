package irl.lyit.DublinSmartHouseSearch.old;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        UserHouseSearch userHouseSearch = new UserHouseSearch(300000, 400000, 3, 4);
        userHouseSearch.searchHouseInBoundingBox();
    }
}
