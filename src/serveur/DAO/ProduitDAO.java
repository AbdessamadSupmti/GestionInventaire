package serveur.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO {
    private Connection connection;

    public ProduitDAO() {
        connection = JDBCUtil.getConnection();
    }


    public void ajouterProduit(String nom, String categorie, int quantite, double prix, String marque, String description, String reference) {
        try {
            String query = "INSERT INTO produits (nom, categorie, quantite, prix, marque, description, reference) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, nom);
            stmt.setString(2, categorie);
            stmt.setInt(3, quantite);
            stmt.setDouble(4, prix);
            stmt.setString(5, marque);
            stmt.setString(6, description);
            stmt.setString(7, reference);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void supprimerProduit(int id) {
        try {
            String query = "DELETE FROM produits WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<String> rechercherProduits(String nom) {
        List<String> produits = new ArrayList<>();
        try {

            String query = "SELECT * FROM produits WHERE nom LIKE ? OR reference LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);


            stmt.setString(1, "%" + nom + "%");
            stmt.setString(2, "%" + nom + "%");

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {

                String productDetails =
                        "ID: " + rs.getInt("id") + ", " +
                                "Nom: " + rs.getString("nom") + ", " +
                                "Catégorie: " + rs.getString("categorie") + ", " +
                                "Quantité: " + rs.getInt("quantite") + ", " +
                                "Prix: " + rs.getDouble("prix") + ", " +
                                "Marque: " + rs.getString("marque") + ", " +
                                "Date Création: " + rs.getString("date_creation") + ", " +
                                "Date Modification: " + rs.getString("date_modification") + ", " +
                                "Description: " + rs.getString("description") + ", " +
                                "Référence: " + rs.getString("reference");


                produits.add(productDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    public String authenticateUser(String username, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<String> afficherProduits() {
        List<String> produits = new ArrayList<>();
        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM produits")) {

            while (rs.next()) {
                produits.add("ID: " + rs.getInt("id") +
                        ", Nom: " + rs.getString("nom") +
                        ", Catégorie: " + rs.getString("categorie") +
                        ", Quantité: " + rs.getInt("quantite") +
                        ", Prix: " + rs.getDouble("prix") +
                        ", Marque: " + rs.getString("marque") +
                        ", Date Création: " + rs.getTimestamp("date_creation") +
                        ", Date Modification: " + rs.getTimestamp("date_modification") +
                        ", Description: " + rs.getString("description") +
                        ", Référence: " + rs.getString("reference"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produits;
    }


    public void modifierProduit(int id, String nom, String categorie, int quantite, double prix, String marque, String description, String reference) {
        String sql = "UPDATE produits SET nom = ?, categorie = ?, quantite = ?, prix = ?, marque = ?, description = ?, reference = ? WHERE id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            stmt.setString(2, categorie);
            stmt.setInt(3, quantite);
            stmt.setDouble(4, prix);
            stmt.setString(5, marque);
            stmt.setString(6, description);
            stmt.setString(7, reference);
            stmt.setInt(8, id);

            stmt.executeUpdate();
            System.out.println("Produit mis à jour avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}