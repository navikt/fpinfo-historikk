package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Transactional(JPA_TM)
public interface JPADittNavOppgaverRepository
        extends JpaRepository<JPADittNavOppgave, Long>, JpaSpecificationExecutor<JPADittNavOppgave> {
    JPADittNavOppgave findByReferanseId(String referanseId);

    JPADittNavOppgave findBySaksnrAndHendelse(String saksnr, HendelseType hendelse);

}
