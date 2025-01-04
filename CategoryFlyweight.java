package org.example.accounting.api.service.flyweight;

public class CategoryFlyweight {
    private final String name;

    public CategoryFlyweight(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
