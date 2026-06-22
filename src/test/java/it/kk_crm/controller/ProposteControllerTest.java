package it.kk_crm.controller;

import it.kk_crm.model.dao.*;
import it.kk_crm.model.mo.Proposte;
import it.kk_crm.model.mo.Utente;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProposteControllerTest {

    @Test
    void testView() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            ProposteController.view(request, response);

            verify(proposteSDAOMock).loadAllProposte();
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/proposte"));
        }
    }

    @Test
    void testDelete() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn("50");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            ProposteController.delete(request, response);

            verify(proposteSDAOMock).delete(50);
            verify(request).setAttribute(eq("applicationMessage"), eq("Cancellazione completata"));
        }
    }

    @Test
    void testInsertView() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        ServiziConsulenzaDAO serviziDAOMock = mock(ServiziConsulenzaDAO.class);
        AziendaDAO aziendaDAOMock = mock(AziendaDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getServiziConsulenzaDAO()).thenReturn(serviziDAOMock);
        when(daoFactoryMock.getAziendaDAO()).thenReturn(aziendaDAOMock);

        Utente user = new Utente();
        user.setUsername("user");
        when(utenteDAOMock.findLoggedUser()).thenReturn(user);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            ProposteController.insertView(request, response);

            verify(serviziDAOMock).loadAllServizi();
            verify(aziendaDAOMock).selectOwner("user");
            verify(request).setAttribute(eq("viewUrl"), eq("vistaRegistrato/dettaglioProposta"));
        }
    }

    @Test
    void testInsert() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("proposta")).thenReturn("Test");
        when(request.getParameter("servizio")).thenReturn("1");
        when(request.getParameter("azienda")).thenReturn("123");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(utenteDAOMock.findLoggedUser()).thenReturn(new Utente());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any())).thenReturn(daoFactoryMock);

            ProposteController.insert(request, response);

            verify(proposteSDAOMock).create(any(Proposte.class));
            verify(request).setAttribute(eq("applicationMessage"), eq("Inserimento completato"));
        }
    }
}
