package jug.lodz.workshop.javafp.effects.answers;

import javaslang.control.Option;
import jug.lodz.workshop.Checker;
import jug.lodz.workshop.javafp.effects.front.HTML;
import jug.lodz.workshop.javafp.effects.model.Customer;
import jug.lodz.workshop.javafp.effects.model.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static jug.lodz.workshop.javafp.effects.data.PurchaseData.data;

/**
 * Created by pawel on 28.04.16.
 */
public class EffectsPart2OptionalAnswer {


    public static void demo(){
        print("[DEMONSTRATION]");
        print(" * OPTIONAL");
        Optional<String> some = Optional.of("someValue");
        Optional<String> empty = Optional.empty();

        print("  *with value "+some);
        print("  *empty "+empty);

        print(" * OPTIONAL & NULL");

        print("  *possible null : some ="+Optional.ofNullable("notNull"));
        print("  *possible null : none ="+Optional.ofNullable(null));

        print(" * MAPPING IN OPTIONAL CONTEXT");
        Function<String,Integer> parse = Integer::parseInt;
        Function<Integer,Double> addTax= price -> price + (0.23*price);

        Function<String, Double> readGrossPrice = parse.andThen(addTax);

        print("  * Gross 10 : "+readGrossPrice.apply("10"));
        print("  * Gross 50 : "+readGrossPrice.apply("50"));

        Optional<String> potentialStringValue = Optional.of("10");
        print("  * Gross 10 from optional: "+ potentialStringValue.map(readGrossPrice));


        //Example with Wrapper class
        print(" * ENDING CALCULATION - STANDARD LIB");

        potentialStringValue.map(readGrossPrice).orElse(0.0);
        Supplier<Double> longCalculations=()->0.0;
        Double grossOrDefault = potentialStringValue.map(readGrossPrice).orElseGet(longCalculations);
        print("  * Some(Gross 10) : "+grossOrDefault);

        Double defaultFromEmpty = Optional.<String>empty().map(readGrossPrice).orElseGet(longCalculations);
        print("  * None : "+defaultFromEmpty);

        potentialStringValue.map(readGrossPrice).ifPresent(gross -> print("  * ifPresent "+gross));

        print("\n * OPTION -  JAVASLANG");
        Option<String> slangSome=Option.of("10");
        print("  * CREATION : "+slangSome);

        print("  * TO JAVA OPTIONAL : "+slangSome.toJavaOptional());
        print("  * FROM JAVA OPTIONAL : "+Option.ofOptional(potentialStringValue));


        Option.ofOptional(potentialStringValue)
                .orElse(Option.of("5"))
                .map(readGrossPrice)
                .forEach(gross->print("  * PARSED OPTION SOME "+gross));

        Option.ofOptional(Optional.<String>empty())
                .orElse(Option.of("5"))
                .map(readGrossPrice)
                .forEach(gross->print("  * PARSED OPTION NONE "+gross));
    }

    static void exercise1(){
        print("\n[EXERCISE1]");
        print("  * DEFINE OPTIONAL");

        Optional<String> someString=Optional.of("value");
        Optional<String> noneString=Optional.empty();

        String value=null;
        Optional<String> nullString=Optional.ofNullable(value);

        print("  * SOME : "+someString.get().equals("value"));
        print("  * NONE : "+(noneString.isPresent()==false));
        print("  * NULL : "+(nullString.isPresent()==false));

        Option<String> slangSome = Option.ofOptional(someString);
        Option<String> slangNone = Option.ofOptional(someString);

        print("  * SLANG SOME : " +slangSome.get().equals("value"));
        print("  * SLANG NONE : " +(slangNone.isDefined()==false));
    }

    static <A,B> Optional<B> findInMap(Map<A,B> map,A key){
        return Optional.ofNullable(map.get(key));
    }

    static void exercise2(){
        print("\n[EXERCISE2]");

        Map<String,String> sourceOne = new HashMap<String,String>(){{
            put("Joe","10.0");
        }};
        Map<String,String> sourceTwo = new HashMap<String,String>(){{
            put("Jane","30.0");
        }};

        Function<String,Double> parse=Double::parseDouble;
        Function<Double,Double> calculateTax=d->d*0.2;
        Function<String, Double> happyPath = parse.andThen(calculateTax);

        print("  * HAPPY PATH 10.0 : "+(happyPath.apply("10.0")==2.0));
        print("  * HAPPY PATH 30.0 : "+(happyPath.apply("30.0")==6.0));


        Optional<String> optionalJoe = findInMap(sourceOne, "Joe");
        Double joeResult = optionalJoe.map(happyPath).orElse(0.0);

        Optional<String> optionalJane = findInMap(sourceOne, "Jane");
        Double janeResult = Option.ofOptional(optionalJane)
                .orElse(Option.ofOptional(findInMap(sourceTwo, "Jane")))
                .map(happyPath)
                .getOrElse(0.0);

        Optional<String> optionalZygfryd = findInMap(sourceOne, "Zygfryd");
        Double zygfrydResult = Option.ofOptional(optionalZygfryd)
                .orElse(Option.ofOptional(findInMap(sourceTwo, "Zygfryd")))
                .map(happyPath)
                .getOrElse(0.0);


        Checker.check("OPTIONAL JOE",optionalJoe,Optional.of("10.0"));
        Checker.check("OPTIONAL JOE RESULT",joeResult,2.0);
        Checker.check("OPTIONAL JANE",optionalJane,Optional.empty());
        Checker.check("OPTIONAL JANE RESULT",janeResult,6.0);
        Checker.check("OPTIONAL ZYGFRYD",optionalZygfryd,Optional.empty());
        Checker.check("OPTIONAL ZYGFRYD RESULT",zygfrydResult,0.0);


    }

    static HTML displayProduct(Product product){
        Optional<String> optionalDescription = Optional.ofNullable(product.description);
        return HTML.html(product.name+ ":"+optionalDescription.orElse("NO DESCRIPTION AVAILABLE"));
    }

    public static void exercise3(){
        print("\n[EXERCISE3]");
        Checker.check("PRODUCT WITH DESCRIPTION",displayProduct(data().tv).content,"tv:Great Tv");
        Checker.check("PRODUCT WITHOUT DESCRIPTION",displayProduct(data().console).content,"console:NO DESCRIPTION AVAILABLE");
    }

    public static void exercise4(){
        print("\n[EXERCISE4]");
        Function<Set<Customer>,Function<Customer, Option<BigDecimal>>> discountsConfiguration = customersWithDiscount->
                customer -> customersWithDiscount.contains(customer.name)? Option.of(new BigDecimal("0.3")) : Option.none();

        Function<Customer, Option<BigDecimal>> discountForCustomer=discountsConfiguration.apply(new HashSet(Arrays.asList("Joe")));

        Function<String, Option<BigDecimal>> discountForACity = city ->
                city.equals("Lodz") ? Option.of(new BigDecimal("0.2")) : Option.none();


        //EXERCISE4
        // if there is discount for given customer - then return it
        //if there is not check if there is discount for city
        //if there is none of them then return zero
        Function<Customer,BigDecimal> calculateDiscount=c->
                discountForCustomer.apply(c).orElse(discountForACity.apply(c.city)).getOrElse(BigDecimal.ZERO);


        print("  * CUSTOMER JOE WITH DISCOUNT : "+calculateDiscount.apply(data().joe).equals(new BigDecimal("0.3")));
        print("  * CUSTOMER JANE WITH NO DISCOUNT : "+calculateDiscount.apply(data().jane).equals(BigDecimal.ZERO));
        print("  * CUSTOMER ZYGFRYD WITH CITY DISCOUNT : "
                +calculateDiscount.apply(new Customer(3L,"Zygfeyd","zygfryd@free.com","Lodz",30)).equals(new BigDecimal("0.2")));
//danger        print(calculateDiscount.apply(null));

    }
    public static void main(String[] args) {
        demo();

        print("\n\n[EXERCISES]");
        exercise1();
        exercise2();
        exercise3();
        exercise4();

    }


    static void print(Object s) {
        System.out.println(s);
    }

    static EffectsPart2OptionalAnswer part2(){return new EffectsPart2OptionalAnswer();}
}
