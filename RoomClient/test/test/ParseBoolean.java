package test;

import java.util.Scanner;

public class ParseBoolean {
    public static void main(String[] args) {
        int times = 2;
        
        String data = "";
        for(int i = 0; i < times; i++) {
            data += ((int) (Math.random() * 2) == 0) + " ";
        }
        
        int totalTrue1 = 0;
        double start = System.currentTimeMillis();
        String[] allB = data.split(" ");
        for(String s : allB) {
            if(Boolean.parseBoolean(s)) totalTrue1++;
        }
        double total = System.currentTimeMillis() - start;
        System.out.println("Parse:   \t" + (total/1000));
        
        int totalTrue2 = 0;
        start = System.currentTimeMillis();
        Scanner bbb = new Scanner(data);
        while(bbb.hasNext()) {
            if(bbb.nextBoolean()) totalTrue2++;
        }
        total = System.currentTimeMillis() - start;
        System.out.println("Scanner:\t" + (total/1000));
        
        System.out.println(totalTrue1 + " = " + totalTrue2);
    }
}