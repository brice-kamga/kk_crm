package it.kk_crm.controller;

import it.kk_crm.model.dao.*;
import it.kk_crm.model.mo.Azienda;
import it.kk_crm.model.mo.Cliente;
import it.kk_crm.model.mo.Utente;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AziendaControllerTest {

    @Test
    void testLoadAziende() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AziendaDAO aziendaDAOMock = mock(AziendaDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAziendaDAO()).thenReturn(aziendaDAOMock);

        Utente user = new Utente();
        user.setUsername("user");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AziendaController.loadAziende(request, response);

            verify(aziendaDAOMock).selectOwnerWReferente("user");
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/aziende"));
        }
    }

    @Test
    void testModifyView() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("status")).thenReturn("modify");
        when(request.getParameter("P_Iva")).thenReturn("12345");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AziendaDAO aziendaDAOMock = mock(AziendaDAO.class);
        ClienteDAO clienteDAOMock = mock(ClienteDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAziendaDAO()).thenReturn(aziendaDAOMock);
        when(daoFactoryMock.getClienteDAO()).thenReturn(clienteDAOMock);
        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AziendaController.modifyView(request, response);

            verify(aziendaDAOMock).getAzienda("12345");
            verify(clienteDAOMock).getClienteWAzienda("12345");
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/dettaglioAzienda"));
        }
    }

    @Test
    void testModify() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Parametri minimi per evitare null pointer
        when(request.getParameter("piva")).thenReturn("123");
        when(request.getParameter("cfR")).thenReturn("CF1");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AziendaDAO aziendaDAOMock = mock(AziendaDAO.class);
        ClienteDAO clienteDAOMock = mock(ClienteDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAziendaDAO()).thenReturn(aziendaDAOMock);
        when(daoFactoryMock.getClienteDAO()).thenReturn(clienteDAOMock);

        Utente user = new Utente();
        user.setUsername("user");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AziendaController.modify(request, response);

            verify(aziendaDAOMock).update(any(Azienda.class));
            verify(clienteDAOMock).update(any(Cliente.class));
            verify(request).setAttribute(eq("applicationMessage"), eq("Modifica riuscita"));
        }
    }

    @Test
    void testDelete() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("piva")).thenReturn("123");
        when(request.getParameter("cf")).thenReturn("CF123");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AziendaDAO aziendaDAOMock = mock(AziendaDAO.class);
        ClienteDAO clienteDAOMock = mock(ClienteDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);
        ProposteSDAO proposteDAOMock = mock(ProposteSDAO.class);
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAziendaDAO()).thenReturn(aziendaDAOMock);
        when(daoFactoryMock.getClienteDAO()).thenReturn(clienteDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteDAOMock);
        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);

        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AziendaController.delete(request, response);

            verify(aziendaDAOMock).delete("123");
            verify(clienteDAOMock).delete("CF123");
            verify(noteDAOMock).deleteC("CF123");
            verify(proposteDAOMock).deleteC("123");
            verify(appuntamentoDAOMock).deleteC("CF123");

            verify(request).setAttribute(eq("applicationMessage"), eq("Cancellazione riuscita"));
        }
    }

    @Test
    void testInsert() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("piva")).thenReturn("123");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AziendaDAO aziendaDAOMock = mock(AziendaDAO.class);
        ClienteDAO clienteDAOMock = mock(ClienteDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAziendaDAO()).thenReturn(aziendaDAOMock);
        when(daoFactoryMock.getClienteDAO()).thenReturn(clienteDAOMock);

        Utente user = new Utente();
        user.setUsername("user");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AziendaController.insert(request, response);

            verify(aziendaDAOMock).create(any(Azienda.class));
            verify(clienteDAOMock).create(any(Cliente.class));
            verify(request).setAttribute(eq("applicationMessage"), eq("Inserimento riuscito"));
        }
    }

    @Test
    void testLoadFiltri() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AziendaDAO aziendaDAOMock = mock(AziendaDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAziendaDAO()).thenReturn(aziendaDAOMock);
        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AziendaController.loadFiltri(request, response);

            verify(aziendaDAOMock).getDistinctCat();
            verify(aziendaDAOMock).getDistinctTipologia();
            verify(aziendaDAOMock).getDistinctOwner();
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/ricercaAzienda"));
        }
    }

    @Test
    void testLoadTable() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("filtro")).thenReturn("valore");
        when(request.getParameter("type")).thenReturn("tipo");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AziendaDAO aziendaDAOMock = mock(AziendaDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAziendaDAO()).thenReturn(aziendaDAOMock);
        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AziendaController.loadTable(request, response);

            verify(aziendaDAOMock).getClientiFilter("tipo", "valore");
            verify(request).setAttribute(eq("tableloaded"), eq("true"));
        }
    }
}
