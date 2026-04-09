package common.util;

import common.wrapper.Option;
import common.base.Selectable;

import java.util.HashMap;
import java.util.Scanner;

/**
 * A simple terminal interface for user interaction.
 */
public class Terminal {

    public static final String RESET   = "\u001B[0m";
    public static final String BOLD    = "\u001B[1m";
    public static final String DIM     = "\u001B[2m";
    public static final String ITALIC  = "\u001B[3m";

    public static final String BLACK   = "\u001B[30m";
    public static final String RED     = "\u001B[31m";
    public static final String GREEN   = "\u001B[32m";
    public static final String YELLOW  = "\u001B[33m";
    public static final String BLUE    = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN    = "\u001B[36m";
    public static final String WHITE   = "\u001B[37m";

    private static final int WIDTH = 72;
    private static final Scanner scanner = new Scanner(System.in);

    private static final char LINE_HEAVY = '=';
    private static final char LINE_LIGHT = '-';

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printHeader(String title) {
        System.out.println();
        printLine(LINE_HEAVY);
        int pad = (WIDTH - title.length()) / 2;
        System.out.println(BOLD + CYAN + " ".repeat(Math.max(0, pad)) + title + RESET);
        printLine(LINE_HEAVY);
    }

    public static void printSubHeader(String title) {
        System.out.println();
        System.out.println(BOLD + YELLOW + "  > " + title + RESET);
        printLine(LINE_LIGHT);
    }

    public static void printLine(char ch) {
        System.out.println(DIM + String.valueOf(ch).repeat(WIDTH) + RESET);
    }

    public static void printLine() { printLine(LINE_LIGHT); }

    public static void println()                  { System.out.println(); }
    public static void println(String s)          { System.out.println("  " + s); }
    public static void printRaw(String s)         { System.out.println(s); }

    public static void printSuccess(String msg)   { System.out.println("  " + GREEN  + "[OK]  " + msg + RESET); }
    public static void printError(String msg)     { System.out.println("  " + RED    + "[X]   " + msg + RESET); }
    public static void printWarning(String msg)   { System.out.println("  " + YELLOW + "[!]   " + msg + RESET); }
    public static void printInfo(String msg)      { System.out.println("  " + CYAN   + "[i]   " + msg + RESET); }

    public static void printBadge(String label, String value) {
        System.out.println("  " + BOLD + label + RESET + "  " + value);
    }

    public static void printField(String label, String value) {
        String paddedLabel = String.format("%-14s", label);
        System.out.println("  " + DIM + paddedLabel + RESET + " " + BOLD + value + RESET);
    }

    public static void printFieldWithError(String label, String value, String error) {
        printField(label, value);
        if (error != null) {
            System.out.println("  " + " ".repeat(15) + RED + "  -> " + error + RESET);
        }
    }

    public static void printMenuOption(String key, String description) {
        System.out.println("  " + BOLD + CYAN + "[" + key + "]" + RESET + "  " + description);
    }

    public static void printMenuOption(String key, String description, String note) {
        System.out.println("  " + BOLD + CYAN + "[" + key + "]" + RESET + "  " + description
                + "  " + DIM + note + RESET);
    }

    public static String prompt(String label) {
        System.out.print("  " + BOLD + CYAN + label + RESET + " ");
        return scanner.nextLine().trim();
    }

    public static String promptWithDefault(String label, String defaultValue) {
        String display = (defaultValue == null || defaultValue.isBlank())
                ? label
                : label + " " + DIM + "[" + defaultValue + "]" + RESET;
        System.out.print("  " + BOLD + CYAN + display + RESET + " ");
        String input = scanner.nextLine().trim();
        return input.isBlank() ? defaultValue : input;
    }

    public static String promptWithError(String label, String currentValue, String error) {
        if (error != null) {
            System.out.println("  " + RED + "  -> " + error + RESET);
        }
        return promptWithDefault(label, currentValue);
    }

    public static boolean confirm(String message) {
        System.out.print("  " + BOLD + YELLOW + message + " [y/N]:" + RESET + " ");
        String input = scanner.nextLine().trim();
        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes");
    }

    public static void pressEnterToContinue() {
        System.out.print("  " + DIM + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    public static void printTableHeader(String... cols) {
        StringBuilder sb = new StringBuilder("  ");
        for (String col : cols) {
            sb.append(BOLD).append(String.format("%-22s", col)).append(RESET);
        }
        System.out.println(sb);
        printLine();
    }

    public static void printTableRow(int index, String... cols) {
        String indexStr = DIM + String.format("%2d.", index) + RESET + "  ";
        StringBuilder sb = new StringBuilder("  " + indexStr);
        for (int i = 0; i < cols.length; i++) {
            sb.append(String.format("%-20s", truncate(cols[i], 19)));
            if (i < cols.length - 1) sb.append("  ");
        }
        System.out.println(sb);
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "...";
    }

    public static int WIDTH() { return WIDTH; }

    /**
     * Selectables are entities like stores and products (interface, non-invasive)
     * Options are things like new and quit
     * Returns whether or not the program may continue. If false, it should terminate.
     *
     * Author: Jase Beaubien
     *
     * @param header
     * @param items
     * @param options
     * @return
     * @param <T>
     */
    public static <T extends Selectable> T prompt(String header, Iterable<T> items, Iterable<Option> options){
        HashMap<String, T> select = new HashMap<>();
        HashMap<String, Runnable> functions = new HashMap<>();

        printSubHeader(header);

        for(T item : items){
            System.out.println(CYAN + "[" + item.getId() + "] " + RESET + item.getName());
            select.put(Integer.toString(item.getId()), item);
        }
        for(Option option : options){
            System.out.println(CYAN + "[" + option.getCommand() + "] " + RESET + option.getDescription());
            functions.put(option.getCommand(), option::pick);
        }

        String input;
        boolean itemInput = false;
        boolean functionInput = false;
        while(true){
            System.out.print("\n" + BOLD + CYAN + "Choice > " + RESET);
            input = scanner.nextLine();

            itemInput = select.containsKey(input);
            functionInput = functions.containsKey(input);
            if(itemInput || functionInput){
                break;
            }

            System.out.println("Invalid input.");
        }

        if(functionInput) {
            functions.get(input).run();
            return null;
        }

        return select.get(input);
    }

    public static String getInput(String header){
        System.out.print(BOLD + CYAN + header + " > " + RESET);
        return scanner.nextLine();
    }
}