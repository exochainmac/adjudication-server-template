package com.exochain.api.bc.persistence.repositories;

import com.exochain.api.bc.persistence.entities.LoginChannel;
import com.exochain.api.bc.service.Registrar;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
@ActiveProfiles({"h2db", "dev", "internal", "nosecurity"})
public class LoginChannelRepositoryTest {

    @Autowired
    private LoginChannelRepository loginChannelRepo;

    @Test
    public void shouldContainDefaultLoginChannels() {
        Collection<LoginChannel> loginChannels = loginChannelRepo.findAllByTitle("BCC1");
        assertThat(loginChannels).hasSize(1);
    }

    @Test
    public void shouldFindByPrimaryKey() {
        final String id = "901a22c4-bb6f-467f-a802-1d1da994814";
        LoginChannel channel = loginChannelRepo.findOne(id);

        assertThat(channel.getId()).isEqualToIgnoringCase(id);
    }
}