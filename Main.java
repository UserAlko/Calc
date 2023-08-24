import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ArabicToRomanConverter {

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public static String toRoman(int number) {
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }
}

public class Main {

    private enum NumberFormat {
        Arabic,
        Roman
    }

    private static final Pattern pattern = Pattern.compile("^(\\w+)\\s*([\\+\\/\\*\\-])\\s*(\\w+)$");

    private static final Map<String, Integer> romanToArabicDictionary = new HashMap<>();

    public static void main(String[] args) {
        loadDictionary();

        Scanner sc = new Scanner(System.in);

        System.out.println("input: ");
        String input = sc.nextLine();

        System.out.println("output: ");

        System.out.println(calc(input));
    }

    private static void loadDictionary() {
        romanToArabicDictionary.put("I", 1);
        romanToArabicDictionary.put("II", 2);
        romanToArabicDictionary.put("III", 3);
        romanToArabicDictionary.put("IV", 4);
        romanToArabicDictionary.put("V", 5);
        romanToArabicDictionary.put("VI", 6);
        romanToArabicDictionary.put("VII", 7);
        romanToArabicDictionary.put("VIII", 8);
        romanToArabicDictionary.put("IX", 9);
        romanToArabicDictionary.put("X", 10);
    }

    private static String calc(String input) {
        String upperCaseInput = input.toUpperCase();

        if (!isCorrectInput(upperCaseInput)) {
            throw new IllegalArgumentException("Неправильная запись!");
        }

        Matcher matcher = pattern.matcher(upperCaseInput);

        matcher.find();

        String firstNumberAsString = matcher.group(1);

        String operation = matcher.group(2);

        String secondNumberAsString = matcher.group(3);

        NumberFormat numberFormat = getNumbersFormat(firstNumberAsString, secondNumberAsString);

        int firstNumber = 0;
        int secondNumber = 0;

        if (numberFormat == NumberFormat.Roman) {
            firstNumber = romanToArabicDictionary.get(firstNumberAsString);
            secondNumber = romanToArabicDictionary.get(secondNumberAsString);
        } else {
            firstNumber = Integer.parseInt(firstNumberAsString);
            secondNumber = Integer.parseInt(secondNumberAsString);
        }

        if (firstNumber < 1 || firstNumber > 10 || secondNumber < 1 || secondNumber > 10) {
            throw new IllegalArgumentException("Ожидались числа от 1-10");
        }

        int result = 0;

        switch (operation) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                result = firstNumber / secondNumber;
                break;
            default:
                throw new IllegalArgumentException("Неожиданный оператор!");
        }

        if (numberFormat == NumberFormat.Arabic) {
            return Integer.toString(result);
        } else {
            if (result <= 0) {
                throw new IllegalArgumentException("Римские цифры не могут быть отрицательными");
            }

            return ArabicToRomanConverter.toRoman(result);
        }
    }

    private static boolean isCorrectInput(String input) {
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    private static NumberFormat getNumbersFormat(String firstNumber, String secondName) {
        if (firstNumber.chars().allMatch(Character::isDigit)
                && secondName.chars().allMatch(Character::isDigit)) {
            return NumberFormat.Arabic;
        } else if (romanToArabicDictionary.containsKey(firstNumber)
                && romanToArabicDictionary.containsKey(secondName)) {
            return NumberFormat.Roman;
        } else {
            throw new IllegalArgumentException("Неправильная запись!");
        }
    }
}