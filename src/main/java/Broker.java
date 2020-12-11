package main.java;

import java.util.Map;
import java.util.TreeMap;

public class Broker {
    private final TreeMap<Integer, Integer> asks = new TreeMap<>();
    private final TreeMap<Integer, Integer> bids = new TreeMap<>();

    public void updateBook(String[] line) {
        if (line.length == 4) {
            int price = Integer.parseInt(line[1]);
            int size = Integer.parseInt(line[2]);
            String type = line[3];
            switch (type) {
                case "ask" -> {
                    if (size > 0) {
                        asks.put(price, size);
                    } else {
                        asks.remove(price);
                    }
                }
                case "bid" -> {
                    if (size > 0) {
                        bids.put(price, size);
                    } else {
                        bids.remove(price);
                    }
                }
            }
        }

    }

    public void orderAndUpdateBook(String[] line) {
        if (line.length == 3) {
            String type = line[1];
            int size = Integer.parseInt(line[2]);
            switch (type) {
                case "buy" -> {
                    while (size > 0) {
                        size -= asks.firstEntry().getValue();
                        if (size >= 0) {
                            asks.remove(asks.firstKey());
                        } else {
                            asks.replace(asks.firstKey(), size * -1);
                        }
                    }
                }
                case "sell" -> {
                    while (size > 0) {
                        size -= bids.lastEntry().getValue();
                        if (size >= 0) {
                            bids.remove(bids.lastKey());
                        } else {
                            bids.replace(bids.lastKey(), size * -1);
                        }
                    }
                }
            }
        }
    }

    public void respondToQuery(String[] line, StringBuilder sb) {
        if (line.length == 3 && "size".equals(line[1])) {
            int price = Integer.parseInt(line[2]);
            int size = asks.containsKey(price) ? asks.get(price)
                    : bids.getOrDefault(price, 0);

            sb.append(size);
            sb.append(System.lineSeparator());

        } else if (line.length == 2) {
            String goal = line[1];
            int price = 0;
            int size = 0;
            switch (goal) {
                case "best_ask" -> {
                    for (Map.Entry<Integer, Integer> entry : asks.entrySet()) {
                        if ((size = entry.getValue()) > 0) {
                            price = entry.getKey();
                            break;
                        }
                    }
                }

                case "best_bid" -> {
                    if (!bids.isEmpty()) {
                        Integer key = bids.lastKey();
                        while (size == 0) {
                            size = bids.get(key);
                            price = size > 0 ? key : 0;
                            if ((key = bids.lowerKey(key)) == null) {
                                break;
                            }
                        }
                    }
                }
            }

            sb.append(price);
            sb.append(",");
            sb.append(size);
            sb.append(System.lineSeparator());
        }
    }
}
