
# Système de Gestion d'Inventaire avec Accès Distant Via (RMI)

![Image](https://img001.prntscr.com/file/img001/2AGgbDw9TwiKlzIJPIcNew.png)


## Description
Le projet "Système de Gestion d'Inventaire avec Accès Distant Via (RMI)" permet de gérer un inventaire de produits avec un accès distant via RMI (Remote Method Invocation). Le système utilise Java pour la logique de l'application, JavaFX pour l'interface graphique, et MySQL pour la gestion des données. Le projet inclut également un système d'authentification simple avec deux rôles : **utilisateur** et **admin**.

## Prérequis
Avant d'exécuter le projet, assurez-vous d'avoir installé les éléments suivants :

- **MySQL** : Une base de données pour gérer l'inventaire des produits.
  - [Télécharger MySQL](https://dev.mysql.com/downloads/installer/)

- **Zulu OpenJDK 23 avec JavaFX** : Cette version de Zulu JDK inclut JavaFX, nécessaire pour exécuter l'application.
  - [Télécharger Zulu OpenJDK 23](https://www.azul.com/downloads/?package=jdk-fx#zulu)

## Fonctionnalités de l'application

1. **Ajouter un produit** : 
   Permet d'ajouter un nouveau produit à l'inventaire avec des informations comme le nom, la catégorie, la quantité, le prix, la marque, la description et la référence.

2. **Supprimer un produit** : 
   Permet de supprimer un produit de l'inventaire en fonction de son identifiant unique.

3. **Rechercher des produits** :
   Permet de rechercher un produit dans l'inventaire en utilisant son nom ou sa référence.

4. **Afficher tous les produits** :
   Affiche la liste complète de tous les produits présents dans l'inventaire.

5. **Authentification d'utilisateur** :
   Permet de valider les informations de connexion d'un utilisateur. Les utilisateurs peuvent être soit des **utilisateurs réguliers** avec des privilèges limités, soit des **admins** avec des privilèges étendus.

6. **Modifier un produit** :
   Permet de modifier les informations d'un produit existant dans l'inventaire, comme le nom, la catégorie, la quantité, le prix, la marque, la description et la référence.
   
7. **Afficher les logs des operations** :
   Affiche l'historique des actions effectuées (ajout, suppression, modification) avec le type d'opération, l'utilisateur, les détails, et l'horodatage pour un suivi précis..
   
## Installation
Voici les étapes pour configurer la base de données et exécuter l'application :
1. **Créer la base de données** :
   Une fois MySQL installé avec user = root et mot de passe = root , vous devez créer la base de données qui stockera les informations de l'inventaire.
   ```sql
   CREATE DATABASE inventaire;
   ```

2. **Créer les tables** :
   Créez les tables nécessaires pour gérer les produits et les utilisateurs. Les tables sont définies comme suit :
   
   - **Table `produits`** : Elle stocke les informations sur les produits dans l'inventaire.
     ```sql
     CREATE TABLE produits (
         id INT AUTO_INCREMENT PRIMARY KEY,
         nom VARCHAR(255) NOT NULL,
         categorie VARCHAR(255) NOT NULL,
         quantite INT NOT NULL,
         prix DECIMAL(10, 2) NOT NULL,
         marque VARCHAR(255),
         date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
         date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
         description TEXT,
         reference VARCHAR(100)  NOT NULL
     );
     ```
     

 - **Table `logs`** : Elle stocke les informations sur les modifications.
     ```sql
    CREATE TABLE logs (
    id INT AUTO_INCREMENT PRIMARY KEY,       
    operation VARCHAR(50) NOT NULL,          
    details TEXT NOT NULL,                   
    user VARCHAR(50) NOT NULL,               
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP );
     ```
  
     

   - **Table `users`** : Elle permet de gérer les utilisateurs du système avec des rôles comme 'admin' et 'user'.
     ```sql
     CREATE TABLE users (
         id INT AUTO_INCREMENT PRIMARY KEY,
         username VARCHAR(255) UNIQUE NOT NULL,
         password VARCHAR(255) NOT NULL,
         role VARCHAR(50) NOT NULL
     );
     ```

     - Vous pouvez ajouter des utilisateurs par défaut en exécutant ces commandes :
     ```sql
     INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');
     INSERT INTO users (username, password, role) VALUES ('user', 'user123', 'user');
     ```



3. **Installer Zulu OpenJDK 23** :
   [Télécharger Zulu OpenJDK 23](https://www.azul.com/downloads/?package=jdk-fx#zulu)   qui inclut JavaFX.

4 . **Configurez JDBC** :
https://github.com/AbdessamadSupmti/GestionInventaire/blob/a742925a4723cb2a1062855390649be3a8ec6e40/src/serveur/DAO/JDBCUtil.java#L8
https://github.com/AbdessamadSupmti/GestionInventaire/blob/a742925a4723cb2a1062855390649be3a8ec6e40/src/serveur/DAO/JDBCUtil.java#L9
https://github.com/AbdessamadSupmti/GestionInventaire/blob/a742925a4723cb2a1062855390649be3a8ec6e40/src/serveur/DAO/JDBCUtil.java#L10
## Instructions pour exécuter le code (il y a 2 choix)


1. **Utiliser  l'application précompilée** :

    - Dans le dossier out/artifacts, exécutez le fichier Run.bat et l'application démarrera automatiquement.

1. **Cloner le projet depuis GitHub** :
   Clonez le repository avec la commande suivante :
   ```bash
   git clone https://github.com/AbdessamadSupmti/GestionInventaire.git
   ```

2. **Compiler le projet** :
   Compilez le projet Java avec votre IDE préféré ou en ligne de commande. Assurez-vous que Zulu JDK est bien inclus dans votre environnement de développement.

3. **Lancer le serveur RMI** :
   Exécutez le serveur RMI pour permettre l'accès distant à la gestion de l'inventaire
 

4. **Lancer l'application client** :
   Exécutez l'application Java pour interagir avec l'inventaire via l'interface graphique .

