package ex1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Tab {
    public static int[][] createTab(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner sc = new Scanner(file);
        String ligne = sc.nextLine();
        int[][] tab = new int[Integer.parseInt(ligne.split(" ")[0])][Integer.parseInt(ligne.split(" ")[1])];
        int i = 0;
        while(sc.hasNextLine()) {
            ligne = sc.nextLine();
            var tmp = ligne.split(" ");
            for(int j = 0; j < tmp.length; j++) {
                tab[i][j] = Integer.parseInt(tmp[j]);
            }
            i++;
        }
        return tab;
    }

    public static void main(String[] args) throws FileNotFoundException {
        var tab = createTab("resources/examples/reseau_50_50_1.txt");
        for(int[] t : tab)
            System.out.println(Arrays.toString(t));
    }
}
