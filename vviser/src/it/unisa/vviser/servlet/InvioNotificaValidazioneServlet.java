package it.unisa.vviser.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.vviser.entity.Prodotto;
import it.unisa.vviser.entity.Utente;
import it.unisa.vviser.storage.DBGestioneProdotto;
import it.unisa.vviser.storage.DBGestioneValidazione;



/**
 * 
 * @author Angiuoli Salvatore
 *
 */
public class InvioNotificaValidazioneServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private DBGestioneValidazione gprodotto;

	/**
	 * 
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		this.gprodotto=DBGestioneValidazione.getInstance();
	}
	
	/**
     * Gestisce il metodo HTTP <code>GET</code>
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException in presenza di un errore servlet
     * @throws IOException in presenza di un errore I/O 
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
        processRequest(request, response);
    }
	
	/**
     * Gestisce il metodo HTTP <code>POST</code>
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException in presenza di un errore servlet
     * @throws IOException in presenza di un errore I/O 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

	/**
	 * validazione prodotti per dipartimento
	 * @param request servlet request
	 * @param response servlet response
	 */
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String messaggio = request.getParameter("messaggio");
		DBGestioneValidazione gp=DBGestioneValidazione.getInstance();
		DBGestioneProdotto gpr=DBGestioneProdotto.getInstance();
		
		String checkProduct=request.getParameter("isbn");
		HttpSession s = request.getSession();
		Utente currentUser=(Utente)s.getAttribute("utente");
		
		//System.out.println(checkProduct);
		//System.out.println("loggato");
		//System.out.println(currentUser.getEmail());
		try
		{
			Prodotto p=gpr.ricercaProdottoISBN(checkProduct);
			//System.out.println("proprietario");
			//System.out.println(p.getProprietario());
			gp.invionotifica(messaggio,p.getProprietario(),currentUser.getEmail(),"messaggio");
			gp.Spostainbozza(checkProduct);
			ServletContext sc = getServletContext();
			
			if(currentUser.getTipologia().equals("direttoreDiDipartimento"))
			{
				RequestDispatcher rd = sc.getRequestDispatcher("/direttore/home_direttore.jsp");
				try
				{
					rd.forward(request,response);
				}
				catch (ServletException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if(currentUser.getTipologia().equals("membroDelComitatoDiAreaDidattica"))
			{
				RequestDispatcher rd = sc.getRequestDispatcher("/direttore/home_direttore.jsp");
				try
				{
					rd.forward(request,response);
				}
				catch (ServletException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				
			
			}
			
			
			
			
			
			
			
			
			
			
			
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}
