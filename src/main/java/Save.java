import java.io.*;

public class Save {
    public static void WriteObjectToFile(Object obj, String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(obj);
            objectOut.close();
            System.out.println("Wrote character to " + filename);
        } catch (Exception e) {
            System.out.println("SOMETHING FUCKED UP IN THE FILE-SAVING");
        }
    }

    public static DNDCharacter ReadObjectFromFile(String filename) throws Exception {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);

        DNDCharacter obj = (DNDCharacter) objectIn.readObject();
        objectIn.close();
        System.out.println("Read character from " + filename);

        return obj;
    }
}
