package com.physmo.garnet.graphics;


import org.junit.Test;

public class ObjectPoolTest {

    static int n = 1;

    @Test
    public void t1() {
        ObjectPool<String> objectPool = new ObjectPool<>(
                String.class,
                () -> "hello " + n++);

        System.out.println(objectPool);
        String s1 = objectPool.getFreeObject();
        System.out.println(objectPool);
        String s2 = objectPool.getFreeObject();
        System.out.println(objectPool);

        System.out.println();

        objectPool.releaseObject(s1);
        System.out.println(objectPool);
        objectPool.releaseObject(s2);
        System.out.println(objectPool);

        s1 = objectPool.getFreeObject();
        System.out.println(s1);
        System.out.println(objectPool);
    }
}