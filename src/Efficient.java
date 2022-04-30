import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Character.isDigit;

public class Efficient {

    static int cost;
    static class Pair<T, V>{

        public T first;
        public V second;
        Pair(T f, V s){
            first = f;
            second = s;
        }
    }

    public static void main(String[] args){

        long beforeUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long timeBefore = System.nanoTime();
        try {
            //parsing & generating strings

            Scanner scan = new Scanner(new FileInputStream((new File(args[0]))));
            String result = "";
            List<String> strings = new ArrayList<String>();

            while (scan.hasNextLine()) {

                String line = scan.nextLine();

                if (isDigit(line.charAt(0))) {

                    int index = Integer.parseInt(line);
                    result = result.substring(0, index + 1) + result + result.substring(index + 1);


                } else {

                    if (result.length() != 0) {

                        strings.add(result);
                    }

                    result = line;
                }
            }

            scan.close();
            strings.add(result);

            //DP algorithm
            final int DELTA = 30;
            final int[][] ALPHA = new int[128][128];
            ALPHA['A']['A'] = 0;
            ALPHA['A']['C'] = 110;
            ALPHA['A']['G'] = 48;
            ALPHA['A']['T'] = 94;

            ALPHA['C']['C'] = 0;
            ALPHA['C']['A'] = 110;
            ALPHA['C']['G'] = 118;
            ALPHA['C']['T'] = 48;

            ALPHA['G']['G'] = 0;
            ALPHA['G']['A'] = 48;
            ALPHA['G']['C'] = 118;
            ALPHA['G']['T'] = 110;

            ALPHA['T']['T'] = 0;
            ALPHA['T']['A'] = 94;
            ALPHA['T']['C'] = 48;
            ALPHA['T']['G'] = 110;

            String s1 = strings.get(0), s2 = strings.get(1);

            Pair<String, String> alignment = divideAndConquer(s1, s2, ALPHA, DELTA);
            long afterUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            float totalUsedMem = (afterUsedMem - beforeUsedMem) / 1024.0f;
            long timeAfter = System.nanoTime();
            float timeUsage = (float) ((timeAfter - timeBefore) / 1000000.0);

            File file = new File(args[1]);
            file.createNewFile();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println(cost);
            writer.println(alignment.first);
            writer.println(alignment.second);
            writer.println(timeUsage);
            writer.println(totalUsedMem);

            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Pair<String, String> divideAndConquer(String s1, String s2, final int[][] ALPHA, final int DELTA){

        if (s1.length() == 1){



            int [][] OPT = new int[2][s2.length() + 1];


            OPT[1][0] = DELTA;

            for (int i = 0; i <= s1.length(); i++){

                for (int j = 0; j <= s2.length(); j++){

                    if (i == 0){

                        OPT[i][j] = DELTA * j;
                    }
                    else if (j == 0){

                        OPT[i][j] = DELTA * i;
                    }
                    else{

                        int first = OPT[(i - 1)][j - 1] + ALPHA[s1.charAt(i - 1)][s2.charAt(j - 1)];
                        int second = OPT[(i - 1)][j] + DELTA;
                        int third = OPT[i][j - 1] + DELTA;
                    }
                }
            }

            int i = s1.length(), j = s2.length();

            String align1 = "", align2 = "";
            while (i > 0 && j > 0){

                int first = OPT[i - 1][j - 1] + ALPHA[s1.charAt(i - 1)][s2.charAt(j - 1)];
                int second = OPT[i - 1][j] + DELTA;
                int third = OPT[i][j - 1] + DELTA;

                int min = Math.min(first, Math.min(second, third));

                if (min == first){

                    align1 = s1.charAt(i - 1) + align1;
                    align2 = s2.charAt(j - 1) + align2;
                    i--;
                    j--;
                }
                else if (min == third){

                    align1 =  "_" + align1;
                    align2 = s2.charAt(j - 1) + align2;
                    j--;
                }
                else{

                    align1 = s1.charAt(i - 1) + align1;
                    align2 = "_" + align2;
                    i--;
                }
            }

            //has some chars left in s1
            while (i > 0){

                align1 = s1.charAt(i - 1) + align1;
                align2 = '_' + align2;
                i--;
            }

            //has some chars left in s2
            while (j > 0){

                align1 = '_' + align1;
                align2 = s2.charAt(j - 1) + align2;
                j--;
            }


            return new Pair<String, String>(align1, align2);
        }
        else{

            int mid = s1.length() / 2;
            int[][] OPTLeft = new int[2][s2.length() + 1];

            //left part of s1
            for (int i = 0; i <= mid; i++){

                for (int j = 0; j <= s2.length(); j++){

                    if (i == 0){

                        OPTLeft[i % 2][j] = DELTA * j;
                    }
                    else if (j == 0){

                        OPTLeft[i % 2][j] = DELTA * i;
                    }
                    else{

                        int first = OPTLeft[(i - 1) % 2][j - 1] + ALPHA[s1.charAt(i - 1)][s2.charAt(j - 1)];
                        int second = OPTLeft[(i - 1) % 2][j] + DELTA;
                        int third = OPTLeft[i % 2][j - 1] + DELTA;

                        OPTLeft[(i % 2)][j] = Math.min(first, Math.min(second, third));
                    }
                }
            }

            //right part of s1;
            String s1reversed = new StringBuilder(s1.substring(mid)).reverse().toString();
            String s2reversed = new StringBuilder(s2).reverse().toString();
            int[][] OPTRight = new int[2][s2reversed.length() + 1];

            for (int i = 0; i <= s1reversed.length(); i++){

                for (int j = 0; j <= s2reversed.length(); j++){

                    if (i == 0){

                        OPTRight[i % 2][j] = DELTA * j;
                    }
                    else if (j == 0){

                        OPTRight[i % 2][j] = DELTA * i;
                    }
                    else{

                        int first = OPTRight[(i - 1) % 2][j - 1] + ALPHA[s1reversed.charAt(i - 1)][s2reversed.charAt(j - 1)];
                        int second = OPTRight[(i - 1) % 2][j] + DELTA;
                        int third = OPTRight[(i % 2)][j - 1] + DELTA;

                        OPTRight[(i % 2)][j] = Math.min(first, Math.min(second, third));
                    }
                }
            }

            int score = Integer.MAX_VALUE;
            int index = -1;

            //find optimal split
            for (int i = 0; i <= s2.length(); i++){

                int total = OPTLeft[mid % 2][i] + OPTRight[s1reversed.length() % 2][s2.length() - i];

                //System.out.println("Score is: " + total);
                if (total < score){

                    index = i;
                    score = total;
                }
            }


            //combine solution
            Pair<String, String> left = divideAndConquer(s1.substring(0, mid), s2.substring(0, index), ALPHA, DELTA);
            Pair<String, String> right = divideAndConquer(s1.substring(mid), s2.substring(index), ALPHA, DELTA);

            cost = score;
            return new Pair(left.first + right.first, left.second + right.second);
        }
    }
}
