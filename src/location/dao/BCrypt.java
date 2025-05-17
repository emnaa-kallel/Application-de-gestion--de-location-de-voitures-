/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package location.dao;

import java.security.MessageDigest;
 import java.security.NoSuchAlgorithmException;
 import java.security.SecureRandom;
 import java.util.Base64;

/**
 * Classe utilitaire pour le hachage des mots de passe en utilisant une approche simplifiée
 * Dans un environnement de production, il est recommandé d'utiliser une bibliothèque comme jBCrypt
 * @author sahar
 */
public class BCrypt {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SALT_LENGTH = 16; // Longueur du sel en octets
    
    /**
     * Génère un sel aléatoire pour le hachage
     * @return Chaîne encodée en Base64 représentant le sel
     */
    public static String gensalt() {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hache un mot de passe avec le sel fourni
     * @param password Mot de passe à hacher
     * @param salt Sel à utiliser
     * @return Mot de passe haché
     */
    public static String hashpw(String password, String salt) {
        try {
            // Combiner le mot de passe et le sel
            String combined = password + salt;
            
            // Créer un hachage SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combined.getBytes());
            
            // Encoder le hachage en Base64
            String hashedPassword = Base64.getEncoder().encodeToString(hash);
            
            // Stocker le sel et le hachage ensemble pour la vérification
            return salt + "$" + hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hachage", e);
        }
    }
    
    /**
     * Vérifie si un mot de passe correspond à son hachage
     * @param plaintext Mot de passe en clair
     * @param hashed Mot de passe haché (contenant le sel)
     * @return true si le mot de passe correspond, false sinon
     */
    public static boolean checkpw(String plaintext, String hashed) {
        try {
            // Séparer le sel et le hachage
            String[] parts = hashed.split("\\$");
            if (parts.length != 2) {
                return false; // Format invalide
            }
            
            String salt = parts[0];
            String storedHash = parts[1];
            
            // Hacher le mot de passe fourni avec le même sel
            String combined = plaintext + salt;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combined.getBytes());
            String newHash = Base64.getEncoder().encodeToString(hash);
            
            // Comparer les hachages
            return storedHash.equals(newHash);
        } catch (Exception e) {
            return false; // En cas d'erreur, considérer la vérification comme échouée
        }
    }
}
