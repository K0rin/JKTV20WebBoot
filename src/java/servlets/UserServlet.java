/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Boot;
import entity.History;
import entity.Reader;
import entity.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.BookFacade;
import session.HistoryFacade;
import session.ReaderFacade;
import session.UserFacade;
import session.UserRolesFacade;
import tools.EncryptPassword;

/**
 *
 * @author Melnikov
 */
@WebServlet(name = "UserServlet", urlPatterns = {
    "/editUser", 
    "/updateUser", 
    "/buyBook",
    
   
    
})
public class UserServlet extends HttpServlet {
    
    @EJB private UserFacade userFacade;
    @EJB private ReaderFacade readerFacade;
    @EJB private UserRolesFacade userRolesFacade;
    @EJB private BookFacade bookFacade;
    @EJB private HistoryFacade historyFacade;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if(session == null){
            request.setAttribute("info", "У вас нет прав. Войдите с правами читателя");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        User authUser = (User) session.getAttribute("authUser");
        if(authUser == null){
            request.setAttribute("info", "У вас нет прав. Войдите с правами читателя");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        
        if(!userRolesFacade.isRole("READER", authUser)){
            request.setAttribute("info", "У вас нет прав. Войдите с правами читателя");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        String path = request.getServletPath();
        switch (path) {
            case "/editUser":
                User user = userFacade.find(authUser.getId());
                request.setAttribute("user", user);
                request.getRequestDispatcher("/editUser.jsp").forward(request, response);
                break;
            case "/updateUser":
                String userId = request.getParameter("userId");
                String firstname = request.getParameter("firstname");
                String lastname = request.getParameter("lastname");
                String phone = request.getParameter("phone");
                String money = request.getParameter("money");
                
                String password1 = request.getParameter("password1");
                String password2 = request.getParameter("password2");
                if(!password1.equals(password2)){
                    request.setAttribute("firstname", firstname);
                    request.setAttribute("lastname", lastname);
                    request.setAttribute("phone", phone);
                    request.setAttribute("money", money);
                    
                    request.setAttribute("info", "Пароли не совпадают!");
                    request.getRequestDispatcher("/showRegistration").forward(request, response);
                    break;
                }
                if("".equals(firstname) || "".equals(lastname)
                      ||  "".equals(phone)){
                    request.setAttribute("firstname", firstname);
                    request.setAttribute("lastname", lastname);
                    request.setAttribute("phone", phone);
                    request.setAttribute("money", money);
                    request.setAttribute("info", "Заполните все поля!");
                    request.getRequestDispatcher("/showRegistration").forward(request, response);
                    break;
                }
                user = userFacade.find(Long.parseLong(userId));
                Reader reader = readerFacade.find(user.getReader().getId());
                reader.setFirstname(firstname);
                reader.setLastname(lastname);
                reader.setPhone(phone);
                reader.setMoney(money);
                readerFacade.edit(reader);
                if(!"".equals(password1) && !"".equals(password2)){
                    EncryptPassword ep = new EncryptPassword();
                    password1 = ep.createHash(password2, user.getSalt());
                    user.setPassword(password1);
                }
                user.setReader(reader);
                userFacade.edit(user);
                session.setAttribute("authUser", user);
                request.setAttribute("user", user);
                request.setAttribute("info", "Изменение данных успешно");
                request.getRequestDispatcher("/editUser.jsp").forward(request, response);
                break;
            case "/buyBook":
                String bookId = request.getParameter("bootId");
                Boot book = bookFacade.find(Long.parseLong(bookId));
                user = userFacade.find(authUser.getId());
                if(user.getReader().getMoney()== null){
                    request.setAttribute("info", "Для покупки не хватает денег");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    break;
                }
                if(user.getReader().getDecimalMoney().compareTo(book.getDecimalPrice()) < 0){
                    request.setAttribute("info", "Для покупки не хватает денег");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    break;
                }
                reader = readerFacade.find(user.getReader().getId());
                reader.setDecimalMoney(reader.getDecimalMoney().subtract(book.getDecimalPrice()));
                readerFacade.edit(reader);
                History history = new History();
                book.setQuantity(book.getQuantity()-1);
                history.setBook(book);
                history.setReader(user.getReader());
                history.setSellingDate(new GregorianCalendar().getTime());
                historyFacade.create(history);
                bookFacade.edit(book);
                session.setAttribute("authUser", user);
                request.setAttribute("info", "Покупка совершена успешно");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
        }    
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
