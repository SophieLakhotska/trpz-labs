package org.example.accounting.api.service.flyweight;

import java.util.HashMap;
import java.util.Map;

public class CategoryFlyweightFactory {
    private final Map<String, CategoryFlyweight> flyweights = new HashMap<>();

    public CategoryFlyweight getFlyweight(String name) {
        if (!flyweights.containsKey(name)) {
            flyweights.put(name, new CategoryFlyweight(name));
        }
        return flyweights.get(name);
    }

    public int getFlyweightCount() {
        return flyweights.size();
    }
}
