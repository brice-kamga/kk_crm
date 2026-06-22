package it.kk_crm.controller;

import it.kk_crm.model.dao.AppuntamentoDAO;
import it.kk_crm.model.dao.ClienteDAO;
import it.kk_crm.model.dao.DAOFactory;
import it.kk_crm.model.dao.UtenteDAO;
import it.kk_crm.model.mo.Appuntamento;
import it.kk_crm.model.mo.Utente;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AppuntamentiControllerTest {

    @Test
    void testView_CaricamentoAppuntamenti() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock DAO
        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);

        // Simuliamo utente loggato
        Utente user = new Utente();
        user.setUsername("test.user");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        // Simuliamo lista appuntamenti
        when(appuntamentoDAOMock.loadAllAppuntamenti("test.user")).thenReturn(new ArrayList<>());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            // ACT
            AppuntamentiController.view(request, response);

            // ASSERT
            verify(request).setAttribute(eq("appuntamenti"), any(ArrayList.class));
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/appuntamenti"));
        }
    }

    @Test
    void testDelete_Cancellazione() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("codice")).thenReturn("10");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AppuntamentiController.delete(request, response);

            verify(appuntamentoDAOMock).delete(10);
            verify(request).setAttribute(eq("applicationMessage"), eq("Eliminazione completata"));
        }
    }

    @Test
    void testLoadFilter_RicercaPerData() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("data")).thenReturn("2025-12-25");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);

        Utente user = new Utente();
        user.setUsername("user");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AppuntamentiController.loadFilter(request, response);

            verify(appuntamentoDAOMock).loadSelected("2025-12-25", "user");
            verify(request).setAttribute(eq("mode"), eq("filter"));
        }
    }

    @Test
    void testInsertView() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        ClienteDAO clienteDAOMock = mock(ClienteDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getClienteDAO()).thenReturn(clienteDAOMock);

        Utente user = new Utente();
        user.setUsername("user");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AppuntamentiController.insertView(request, response);

            verify(clienteDAOMock).getClientiOwner("user");
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/dettaglioAppuntamento"));
        }
    }

    @Test
    void testInsert_Inserimento() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("data")).thenReturn("2025-12-25");
        when(request.getParameter("nota")).thenReturn("Test");
        when(request.getParameter("cf")).thenReturn("CF123");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            AppuntamentiController.insert(request, response);

            verify(appuntamentoDAOMock).create(any(Appuntamento.class));
            verify(request).setAttribute(eq("applicationMessage"), eq("Inserimento appuntamento completato"));
        }
    }
}
