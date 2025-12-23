package com.hendisantika.usermanagement;

import com.hendisantika.usermanagement.entity.User;
import com.hendisantika.usermanagement.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class CalculatorTest {
    
    private User user;
    
    // Cette méthode s'exécute AVANT chaque test
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        user.setPassword("securePassword123");
        user.setConfirmPassword("securePassword123");
        
        // Création d'un rôle pour les tests
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        
        // Dates
        user.setCreatedOn(LocalDateTime.of(2024, 1, 1, 10, 30));
        user.setLastModifiedOn(LocalDateTime.of(2024, 1, 2, 14, 45));
    }
    
    // ============= TESTS DES GETTERS/SETTERS =============
    
    @Test
    void testUserCreation() {
        assertNotNull(user, "L'utilisateur doit être créé");
    }
    
    @Test
    void testId() {
        assertEquals(1L, user.getId(), "L'ID doit être 1");
    }
    
    @Test
    void testFirstName() {
        assertEquals("John", user.getFirstName(), "Le prénom doit être 'John'");
    }
    
    @Test
    void testLastName() {
        assertEquals("Doe", user.getLastName(), "Le nom doit être 'Doe'");
    }
    
    @Test
    void testFullNameLogic() {
        // Test d'une logique métier hypothétique
        String expectedFullName = "John Doe";
        String actualFullName = user.getFirstName() + " " + user.getLastName();
        assertEquals(expectedFullName, actualFullName, 
                   "Le nom complet doit être 'John Doe'");
    }
    
    @Test
    void testEmail() {
        assertTrue(user.getEmail().contains("@"), 
                  "L'email doit contenir le caractère @");
        assertEquals("john.doe@example.com", user.getEmail(), 
                    "L'email doit être 'john.doe@example.com'");
    }
    
    @Test
    void testUsername() {
        assertEquals("johndoe", user.getUsername(), 
                    "Le username doit être 'johndoe'");
    }
    
    // ============= TESTS DE VALIDATION (Annotations) =============
    
    @Test
    void testFirstNameSizeValidation() {
        // Test avec un prénom trop court (doit échouter les règles @Size)
        User shortNameUser = new User();
        shortNameUser.setFirstName("J"); // 1 caractère < min=2
        // Note: Les annotations @Size sont validées par Spring, pas par JUnit directement
        // Ce test montre la logique
        assertTrue(shortNameUser.getFirstName().length() < 2, 
                  "Le prénom est trop court selon les règles @Size");
    }
    
    @Test
    void testEmailFormat() {
        // Test du format d'email
        String email = user.getEmail();
        assertTrue(email.contains("@") && email.contains("."), 
                  "L'email doit avoir un format valide (@ et .)");
    }
    
    // ============= TESTS DES COLLECTIONS =============
    
    @Test
    void testRolesNotEmpty() {
        assertFalse(user.getRoles().isEmpty(), 
                   "L'utilisateur doit avoir au moins un rôle");
        assertEquals(1, user.getRoles().size(), 
                    "L'utilisateur doit avoir exactement 1 rôle");
    }
    
    @Test
    void testRoleContent() {
        Role firstRole = user.getRoles().iterator().next();
        assertEquals("ROLE_USER", firstRole.getName(), 
                    "Le rôle doit être 'ROLE_USER'");
    }
    
    // ============= TESTS DES DATES =============
    
    @Test
    void testCreatedOn() {
        assertNotNull(user.getCreatedOn(), 
                     "La date de création ne doit pas être null");
        assertEquals(2024, user.getCreatedOn().getYear(), 
                    "L'année de création doit être 2024");
    }
    
    @Test
    void testLastModifiedOnAfterCreatedOn() {
        // Vérifie que lastModifiedOn est après createdOn
        assertTrue(user.getLastModifiedOn().isAfter(user.getCreatedOn()), 
                  "La date de modification doit être après la date de création");
    }
    
    // ============= TESTS DES MOTS DE PASSE =============
    
    @Test
    void testPasswordConfirmation() {
        // Test de la logique de confirmation de mot de passe
        // (ce serait dans le service, mais on teste la logique ici)
        boolean passwordsMatch = user.getPassword().equals(user.getConfirmPassword());
        assertTrue(passwordsMatch, 
                  "Le mot de passe et la confirmation doivent correspondre");
    }
    
    @Test
    void testPasswordNotBlank() {
        assertFalse(user.getPassword().isBlank(), 
                   "Le mot de passe ne doit pas être vide");
        assertTrue(user.getPassword().length() >= 8, 
                  "Le mot de passe doit avoir au moins 8 caractères (bonne pratique)");
    }
    
    // ============= TESTS AVEC NULL =============
    
    @Test
    void testUserWithNullValues() {
        User nullUser = new User();
        nullUser.setFirstName(null);
        
        // Test que le setter accepte null (même si @NotBlank le rejettera après)
        assertNull(nullUser.getFirstName(), 
                  "Le prénom peut être null (validation faite ailleurs)");
    }
    
    // ============= TEST DE L'EQUALS/HASHCODE (Lombok) =============
    
    @Test
    void testEqualsAndHashCode() {
        User user2 = new User();
        user2.setId(1L); // Même ID = mêmes objets selon equals
        
        // Lombok génère equals() basé sur tous les champs
        // Ici, on teste avec un champ différent
        user2.setFirstName("Jane");
        
        // Deux utilisateurs avec le même ID mais prénoms différents
        // Note: Votre @EqualsAndHashCode sans paramètres utilise TOUS les champs
        assertNotEquals(user, user2, 
                       "Deux users avec prénoms différents ne doivent pas être égaux");
    }
    
    // ============= TEST D'EXCEPTION (exemple) =============
    
    @Test
    void testNoExceptionWhenSettingValidData() {
        // Test qu'aucune exception n'est levée avec des données valides
        assertDoesNotThrow(() -> {
            User safeUser = new User();
            safeUser.setFirstName("ValidName");
            safeUser.setEmail("valid@email.com");
        }, "Aucune exception ne doit être levée avec des données valides");
    }
}