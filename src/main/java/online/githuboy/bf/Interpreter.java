package online.githuboy.bf;

import java.io.IOException;
import java.util.Scanner;

/**
 * A BF implementation in java
 * @author suchu
 * @since 2018/8/23 10:18
 */
public class Interpreter {
    private int pc;//program counter
    private int dp;//data pointer
    private char[] source;//source code
    private int[] memory;//memory space
    private StringBuilder result;
    private boolean termimal = false;//flag for program is running
    private boolean optimize = false;//should optimize code interpret
    Scanner scanner = new Scanner(System.in);// SYSTEM INPUT

    public Interpreter(String src) {
        this.source = src.toCharArray();
        int MEMORY_LIMIT = 4096;
        this.memory = new int[MEMORY_LIMIT];
        this.pc = 0;
        this.dp = 0;
        result = new StringBuilder(512);
    }

    private int add(int v) {
        return v > 255 ? 0 : v;
    }

    private int fixedValue(int v) {
        if (v > 255) return 0;
        if (v < 0) return 255;
        else return v;
    }

    private int sub(int v) {
        return v < 0 ? 255 : v;
    }

    private void rightForward() {
        if (memory[dp] == 0) {
            int depth = 1;
            while (depth != 0) {
                pc++;
                char opcode = opcode();
                if (opcode == '[') depth++;
                if (opcode == ']') depth--;
            }
        }
    }

    private void leftForward() {
        if (memory[dp] != 0) {
            int depth = 1;
            while (depth != 0) {
                pc--;
                char opcode = opcode();
                if (opcode == ']') depth++;
                if (opcode == '[') depth--;
            }
        }
    }

    private char opcode() {
        if (pc >= source.length || pc < 0) {
            termimal = true;
            return 0;
        }
        return source[pc];
    }

    public void run() {
        while (pc < source.length && !termimal) {
            char opcode = opcode();
            switch (opcode) {
                case Opcode.PTR_ADD:
                    dp++;
                    break;
                case Opcode.PTR_SUB:
                    dp--;
                    break;
                case Opcode.M_ADD:
                    memory[dp] = add(++memory[dp]);
                    break;
                case Opcode.M_SUB:
                    memory[dp] = sub(--memory[dp]);
                    break;
                case Opcode.D_IN:
                    char c = scanner.next(".").charAt(0);
                    memory[dp] = c;
                    break;
                case Opcode.D_OUT:
                    result.append((char) memory[dp]);
                    System.out.print((char) memory[dp]);
                    break;
                case Opcode.LOOP_START:
                    rightForward();
                    break;
                case Opcode.LOOP_END:
                    leftForward();
                    break;
                default:
                    break;
            }
            pc++;
        }
        //System.out.println(result.toString());
    }

    public static void main(String[] args) throws IOException {
        String src1 = "->+>+++>>+>++>+>+++>>+>++>>>+>+>+>++>+>>>>+++>+>>++>+>+++>>++>++>>+>>+>++>++>+>>>>+++>+>>>>++>++>>>>+>>++>+>+++>>>++>>++++++>>+>>++>+>>>>+++>>+++++>>+>+++>>>++>>++>>+>>++>+>+++>>>++>>+++++++++++++>>+>>++>+>+++>+>+++>>>++>>++++>>+>>++>+>>>>+++>>+++++>>>>++>>>>+>+>++>>+++>+>>>>+++>+>>>>+++>+>>>>+++>>++>++>+>+++>+>++>++>>>>>>++>+>+++>>>>>+++>>>++>+>+++>+>+>++>>>>>>++>>>+>>>++>+>>>>+++>+>>>+>>++>+>++++++++++++++++++>>>>+>+>>>+>>++>+>+++>>>++>>++++++++>>+>>++>+>>>>+++>>++++++>>>+>++>>+++>+>+>++>+>+++>>>>>+++>>>+>+>>++>+>+++>>>++>>++++++++>>+>>++>+>>>>+++>>++++>>+>+++>>>>>>++>+>+++>>+>++>>>>+>+>++>+>>>>+++>>+++>>>+[[->>+<<]<+]+++++[->+++++++++<]>.[+]>>[<<+++++++[->+++++++++<]>-.------------------->-[-<.<+>>]<[+]<+>>>]<<<[-[-[-[>>+<++++++[->+++++<]]>++++++++++++++<]>+++<]++++++[->+++++++<]>+<<<-[->>>++<<<]>[->>.<<]<<]";
        String src4 = "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.";
        Interpreter interpreter = new Interpreter(src4);
        interpreter.run();
    }

}
