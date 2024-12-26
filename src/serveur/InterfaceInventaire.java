package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceInventaire extends Remote {

    void ajouterProduit(String nom, String categorie, int quantite, double prix, String marque, String description, String reference, String username) throws RemoteException;

    void supprimerProduit(int id, String username) throws RemoteException;

    List<String> rechercherProduits(String nom) throws RemoteException;

    List<String> afficherProduits() throws RemoteException;

    String authenticateUser(String username, String password) throws RemoteException;
    List<String> afficherLogs() throws RemoteException;


    void modifierProduit(int id, String nom, String categorie, int quantite, double prix, String marque, String description, String reference, String username) throws RemoteException;
}
