package dk.cphbusiness.mrv.twitterclone.usermanagement;

import dk.cphbusiness.mrv.twitterclone.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserOverviewIT extends TestBase {
    @Test
    public void getUserOverviewMustReturnNullIfUsernameDoesNotExist() {
        // Arrange
        // Act
        var result = um.getUserOverview("blub");

        // Assert
        assertNull(result, "Expected user 'blub' did not exist, but getUserOverview did not return null!");
    }

    @Test
    public void getUserOverview() {
        // Arrange
        var albert = getAlbert();
        um.createUser(albert);

        // Act
        var overview = um.getUserOverview(albert.username);

        // Assert
        assertEquals(overview.firstname, albert.firstname);
    }
}
