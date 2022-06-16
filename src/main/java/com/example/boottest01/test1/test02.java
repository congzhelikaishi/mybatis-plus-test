package com.example.boottest01.test1;

import java.util.Arrays;
import java.util.List;

public class test02 {
    public static void main(String[] args) {

        // 构造器引用Class::new，或者更一般的Class< T >::new
        final Car car = Car.create(Car::new);
        final List<Car> cars = Arrays.asList(car);

        // 静态方法引用：它的语法是Class::static_method
        cars.forEach(Car::collide);
        // 特定类的任意对象的方法引用：它的语法是Class::method
        cars.forEach(Car::repair);
        // 特定对象的方法引用：它的语法是instance::method
        cars.forEach(car::follow);

        Integer[] list = {1, 2, 3, 4, 5};
        List<Integer> lsit = Arrays.asList(list);
        lsit.forEach(System.out::println);

    }
}
