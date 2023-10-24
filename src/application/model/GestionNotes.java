/* 
 * GestionNotes.java                                                  19.10.2023
 * © copyright Chips-Couscous, But Informatique 2, IUT de Rodez (12)
 */

package application.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** TODO comment class responsibility (SRP)
 * @author thomas.lemaire
 *
 */
public class GestionNotes {

    /** TODO comment method role
     * @param csvFilePath
     * @return 0
     */
    public static List<String[]> readCSVFile(String csvFilePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(";");
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /** TODO comment method role
     * @param args
     */
    public static void main(String[] args) {
        String csvFile = "src/application/model/parametrage-sae.csv";
        List<String[]> csvData = readCSVFile(csvFile);

        for (String[] row : csvData) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }
}
