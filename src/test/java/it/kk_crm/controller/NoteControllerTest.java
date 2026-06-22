package it.kk_crm.controller;

import it.kk_crm.model.dao.*;
import it.kk_crm.model.mo.Note;
import it.kk_crm.model.mo.Utente;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NoteControllerTest {

    @Test
    void testLoadNote() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("nome")).thenReturn("Mario");
        when(request.getParameter("cognome")).thenReturn("Rossi");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            NoteController.loadNote(request, response);

            verify(noteDAOMock).loadNoteFilter("Mario", "Rossi");
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/note"));
        }
    }

    @Test
    void testView() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class); // In questo test mancava anche NoteDAO

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            NoteController.view(request, response);
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/note"));
        }
    }

    @Test
    void testModifyView() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("status")).thenReturn("modify");
        when(request.getParameter("id")).thenReturn("5");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);
        ClienteDAO clienteDAOMock = mock(ClienteDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        when(daoFactoryMock.getClienteDAO()).thenReturn(clienteDAOMock);

        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            NoteController.modifyView(request, response);

            verify(noteDAOMock).getNota("5");
            verify(clienteDAOMock).getClienti();
            verify(utenteDAOMock).loadAllUtenti();
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/dettaglioNota"));
        }
    }

    @Test
    void testModify() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn("10");
        when(request.getParameter("nota")).thenReturn("Nuova");

        when(request.getParameter("nome")).thenReturn("");
        when(request.getParameter("cognome")).thenReturn("");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);

        Utente user = new Utente();
        user.setUsername("user");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        when(noteDAOMock.loadNoteFilter(any(), any())).thenReturn(new ArrayList<>());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            NoteController.modify(request, response);

            verify(noteDAOMock).update(any(Note.class));
            verify(request).setAttribute(eq("applicationMessage"), eq("Modifica completata"));
        }
    }

    @Test
    void testDelete() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn("15");

        when(request.getParameter("nome")).thenReturn("");
        when(request.getParameter("cognome")).thenReturn("");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);

        Utente user = new Utente();
        user.setUsername("test.user");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        when(noteDAOMock.loadNoteFilter(any(), any())).thenReturn(new ArrayList<>());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            NoteController.delete(request, response);

            verify(noteDAOMock).delete(15);
            verify(request).setAttribute(eq("applicationMessage"), eq("Cancellazione completata"));
        }
    }

    @Test
    void testInsert() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("nota")).thenReturn("Test");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);

        Utente user = new Utente();
        user.setUsername("user");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            NoteController.insert(request, response);

            verify(noteDAOMock).create(any(Note.class));
            verify(request).setAttribute(eq("applicationMessage"), eq("Inserimento completato"));
        }
    }
}
