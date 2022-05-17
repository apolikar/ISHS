package irl.lyit.DublinSmartHouseSearch.service.isochroneMap;

public enum SearchAttributeHousePriceType {

    PRICE_100K("€100K", 100_000),
    PRICE_150K("€150K", 150_000),
    PRICE_200K("€200K", 200_000),
    PRICE_250K("€250K", 250_000),
    PRICE_300K("€300K", 300_000),
    PRICE_350K("€350K", 350_000),
    PRICE_450K("€450K", 450_000),
    PRICE_500K("€500K", 500_000),
    PRICE_550K("€550K", 550_000),
    PRICE_600K("€600K", 600_000),
    PRICE_650K("€650K", 650_000),
    PRICE_700K("€700K", 700_000),
    PRICE_750K("€750K", 750_000),
    PRICE_800K("€800K", 800_000),
    PRICE_850K("€850K", 850_000),
    PRICE_900K("€900K", 900_000),
    PRICE_950K("€950K", 950_000),
    PRICE_1M("€1M", 1_000_000),
    PRICE_1_5M("€1,5M", 1_500_000),
    PRICE_2M("€2M", 2_000_000),
    PRICE_3M("€3M", 3_000_000),
    PRICE_4M("€4M", 4_000_000),
    PRICE_5M("€5M", 5_000_000);

    private final String label;
    private final int amount;


    SearchAttributeHousePriceType(String label, int amount) {
        this.label = label;
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public int getAmount() {
        return amount;
    }
}
