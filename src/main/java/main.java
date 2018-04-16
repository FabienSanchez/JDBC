import java.sql.*;
import java.util.Scanner;

public class main {
    private static Connection connect = Connect.getInstance().getCon();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Boolean quit = false;

        do {
            System.out.println("Menu :" +
                    "\n\t1 - Afficher tout les personnages" +
                    "\n\t2 - Afficher un personnage" +
                    "\n\t3 - Créer un personnage");

            switch (sc.nextLine()) {
                case "1":
                    afficherTout();
                    break;
                case "2":
                    System.out.println("Nom ou ID :");
                    afficherSingle(sc.nextLine());
                    break;
                case "3":
                    createPerso();
                    break;
                default:
                    break;
            }
        } while (!quit);
    }

    private enum type {
        warrior,
        magicien;
    }

    private static void createPerso() {
        try {
            Statement state = connect.createStatement();
            ResultSet res;

            String query = "INSERT INTO personnage (type, name, image, life, power, weapon) VALUE (?,?,?,?,?,?)";
            PreparedStatement prepare = connect.prepareStatement(query);

            System.out.println("type :");
            prepare.setString(1, chooseType());
            System.out.println("Name :");
            prepare.setString(2, sc.nextLine());
            System.out.println("Image :");
            prepare.setString(3, sc.nextLine());
            System.out.println("Life :");
            prepare.setInt(4, Integer.parseInt(sc.nextLine()));
            System.out.println("Power :");
            prepare.setInt(5, Integer.parseInt(sc.nextLine()));
            System.out.println("Weapon :");
            prepare.setString(6, sc.nextLine());

            prepare.executeUpdate();

            prepare.close();
            state.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String chooseType() {
        do {
            try {
                int i = 0;
                for (type type : type.values())
                    System.out.println(i++ + " - " + type);
                int choix = Integer.parseInt(sc.nextLine());
                String str = type.values()[choix].name();
                return str;
            } catch (Exception ignored) {
                System.out.println("Invalid choice");
            }
        } while (true);
    }

    private static void afficherSingle(String args) {
        try {
            Statement state = connect.createStatement();
            ResultSet res;

            String query = "SELECT * FROM personnage WHERE id = ? OR name = ?";
            PreparedStatement prepare = connect.prepareStatement(query);

            prepare.setInt(1, Integer.parseInt(args));
            prepare.setString(2, args);

            res = prepare.executeQuery();

            ResultSetMetaData resultMeta = res.getMetaData();

            System.out.println("\n**********************************");
            for (int i = 1; i <= resultMeta.getColumnCount(); i++)
                System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");
            System.out.println("\n**********************************");

            while (res.next()) {
                for (int i = 1; i <= resultMeta.getColumnCount(); i++)
                    System.out.print("\t" + res.getObject(i).toString() + "\t |");
                System.out.println("\n---------------------------------");
            }

            prepare.close();
            res.close();
            state.close();
        } catch (Exception e) {
            System.out.println("Le perso n'existe pas");
        }

    }

    private static void afficherTout() {
        try {
            Statement state = connect.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM personnage");
            ResultSetMetaData resultMeta = result.getMetaData();

            System.out.println("\n**********************************");
            for (int i = 1; i <= resultMeta.getColumnCount(); i++)
                System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");
            System.out.println("\n**********************************");

            while (result.next()) {
                for (int i = 1; i <= resultMeta.getColumnCount(); i++)
                    System.out.print("\t" + result.getObject(i).toString() + "\t |");
                System.out.println("\n---------------------------------");
            }

            result.close();
            state.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
