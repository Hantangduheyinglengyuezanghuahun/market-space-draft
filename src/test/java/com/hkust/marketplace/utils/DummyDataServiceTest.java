package com.hkust.marketplace.utils;

import com.hkust.marketplace.models.User;
import com.hkust.marketplace.models.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DummyDataServiceTest {

    @Test
    void seededBuyerPasswordIsHashedAndVerifiable() {
        DummyDataService service = DummyDataService.getInstance();
        User alice = service.findUser("alice");

        assertNotNull(alice);
        assertNotEquals("password1", alice.getPasswordHash());
        assertTrue(service.verifyPassword(alice, "password1"));
        assertFalse(service.verifyPassword(alice, "wrong-password"));
    }

    @Test
    void registerUserHashesPasswordAndNormalizesUsername() {
        DummyDataService service = DummyDataService.getInstance();
        String uniqueUsername = "testuser" + System.nanoTime();
        User user = new User("  " + uniqueUsername.toUpperCase() + "  ", "Test User", "Passw0rd", UserRole.BUYER);

        service.registerUser(user);

        assertEquals(uniqueUsername.toLowerCase(), user.getUsername());
        assertTrue(service.usernameExists(uniqueUsername));
        assertNotEquals("Passw0rd", user.getPasswordHash());
        assertTrue(service.verifyPassword(uniqueUsername, "Passw0rd"));
        assertFalse(service.verifyPassword(uniqueUsername, "WrongPassw0rd"));
    }

    @Test
    void passwordStrengthRulesAreEnforced() {
        assertEquals("Password cannot be empty.", DummyDataService.checkPasswordStrength(""));
        assertEquals("Password must be at least 8 characters.", DummyDataService.checkPasswordStrength("Ab1"));
        assertEquals("Password should contain uppercase, lowercase and a digit.",
                DummyDataService.checkPasswordStrength("password"));
        assertEquals("Password should contain uppercase, lowercase and a digit.",
                DummyDataService.checkPasswordStrength("PASSWORD1"));
        assertEquals("OK", DummyDataService.checkPasswordStrength("SecurePass1"));
    }
}
