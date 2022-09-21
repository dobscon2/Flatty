package nz.ac.otago.flatty.dao;

import androidx.core.widget.TextViewCompat;

import nz.ac.otago.flatty.domain.Accounts;
import nz.ac.otago.flatty.dao.AccountsDAO;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AccountDAOTest {

    private AccountsDAO dao = new AccountsDAO("jdbc:h2:mem:tests;INIT=runscript from 'Database/FlattyDatabase-H2Template.sql'");
    private Accounts account1 = new Accounts("Testy", "Tester", "testy@tester.com", "testy2021");
    private Accounts account2;

    @BeforeEach
    public void setUp() throws IOException {
        account2 = dao.getAccount("danielcalencar@otago.ac.nz");
    }

    @AfterEach
    public void tearDown() throws IOException {

    }

    @Test
    public void testCreateAccount() {
        dao.createAccount(account1);
        Accounts returnAccount = dao.getAccount("testy@tester.com");
        assertThat(returnAccount.getFirstName(), is(account1.getFirstName()));
    }

    @Test
    public void testRemoveAccount() {
        Accounts returnAccount = dao.getAccount("testy@tester.com");
        dao.removeAccount(returnAccount);
        assertNull(dao.getAccount("testy@tester.com"));
    }

    @Test
    public void testLoginAccount() {
        assertThat(dao.validateLogin("danielcalencar@otago.ac.nz", "INFO310"), is(true));
        assertThat(dao.validateLogin("danielcalencar@otago.ac.nz", "INFO303"), is(false));
    }

    @Test
    public void testUpdateAccount() {
        Accounts account2_changed = dao.getAccount("danielcalencar@otago.ac.nz");
        account2_changed.setFirstName("Mark");
        account2_changed.setLastName("George");
        dao.updateAccount(account2_changed);
        assertThat(dao.getAccount("danielcalencar@otago.ac.nz").getFirstName(), is("Mark"));
    }
}
