import java.io.*;
import java.util.*;

import static java.lang.Character.*;

public class Basic {

    public static void main(String[] args) {

        long beforeUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long timeBefore = System.nanoTime();
        try {
            //parsing & generating strings

            Scanner scan = new Scanner(new FileInputStream((new File(args[0]))));
            String result = "";
            List<String> strings = new ArrayList<String>();

            while (scan.hasNextLine()){

                String line = scan.nextLine();

                if (isDigit(line.charAt(0))){

                    int index = Integer.parseInt(line);
                    result = result.substring(0, index + 1) + result + result.substring(index + 1);


                }
                else{

                    if (result.length() != 0){

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
            int[][] OPT = new int[s1.length() + 1][s2.length() + 1];

            for (int i = 0; i <= s1.length(); i++){

                OPT[i][0] = DELTA * i;
            }

            for (int i = 0; i <= s2.length(); i++){

                OPT[0][i] = DELTA * i;
            }

            for (int i = 1; i <= s1.length(); i++){

                for (int j = 1; j <= s2.length(); j++){

                    int first = OPT[i - 1][j - 1] + ALPHA[s1.charAt(i - 1)][s2.charAt(j - 1)];
                    int second = OPT[i - 1][j] + DELTA;
                    int third = OPT[i][j - 1] + DELTA;

                    OPT[i][j] = Math.min(first, Math.min(second, third));
                }
            }


            int solution = OPT[s1.length()][s2.length()];
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

            long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            float totalUsedMem = (afterUsedMem - beforeUsedMem) / 1024.0f;
            long timeAfter = System.nanoTime();
            float timeUsage = (float) ((timeAfter - timeBefore) / 1000000.0);

            File outputFile = new File(args[1]);
            outputFile.createNewFile();
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
            writer.write("" + solution);
            writer.write("\n" + align1 + "\n");
            writer.write(align2 + "\n" );
            writer.write("" + timeUsage + "\n");
            writer.write("" + totalUsedMem);

            writer.close();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
