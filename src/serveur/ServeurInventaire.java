package serveur;

import serveur.DAO.ProduitDAO;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServeurInventaire extends UnicastRemoteObject implements InterfaceInventaire {
    private ProduitDAO produitDAO;

    protected ServeurInventaire() throws RemoteException {
        produitDAO = new ProduitDAO();
    }


    @Override
    public String authenticateUser(String username, String password) throws RemoteException {
        return produitDAO.authenticateUser(username, password);
    }
    public void ajouterProduit(String nom, String categorie, int quantite, double prix, String marque, String description, String reference) throws RemoteException {
        produitDAO.ajouterProduit(nom, categorie, quantite, prix, marque, description, reference);
    }

    public void supprimerProduit(int id) throws RemoteException {
        produitDAO.supprimerProduit(id);
    }

    public List<String> rechercherProduits(String nom) throws RemoteException {
        return produitDAO.rechercherProduits(nom);
    }

    @Override
    public List<String> afficherProduits() throws RemoteException {
        return produitDAO.afficherProduits();
    }

    @Override
    public void modifierProduit(int id, String nom, String categorie, int quantite, double prix, String marque, String description, String reference) throws RemoteException {
        produitDAO.modifierProduit(id, nom, categorie, quantite, prix, marque, description, reference);
    }

    public static void main(String[] args) {
        try {

            Runtime.getRuntime().exec("rmiregistry 2299");
            LocateRegistry.createRegistry(2299);
            Naming.rebind("rmi://localhost:2299/gestionnaire", new ServeurInventaire());
            System.out.println("Serveur RMI Démarré Avec Succès...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
