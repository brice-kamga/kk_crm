package it.kk_crm.controller;

import it.kk_crm.model.dao.*;
import it.kk_crm.model.mo.Appuntamento;
import it.kk_crm.model.mo.Note;
import it.kk_crm.model.mo.ProposteS;
import it.kk_crm.model.mo.Utente;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DashboardControllerTest {

    @Test
    void testView_CaricamentoDashboard() {
        // 1. ARRANGE (Preparazione Mocks)
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock di tutti i DAO coinvolti
        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        // Colleghiamo i DAO alla Factory
        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);

        // Simuliamo l'utente loggato
        Utente user = new Utente();
        user.setUsername("mario.rossi");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        // Simuliamo i dati che ritornano dal DB (Liste vuote per semplicità)
        List<Appuntamento> appuntamenti = new ArrayList<>();
        List<ProposteS> proposte = new ArrayList<>();
        List<Note> noteOwner = new ArrayList<>();
        List<Note> noteNotOwner = new ArrayList<>();

        when(appuntamentoDAOMock.loadAppuntamenti7Days("mario.rossi")).thenReturn(appuntamenti);
        when(proposteSDAOMock.loadProposteOwner("mario.rossi")).thenReturn(proposte);
        when(noteDAOMock.loadNoteOwner("mario.rossi")).thenReturn(noteOwner);
        when(noteDAOMock.loadNoteNotOwner("mario.rossi")).thenReturn(noteNotOwner);

        // 2. ACT (Esecuzione)
        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            DashboardController.view(request, response);

            // 3. ASSERT (Verifiche)

            // Verifichiamo che siano stati chiamati i metodi corretti sui DAO
            verify(appuntamentoDAOMock).loadAppuntamenti7Days("mario.rossi");
            verify(proposteSDAOMock).loadProposteOwner("mario.rossi");
            verify(noteDAOMock).loadNoteOwner("mario.rossi");
            verify(noteDAOMock).loadNoteNotOwner("mario.rossi");

            // Verifichiamo che i dati siano stati messi nella Request per la JSP
            verify(request).setAttribute(eq("loggedUser"), eq(user));
            verify(request).setAttribute(eq("appuntamenti"), eq(appuntamenti));
            verify(request).setAttribute(eq("proposte"), eq(proposte));
            verify(request).setAttribute(eq("note"), eq(noteOwner));
            verify(request).setAttribute(eq("noteNot"), eq(noteNotOwner));

            // Verifichiamo il redirect alla pagina giusta
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/dashboard"));
        }
    }
}
