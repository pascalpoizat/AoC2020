/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package aoc;

import java.util.List;

public class App {

    public static void main(String[] args) {
        List.of(Day1.instance(), Day2.instance(), Day3.instance(), Day4.instance(), Day5.instance(), Day6.instance(), Day7.instance(), Day8.instance(), Day9.instance(), Day10.instance(), Day11.instance())
                .forEach(Day::run);
    }
}
