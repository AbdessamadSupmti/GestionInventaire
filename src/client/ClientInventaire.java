package client;

import serveur.InterfaceInventaire;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.geometry.Pos;

import java.rmi.Naming;
import java.util.List;

public class ClientInventaire extends Application {

    private InterfaceInventaire inventaire;
    private User loggedInUser;

    private TableView<Product> table;
    private TextField tfNom, tfCategorie, tfQuantite, tfPrix, tfRecherche, tfMarque, tfDescription, tfReference;
    private Button btnAjouter, btnRechercher, btnModifier, btnSupprimer, btnAfficher;
    private int Idtoupdate = 0 ;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            VBox loginLayout = new VBox(10);
            TextField tfUsername = new TextField();
            tfUsername.setPromptText("Nom d'utilisateur");
            PasswordField tfPassword = new PasswordField();
            tfPassword.setPromptText("Mot de passe");
            Button btnLogin = new Button("Se connecter");

            loginLayout.getChildren().addAll(tfUsername, tfPassword, btnLogin);

            btnLogin.setOnAction(e -> {
                try {
                    inventaire = (InterfaceInventaire) Naming.lookup("rmi://localhost:2299/gestionnaire");
                    String username = tfUsername.getText().trim();
                    String password = tfPassword.getText().trim();

                    String role = inventaire.authenticateUser(username, password);

                    if (role != null) {
                        loggedInUser = new User(username, password, role);
                        showMainApp(primaryStage, role);
                    } else {
                        showErrorMessage("Erreur", "Nom d'utilisateur ou mot de passe incorrect.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showErrorMessage("Erreur", "Une erreur est survenue lors de l'authentification.");
                }
            });

            Scene loginScene = new Scene(loginLayout, 300, 200);
            loginScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Se Connecter au Gestion d'Inventaire");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMainApp (Stage primaryStage, String role) {
        try {
            inventaire = (InterfaceInventaire) Naming.lookup("rmi://localhost:2299/gestionnaire");

            primaryStage.setTitle("Gestion d'Inventaire");

            table = new TableView<>();
            TableColumn<Product, Integer> colId = new TableColumn<>("ID");
            TableColumn<Product, String> colNom = new TableColumn<>("Nom");
            TableColumn<Product, String> colCategorie = new TableColumn<>("Catégorie");
            TableColumn<Product, Integer> colQuantite = new TableColumn<>("Quantité");
            TableColumn<Product, Double> colPrix = new TableColumn<>("Prix");
            TableColumn<Product, String> colMarque = new TableColumn<>("Marque");

            TableColumn<Product, String> colDescription = new TableColumn<>("Description");
            TableColumn<Product, String> colReference = new TableColumn<>("Référence");
            TableColumn<Product, String> colDateCreation = new TableColumn<>("Date Création");
            TableColumn<Product, String> colDateModification = new TableColumn<>("Date Modification");

            colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
            colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
            colQuantite.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantite()).asObject());
            colPrix.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrix()).asObject());
            colMarque.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMarque()));
            colDateCreation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateCreation()));
            colDateModification.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateModification()));
            colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
            colReference.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReference()));

            table.getColumns().addAll(colNom, colCategorie, colQuantite, colPrix, colMarque, colDescription, colReference, colDateCreation, colDateModification);


            table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {

                    Idtoupdate = newValue.getId();
                    tfNom.setText(newValue.getNom());
                    tfCategorie.setText(newValue.getCategorie());
                    tfQuantite.setText(String.valueOf(newValue.getQuantite()));
                    tfPrix.setText(String.valueOf(newValue.getPrix()));
                    tfMarque.setText(newValue.getMarque());
                    tfDescription.setText(newValue.getDescription());
                    tfReference.setText(newValue.getReference());
                }
            });


            tfNom = new TextField();
            tfNom.setPromptText("Nom du produit");
            tfCategorie = new TextField();
            tfCategorie.setPromptText("Catégorie");
            tfQuantite = new TextField();
            tfQuantite.setPromptText("Quantité");
            tfPrix = new TextField();
            tfPrix.setPromptText("Prix");

            tfRecherche = new TextField();
            tfRecherche.setPromptText("Rechercher par Nom de produit ou Reference");

            tfMarque = new TextField();
            tfMarque.setPromptText("Marque");

            tfDescription = new TextField();
            tfDescription.setPromptText("Description");

            tfReference = new TextField();
            tfReference.setPromptText("Référence");

            btnAfficher = new Button("Afficher");
            btnRechercher = new Button("Rechercher");

            btnAjouter = new Button("Ajouter/Modifier");
            btnModifier = new Button("Modifier");
            btnSupprimer = new Button("Supprimer");

            Button btnDisconnect = new Button("Déconnexion");
            btnDisconnect.setOnAction(e -> {
                loggedInUser = null;
                start(primaryStage);
            });


            btnAjouter.setOnAction(e -> ajouterProduit());
            btnRechercher.setOnAction(e -> rechercherProduit());
            btnSupprimer.setOnAction(e -> supprimerProduit());
            btnAfficher.setOnAction(e -> afficherProduits());

            GridPane grid = new GridPane();
            grid.setVgap(10);
            grid.setHgap(10);
            grid.setAlignment(Pos.CENTER);

            if ("admin".equals(role)) {
            grid.add(new Label("Nom :"), 0, 1);
            grid.add(tfNom, 1, 1);
            grid.add(new Label("Catégorie :"), 0, 2);
            grid.add(tfCategorie, 1, 2);
            grid.add(new Label("Quantité :"), 0, 3);
            grid.add(tfQuantite, 1, 3);
            grid.add(new Label("Prix :"), 0, 4);
            grid.add(tfPrix, 1, 4);
            grid.add(new Label("Marque :"), 0, 5);
            grid.add(tfMarque, 1, 5);
            grid.add(new Label("Description :"), 0, 6);
            grid.add(tfDescription, 1, 6);
            grid.add(new Label("Référence :"), 0, 7);
            grid.add(tfReference, 1, 7);
            grid.add(btnAjouter, 0, 8, 2, 1);
            }
            HBox hbox = new HBox(10);
            if ("admin".equals(role)) {
            hbox.getChildren().addAll(btnRechercher, btnSupprimer, btnAfficher);
            hbox.setAlignment(Pos.CENTER);
            } else {

                hbox.getChildren().addAll(btnAfficher, btnRechercher);
                hbox.setAlignment(Pos.CENTER);

            }
            grid.add(btnDisconnect, 20, 0);

            VBox vbox = new VBox(20);
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(grid, tfRecherche, table, hbox);

            Scene scene = new Scene(vbox, 600, 500);

            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();
            afficherProduits();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void ajouterProduit() {
        try {


            String nom = tfNom.getText().trim();
            String categorie = tfCategorie.getText().trim();
            String marque = tfMarque.getText().trim();
            String description = tfDescription.getText().trim();
            String reference = tfReference.getText().trim();

            if (nom.isEmpty() || categorie.isEmpty() || marque.isEmpty() || description.isEmpty() || reference.isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis !");
            }

            int quantite = Integer.parseInt(tfQuantite.getText().trim());
            double prix = Double.parseDouble(tfPrix.getText().trim());

            if (Idtoupdate > 0) {
                inventaire.modifierProduit(Idtoupdate, nom, categorie, quantite, prix, marque, description, reference);
                System.out.println("Produit modifié avec succès !");

            } else {
                inventaire.ajouterProduit(nom, categorie, quantite, prix, marque, description, reference);
                System.out.println("Produit ajouté avec succès !");
            }
            afficherProduits();
            clearFields();
        } catch (IllegalArgumentException e) {
            showErrorMessage("Erreur", "Vérifiez les champs saisis : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Erreur", "Une erreur est survenue lors de l'ajout ou de la modification du produit.");
        }
    }


    private void supprimerProduit() {
        try {
            Product selectedProduct = table.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                int id = selectedProduct.getId();
                inventaire.supprimerProduit(id);
                System.out.println("Produit supprimé avec succès !");
                table.getItems().remove(selectedProduct);
            } else {
                showErrorMessage("Erreur", "Veuillez sélectionner un produit à supprimer.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Erreur", "Une erreur est survenue lors de la suppression du produit.");
        }
    }

    private void rechercherProduit() {
        try {
            String recherche = tfRecherche.getText().trim();

            if (recherche.isEmpty()) {
                throw new IllegalArgumentException("Le champ de recherche ne peut pas être vide !");
            }

            List<String> produits = inventaire.rechercherProduits(recherche);

            table.getItems().clear();

            if (produits.isEmpty()) {
                System.out.println("Aucun produit trouvé.");
            } else {
                produits.forEach(p -> {
                    String[] fields = parseProductString(p);
                    if (fields.length == 10) {
                        int id = Integer.parseInt(fields[0]);
                        table.getItems().add(new Product(
                                id,
                                fields[1], // Nom
                                fields[2], // Catégorie
                                Integer.parseInt(fields[3]), // Quantité
                                Double.parseDouble(fields[4]), // Prix
                                fields[5], // Marque
                                fields[6], // Date Creation
                                fields[7], // Date Modification
                                fields[8], // Description
                                fields[9]  // Reference
                        ));
                    }
                });
            }
        } catch (IllegalArgumentException e) {
            showErrorMessage("Erreur", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Erreur", "Une erreur est survenue lors de la recherche des produits.");
        }
    }







    private void afficherProduits() {
        try {
            List<String> produits = inventaire.afficherProduits();
            table.getItems().clear();

            if (produits.isEmpty()) {
                System.out.println("Aucun produit dans l'inventaire.");
            } else {
                produits.forEach(p -> {
                    String[] fields = parseProductString(p);
                    if (fields.length == 10) {
                        int id = Integer.parseInt(fields[0]); // ID de produit

                        table.getItems().add(new Product(id, fields[1], fields[2], Integer.parseInt(fields[3]), Double.parseDouble(fields[4]), fields[5], fields[6], fields[7], fields[8], fields[9]));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Erreur", "Une erreur est survenue lors de l'affichage des produits.");
        }
    }


    private void clearFields() {
        Idtoupdate = 0;
        tfNom.clear();
        tfCategorie.clear();
        tfQuantite.clear();
        tfPrix.clear();
        tfMarque.clear();
        tfDescription.clear();
        tfReference.clear();
    }

    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String[] parseProductString(String productString) {
        String[] fields = new String[10];

        String[] parts = productString.split(", ");
        for (int i = 0; i < parts.length; i++) {
            String[] keyValue = parts[i].split(": ");
            if (keyValue.length == 2) {
                String value = keyValue[1].trim();
                switch (i) {
                    case 0: // ID:
                        fields[0] = value;
                        break;
                    case 1: // Nom:
                        fields[1] = value;
                        break;
                    case 2: // Catégorie:
                        fields[2] = value;
                        break;
                    case 3: // Quantité:
                        fields[3] = value;
                        break;
                    case 4: // Prix:
                        fields[4] = value;
                        break;
                    case 5: // Marque:
                        fields[5] = value;
                        break;
                    case 6: // Date Création:
                        fields[6] = value;
                        break;
                    case 7: // Date Modification:
                        fields[7] = value;
                        break;
                    case 8: // Description:
                        fields[8] = value;
                        break;
                    case 9: // Reference:
                        fields[9] = value;
                        break;
                }
            }
        }
        return fields;
    }


    public static class Product {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty nom;
        private final SimpleStringProperty categorie;
        private final SimpleIntegerProperty quantite;
        private final SimpleDoubleProperty prix;
        private final SimpleStringProperty marque;
        private final SimpleStringProperty dateCreation;
        private final SimpleStringProperty dateModification;
        private final SimpleStringProperty description;
        private final SimpleStringProperty reference;

        public Product(int id, String nom, String categorie, int quantite, double prix, String marque, String dateCreation, String dateModification, String description, String reference) {
            this.id = new SimpleIntegerProperty(id);
            this.nom = new SimpleStringProperty(nom);
            this.categorie = new SimpleStringProperty(categorie);
            this.quantite = new SimpleIntegerProperty(quantite);
            this.prix = new SimpleDoubleProperty(prix);
            this.marque = new SimpleStringProperty(marque);
            this.dateCreation = new SimpleStringProperty(dateCreation);
            this.dateModification = new SimpleStringProperty(dateModification);
            this.description = new SimpleStringProperty(description);
            this.reference = new SimpleStringProperty(reference);
        }

        public int getId() {
            return id.get();
        }

        public String getNom() {
            return nom.get();
        }

        public String getCategorie() {
            return categorie.get();
        }

        public int getQuantite() {
            return quantite.get();
        }

        public double getPrix() {
            return prix.get();
        }

        public String getMarque() {
            return marque.get();
        }

        public String getDateCreation() {
            return dateCreation.get();
        }

        public String getDateModification() {
            return dateModification.get();
        }

        public String getDescription() {
            return description.get();
        }

        public String getReference() {
            return reference.get();
        }
    }

    public class User {
        private String username;
        private String password;
        private String role;

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }


        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }
    }
}