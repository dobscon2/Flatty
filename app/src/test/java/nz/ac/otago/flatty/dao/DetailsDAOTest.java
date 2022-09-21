package nz.ac.otago.flatty.dao;
import androidx.core.widget.TextViewCompat;

import nz.ac.otago.flatty.domain.Accounts;
import nz.ac.otago.flatty.dao.AccountsDAO;
import nz.ac.otago.flatty.domain.Accounts;
import nz.ac.otago.flatty.dao.AccountsDAO;
import nz.ac.otago.flatty.domain.Details;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DetailsDAOTest {

    private DetailsDAO dao = new DetailsDAO("jdbc:h2:mem:tests;INIT=runscript from 'Database/FlattyDatabase-H2Template.sql'");
    private Accounts account = new Accounts(1000, "John", "Doe", "student1@student.otago.ac.nz", "scarfie");

    @BeforeEach
    public void setUp() throws IOException {
        Date date = new Date(2000,7,8);
        Details details = new Details(account.getAccountID(), date, true, "testdegree", "hobbies", "Any");
        account.setDetails(details);
    }

    @AfterEach
    public void tearDown() throws IOException {
        // after each test
    }

    @Test
    public void testCreateDetails(){
        dao.createDetails(account);
        Details returnDetails = dao.getDetails(account);
        assertThat(returnDetails.getExtraInfo(), is(account.getDetails().getExtraInfo()));
    }

    @Test
    public void testDeleteDetails(){
        dao.deleteDetails(account);
        assertNull(dao.getDetails(account));
    }

    @Test
    public void testUpdateDetails(){
        Details detail = account.getDetails();
        detail.setExtraInfo("Fishing");
        account.setDetails(detail);
        dao.updateDetails(account);
        assertThat(dao.getDetails(account).getExtraInfo(), is("Fishing"));
    }
}
