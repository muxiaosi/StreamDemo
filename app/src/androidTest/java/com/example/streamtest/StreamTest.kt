package com.example.streamtest

import android.text.TextUtils
import org.junit.Test
import java.util.*
import java.util.function.BinaryOperator
import java.util.stream.*
import kotlin.Comparator
import kotlin.collections.ArrayList


/**
 * @author xxx
 * @date 2019-08-08 11:02
 * @description Stream 学习
 */
class StreamTest {

    class Person(
        val name: String,
        val sex: String,
        val age: Int,
        val inCome: Int
    ) {
        override fun toString(): String {
            return "Person(name='$name', sex='$sex', age=$age, inCome=$inCome)"
        }
    }

    @Test
    fun demoJava7() {
        val persons = arrayListOf<Person>()
        persons.add(Person("zsc", "nan", 13, 270000))
        persons.add(Person("fj", "nan", 23, 250000))
        persons.add(Person("csx", "nan", 19, 280000))
        persons.add(Person("xxx", "nv", 18, 240000))

        val curPersons = arrayListOf<Person>()
        for (i in 0 until persons.size) {
            if (TextUtils.equals(persons[i].sex, "nan")) {
                curPersons.add(persons[i])
            }
        }
        println("测试1：" + curPersons.toString())

        curPersons.sortWith(Comparator { o1, o2 -> o2.age.compareTo(o1.age) })
        println("测试2：" + curPersons.toString())
        val personList = arrayListOf<Int>()
        for (i in 0 until curPersons.size) {
            personList.add(curPersons[i].inCome)
        }
        println("测试3：" + personList.toString())
    }

    @Test
    fun demoJava8() {
        val persons = arrayListOf<Person>()
        persons.add(Person("zsc", "nan", 13, 270000))
        persons.add(Person("fj", "nan", 23, 250000))
        persons.add(Person("csx", "nan", 19, 280000))
        persons.add(Person("xxx", "nv", 18, 240000))

        val curPersons = persons.parallelStream()
            .filter { person -> TextUtils.equals(person.sex, "nan") }
            .sorted { o1, o2 -> o2.age.compareTo(o1.age) }
            .map { person -> person.inCome }
            .collect(Collectors.toList())
        println("测试：" + curPersons.toString())
    }

    /**
     * 构造Stream 的通常构造方式
     */
    @Test
    fun createStream() {
        //1.通过Stream.of() 方式构建
        val stream = Stream.of("a", "b", "c")
        //2.数组Arrays
        val strArrays = arrayOf("a", "b", "c")
        val stream1 = Stream.of(strArrays)
        val stream2 = Arrays.stream(strArrays)
        //3.通过集合创建
        val list = Arrays.asList(strArrays)
        val stream3 = list.stream()
        //并行流
        val parallelStream = list.parallelStream()
        //对于基本数据类型，目前还支持IntStream、LongStream、DoubleStream
        //换一种方式 Stream<Integer>、Stream<Long>、Stream<Double>
        var arrayOf = arrayOf(1, 2, 3, 4)
//        val stream4 = IntStream.of(arrayOf).forEach(System.out::println)
        val stream4 = IntStream.of(1, 2, 3, 4)
    }

    /**
     * 基础数据流的构建
     */
    @Test
    fun baseDataStream() {
        println("--------------")
        IntStream.of(1, 2, 3, 4).forEach(System.out::println)
        //打印结果是1、2、3、4
        println("------左闭右开--------")
        IntStream.range(1, 3).forEach(System.out::println)
        //打印结果是1、2、3
        println("-------左闭右闭-------")
        IntStream.rangeClosed(1, 6).forEach(System.out::println)
        //打印结果是1、2、3、4、5、6
        println("--------------")
        DoubleStream.of(1.2, 1.3, 1.4).forEach(System.out::println)
        //打印结果是1.2、 1.3、 1.4
        println("--------------")
        LongStream.of(1, 4).forEach(System.out::println)
        //打印结果是1、4
        println("------左闭右开--------")
        LongStream.range(1, 4).forEach(System.out::println)
        //打印结果是1、2、3
        println("------左闭右闭--------")
        LongStream.rangeClosed(1, 4).forEach(System.out::println)
        //打印结果是1、2、3、4
        println("--------------")
    }

    /**
     * map 操作符
     */
    @Test
    fun testMap() {
        val list = arrayListOf<String>("a", "B", "c")
        val collect = list.stream()
//            .map(String::toUpperCase)
            .map { str -> "D" }
            .collect(Collectors.toList())
        println("----:" + collect.toString())
    }

    /**
     * flatMap 操作符
     * flatMap 把多个Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，最终output的新Stream
     * 里面已经没有list了
     */
    @Test
    fun testFlatMap2() {
        val stream = Stream.of(
            Arrays.asList(1),
            Arrays.asList(2, 3),
            Arrays.asList(4, 5, 6)
        )
        stream.flatMap { childList -> childList.stream() }
            .map { t ->
                if (t >= 6) {
                    7
                } else {
                    t
                }
            }
            .forEach(System.out::println)
    }

    @Test
    fun testFlatMap() {
        val list1 = arrayListOf<Person>()
        list1.add(Person("zsc", "nan", 13, 270000))
        list1.add(Person("fj", "nan", 23, 250000))
        list1.add(Person("csx", "nan", 19, 280000))
        list1.add(Person("xxx", "nv", 18, 240000))
        val list2 = arrayListOf<Person>()
        list2.add(Person("ll", "nan", 15, 270000))
        list2.add(Person("jj", "nan", 33, 250000))
        list2.add(Person("ss", "nan", 14, 280000))
        list2.add(Person("sh", "nv", 58, 240000))
        val list3 = arrayListOf<Person>()
        list3.add(Person("mm", "nan", 15, 27000))
        list3.add(Person("ii", "nan", 33, 25000))
        list3.add(Person("poo", "nan", 14, 2000))
        list3.add(Person("shs", "nv", 58, 2400))
        val list4 = arrayListOf<ArrayList<Person>>()
        list4.addAll(listOf(list1))
        list4.addAll(listOf(list2))
        list4.addAll(listOf(list3))
        //把Stream中的层级结构扁平化并返回Stream
        val stream = list4.stream()
            .flatMap { childList -> childList.stream() }
            .collect(Collectors.toList())
        //展开多个List合并到一个新list
        stream.stream()
            .forEach(System.out::println)
    }

    /**
     * filter 操作符
     */
    @Test
    fun testFilter() {
        val list = Stream.of(1, 2, 3, 4, 5, 6)
            .filter { n -> n % 2 == 0 }
            .collect(Collectors.toList())
        println("测试：" + list)
    }

    /**
     * peek 操作符
     * 功能与map相似，但是对每个元素执行操作,不影响原来数据操作
     */
    @Test
    fun testPeek() {
        val arrayListOf = arrayListOf<Int>()
        val list = Stream.of(1, 2, 3, 4, 5)
            .filter { n -> n > 3 }
            .peek {
                it + 2
                arrayListOf.add(it + 2)
                println("it:" + it)
            }
            .map { k -> k * k }
            .peek { e -> System.out.println(e) }
            .collect(Collectors.toList())
        println("测试：" + list)
        println("测试：" + arrayListOf)
    }

    /**
     * reduce 操作符
     */
    @Test
    fun testReduce() {
        val reduce = Stream.of(2, 2, 3, 4)
            .reduce(1, BinaryOperator { t, u ->
                println("t:" + t + "  u:" + u)
                t * u
            })
        val reduce1 = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum)
        //如果没有给初始化，返回的是Optional
        val reduce2 = Stream.of(1, 2, 3, 4).reduce(Integer::sum)
        println("测试：" + reduce)
        println("测试：" + reduce1)
        println("测试：" + reduce2.get())
    }

    /**
     * findFirst 操作符
     * findFirst 查找的第一个值不能是null，否则报空NullPointException
     */
    @Test
    fun testFindFirst() {
        val list = arrayListOf<Any?>()
        list.add("1")
        list.add(null)
        list.add("1")
//        val optional = Stream.of("", "one", "two", "three").findFirst()
        val optional = list.stream().findFirst()
        val value = Optional.ofNullable(optional)
            .map { str -> str.get() }
            .orElse("str is null")
        println("测试：" + value)
    }

    /**
     * limit 操作符
     */
    @Test
    fun testLimit() {
        val list = Stream.of(2, 1, 3, 6, 7, 8, 4, 5, 9)
            .limit(3)
            .peek(System.out::println)
            .collect(Collectors.toList())
        println("测试：" + list)
    }

    /**
     * skip 操作符
     */
    @Test
    fun testSkip() {
        val list = Stream.of(2, 1, 3, 6, 7, 8, 4, 5, 9)
            .skip(3)
            .peek(System.out::println)
            .collect(Collectors.toList())
        println("测试：" + list)
    }

    /**
     * sorted 操作符
     */
    @Test
    fun testSorted() {
        val persons = arrayListOf<Person>()
        persons.add(Person("zsc", "nan", 13, 17000))
        persons.add(Person("fj", "nan", 23, 15000))
        persons.add(Person("xsd", "nv", 18, 160000))
        persons.add(Person("csx", "nan", 19, 18000))
        persons.add(Person("xxx", "nv", 18, 14000))
        val curPersons = persons.parallelStream()
            .sorted { o1, o2 -> o2.age.compareTo(o1.age) }
            .collect(Collectors.toList())
        println("测试：" + curPersons.toString())
    }

    /**
     * match 相关操作符
     */
    @Test
    fun testMatch() {
        val allMatch = Stream.of(3, 4, 5)
            .allMatch { n -> n > 4 }
        println("测试1：" + allMatch)
        val list = arrayListOf<String>()
        val allMat = list.stream().allMatch {
            TextUtils.equals(it, "c")
        }
        println("测试2：" + allMat)
        val anyMatch = Stream.of(3, 4, 5)
            .anyMatch { n -> n > 4 }
        println("测试3：" + anyMatch)
        val anyMatch1 = list.stream()
            .anyMatch {
                TextUtils.equals(it, "c")
            }
        println("测试4：" + anyMatch1)
        val noneMatch = Stream.of(3, 4, 5)
            .noneMatch { n -> n > 4 }
        println("测试5：" + noneMatch)
        val noneMatch1 = list.stream()
            .noneMatch {
                TextUtils.equals(it, "c")
            }
        println("测试6：" + noneMatch1)
    }
}