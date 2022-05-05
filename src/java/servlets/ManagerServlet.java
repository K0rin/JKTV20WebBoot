/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Author;
import entity.Book;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
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
               // String cover = request.getParameter("cover");
                String pathToFile = "D:\\UploadDir";
                Part part = request.getPart("cover");
                String filename = getFileName(part);
                File file = new File(pathToFile+File.separator+filename);
                file.mkdirs();
                try(InputStream fileContent = part.getInputStream()){
                    Files.copy(fileContent, file.getPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                String caption = request.getParameter("caption");
                String[] bookAuthors = request.getParameterValues("authors");
                String publishedYear = request.getParameter("publishedYear");
                String price = request.getParameter("price");
                Book book = new Book();
                book.setCaption(caption);
                List<Author> listBookAuthors= new ArrayList<>();
                for (int i = 0; i < bookAuthors.length; i++) {
                    String authorId = bookAuthors[i];
                    listBookAuthors.add(authorFacade.find(Long.parseLong(authorId)));
                }
                book.setAuthors(listBookAuthors);
                book.setPublishedYear(Integer.parseInt(publishedYear));
                book.setPrice(price);
                book.setCover(cover);
                bookFacade.create(book);
                request.getRequestDispatcher("/addBook.jsp").forward(request, response);
                break;
            case "/editBook":
                String bookId = request.getParameter("bookId");
                book = bookFacade.find(Long.parseLong(bookId));
                request.setAttribute("book", book);
                Map<Author, String> authorsMap= new HashMap<>();
                List<Author> listAuthors =  authorFacade.findAll();
                for(Author author : listAuthors){
                    if(book.getAuthors().contains(author)){
                        authorsMap.put(author,"selected");
                    }else{
                        authorsMap.put(author,"");
                    }
                }
                request.setAttribute("authorsMap",authorsMap);
                request.getRequestDispatcher("/editBook.jsp").forward(request, response);
                break;
            case "/updateBook":
                String newBookId = request.getParameter("bookId");
                String newCaption = request.getParameter("caption");
                String[] newAuthors = request.getParameterValues("listAuthors");
                String newPublishedYear = request.getParameter("publishedYear");
                price = request.getParameter("price");
                if("".equals(newBookId) || newBookId == null
                     || "".equals(newCaption) || newCaption == null   
                     || newAuthors == null || newAuthors.length == 0  
                     || "".equals(newPublishedYear) || newPublishedYear == null 
                     || "".equals(price)
                        ){
                    request.setAttribute("info", "Заполните все поля (выберите авторов)");
                    request.getRequestDispatcher("/editBook").forward(request, response);
                    break;
                }
                Book editBook = bookFacade.find(Long.parseLong(newBookId));
                editBook.setCaption(newCaption);
                List<Author> newListAuthors = new ArrayList<>();
                for(String authorId : newAuthors){
                    newListAuthors.add(authorFacade.find(Long.parseLong(authorId)));
                }
                editBook.setAuthors(newListAuthors);
                editBook.setPublishedYear(Integer.parseInt(newPublishedYear));
                editBook.setPrice(price);
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
                request.getRequestDispatcher("/addAuthor").forward(request, response);
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
    private String getFileName(Part part){
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")){
            if(content.trim().startsWith("filename")){
                return content
                        .substring(content.indexOf('=')+1)
                        .trim()
                        .replace("\"",""); 
            }
        }
        return null;
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
