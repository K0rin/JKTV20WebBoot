/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Author;
import entity.Book;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.AuthorFacade;
import session.BookFacade;
import session.UserRolesFacade;

/**
 *
 * @author Melnikov
 */
@WebServlet(name = "ManagerServlet", urlPatterns = {
    
    "/addBook", 
    "/createBook", 
    "/editBook", 
    "/updateBook", 
    "/addAuthor", 
    "/createAuthor",
    "/editAuthor",
    "/updateAuthor",
    
})
public class ManagerServlet extends HttpServlet {
    
    @EJB private BookFacade bookFacade;
    @EJB private AuthorFacade authorFacade;
    @EJB private UserRolesFacade userRolesFacade;
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
            request.setAttribute("info", "У вас нет прав. Войдите с правами менеджера");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        User authUser = (User) session.getAttribute("authUser");
        if(authUser == null){
            request.setAttribute("info", "У вас нет прав. Войдите с правами менеджера");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        
        if(!userRolesFacade.isRole("MANAGER", authUser)){
            request.setAttribute("info", "У вас нет прав. Войдите с правами менеджера");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        String path = request.getServletPath();
        switch (path) {
            
            case "/addBook":
                List<Author> authors = authorFacade.findAll();
                request.setAttribute("authors", authors);
                request.getRequestDispatcher("/addBook.jsp").forward(request, response);
                break;
            case "/createBook":
                String caption = request.getParameter("caption");
                String[] bookAuthors = request.getParameterValues("authors");
                String publishedYear = request.getParameter("publishedYear");
                String quantity = request.getParameter("quantity");
                Book book = new Book();
                book.setCaption(caption);
                List<Author> listBookAuthors= new ArrayList<>();
                for (int i = 0; i < bookAuthors.length; i++) {
                    String authorId = bookAuthors[i];
                    listBookAuthors.add(authorFacade.find(Long.parseLong(authorId)));
                }
                book.setAuthors(listBookAuthors);
                book.setPublishedYear(Integer.parseInt(publishedYear));
                book.setQuantity(Integer.parseInt(quantity));
                book.setCount(book.getQuantity());
                bookFacade.create(book);
                request.getRequestDispatcher("/addBook.jsp").forward(request, response);
                break;
            case "/editBook":
                String bookId = request.getParameter("bookId");
                book = bookFacade.find(Long.parseLong(bookId));
                request.setAttribute("book", book);
                request.getRequestDispatcher("/editBook.jsp").forward(request, response);
                break;
            case "/updateBook":
                String newBookId = request.getParameter("bookId");
                String newCaption = request.getParameter("caption");
                String[] newAuthors = request.getParameterValues("listAuthors");
                String newPublishedYear = request.getParameter("publishedYear");
                String newQuantity = request.getParameter("quantity");
                Book editBook = bookFacade.find(Long.parseLong(newBookId));
                editBook.setCaption(newCaption);
                List<Author> newListAuthors = new ArrayList<>();
                for(String authorId : newAuthors){
                    newListAuthors.add(authorFacade.find(Long.parseLong(authorId)));
                }
                editBook.setAuthors(newListAuthors);
                editBook.setPublishedYear(Integer.parseInt(newPublishedYear));
                editBook.setQuantity(Integer.parseInt(newQuantity));
                bookFacade.edit(editBook);
                request.getRequestDispatcher("/listBooks").forward(request, response);
                break;
            case "/addAuthor":
                authors = authorFacade.findAll();
                request.setAttribute("authors", authors);
                request.getRequestDispatcher("/addAuthor.jsp").forward(request, response);
                break;
            case "/createAuthor":
                String name = request.getParameter("name");
                String lastname = request.getParameter("lastname");
                String year = request.getParameter("year");
                String day = request.getParameter("day");
                String month = request.getParameter("month");
                if("".equals(name) || "".equals(lastname)
                        || "".equals(year) || "".equals(day)
                          || "".equals(month)){
                    request.setAttribute("name", name);
                    request.setAttribute("lastname", lastname);
                    request.setAttribute("year", year);
                    request.setAttribute("day", day);
                    request.setAttribute("month", month);
                    request.setAttribute("info", "Заполните все поля");
                    request.getRequestDispatcher("/addAuthor.jsp").forward(request, response);
                    break;
                }
                Author newAuthor = new Author();
                newAuthor.setName(name);
                newAuthor.setLastname(lastname);
                newAuthor.setYear(Integer.parseInt(year));
                newAuthor.setDay(Integer.parseInt(day));
                newAuthor.setMonth(Integer.parseInt(month));
                authorFacade.create(newAuthor);
                request.setAttribute("info", "Новый автор создан");
                request.getRequestDispatcher("/addAuthor.jsp").forward(request, response);
                break;
            case "/editAuthor":
                String authorId = request.getParameter("authorId");
                Author editAuthor = authorFacade.find(Long.parseLong(authorId));
                request.setAttribute("author", editAuthor);
                
                request.getRequestDispatcher("/editAuthor.jsp").forward(request, response);
                break;
            case "/updateAuthor":
                authorId = request.getParameter("authorId");
                Author updateAuthor = authorFacade.find(Long.parseLong(authorId));
                name = request.getParameter("name");
                lastname = request.getParameter("lastname");
                year = request.getParameter("year");
                day = request.getParameter("day");
                month = request.getParameter("month");
                if("".equals(name) || "".equals(lastname)
                        || "".equals(year) || "".equals(day)
                          || "".equals(month)){
                    request.setAttribute("info", "Заполните все поля");
                    request.setAttribute("author", updateAuthor);
                    request.getRequestDispatcher("/editAuthor").forward(request, response);
                    break;
                }
                updateAuthor.setName(name);
                updateAuthor.setLastname(lastname);
                updateAuthor.setYear(Integer.parseInt(year));
                updateAuthor.setDay(Integer.parseInt(day));
                updateAuthor.setMonth(Integer.parseInt(month));
                authorFacade.edit(updateAuthor);
                request.setAttribute("info", "Автор обновлен");
                request.getRequestDispatcher("/addAuthor").forward(request, response);
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
