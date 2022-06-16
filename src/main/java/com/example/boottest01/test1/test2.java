package com.example.boottest01.test1;

import java.util.function.Supplier;

public interface test2<T> {
    T get();
}

class Car {
    public static Car create(final test2<Car> test2) {
        return test2.get();
    }
    public static void collide(final Car car) {
        System.out.println("Collided " + car.toString());
    }
    public void follow(final Car another) {
        System.out.println("Following the " + another.toString());
    }
    public void repair() {
        System.out.println("Repaired " + this.toString());
    }
}
