package jug.lodz.workshop.javafp.streams.exercises;

import javaslang.Tuple;
import javaslang.Tuple2;
import jug.lodz.workshop.Printer;
import jug.lodz.workshop.javafp.streams.Transactions;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by pawel on 07.06.16.
 */
public class StreamsPart2DataTransformationAndQueries implements Printer {

    private static StreamsPart2DataTransformationAndQueries lab = new StreamsPart2DataTransformationAndQueries();

    public static void main(String[] args) {
        lab.demo();
    }


    private void demo() {
        print("\n\nSTREAM DATA TRANSFORMATION\n\n");

        println("\n * SKIP HEADER AND DISPLAY THREE FIRST RECORDS");

        Transactions.transactions.stream()  // run bare version to check results
//                .skip(1)  // KATA
//                .limit(3)
                .forEach(this::println);

        println("\n * CONVERT TO DOMAIN OBJECTS");
        // enter readTransactions defined at the bottom
        // It uses csv simulation from Transactions.java
//        readTransactions().forEach(this::println);


        println("\n * ANY MATCH & ALL MATCH");
//        boolean anyIsGreaterThan600 = readTransactions().anyMatch(t -> t.amount > 600);  KATA
//        boolean allAreGreaterThan600 = readTransactions().allMatch(t->t.amount>600);
//
//        println("  * any greater than 600 : "+anyIsGreaterThan600);
//        println("  * all are greater than 600 : "+allAreGreaterThan600);

        println("\n * FIND RECORDS");
//        Optional<Transactions.FlatTransaction> any = readTransactions().findAny();   KATA
//        Optional<Transactions.FlatTransaction> first = readTransactions().findFirst();
//
//        println("any : "+any);     //CHANGe TO PARALLEL
//        println("first : "+first);



        println("\n * SORTING");

//        readTransactions()            //KATA
//                .sorted(Comparator.comparing(t->t.amount)) //reversed
//                .forEach(this::println);


        println("\n * FIND DISTINCT ACCOUNTS");

        println("  * only accountFrom");

//        readTransactions()            //KATA
//                .map(t->t.accountFrom)
//                .distinct()
//                .forEach(this::println);


        println("  * concat accountFrom and accountTo");

        Stream<Integer> streamFrom = readTransactions().map(t -> t.accountFrom);
        Stream<Integer> streamTo = readTransactions().map(t -> t.accountTo);

        Stream
                .concat(streamFrom,streamTo)
                .distinct()
                .forEach(this::println);


        println("\n * ANOTHER EXAMPLE WITH FLAT MAP - PAIRS");


//        List<Integer> firstList = Arrays.asList(1, 2, 3);              //KATA
//        List<Integer> secondList = Arrays.asList(3,4,5);
//
//        List<Tuple2<Integer,Integer>> pairs = firstList.stream()
//                .flatMap(
//                        i -> secondList.stream()
//                                .map(j -> Tuple.of(i,j))
//                ).collect(Collectors.toList());
//
//        pairs.forEach(this::println);


        println("\n * COUNT, MIN & MAX");
        //KATA
//        println("  * count : "+readTransactions().count());
//        println("  * max amount : "+readTransactions().map(t->t.amount).max(Integer::compare));
//        println("  * min amount : "+readTransactions().map(t->t.amount).min(Integer::compare));



    }

    //MENTION ABOUT EXERCISE WITH FLAT MAP
    private Stream<Transactions.FlatTransaction> readTransactions() {
        return Transactions.transactions.stream()
//                .skip(1)  // KATA
                .map(line -> line.split(","))
//                .filter(cels->cels.length==5) // KATA
//                .filter(cels->!cels[0].equals("aaa") && !cels[1].equals("bbb")) // KATA
//                .filter(cels->!cels[0].isEmpty())  // KATA
                .map(Transactions.FlatTransaction::new);   //domain object created
    }
}