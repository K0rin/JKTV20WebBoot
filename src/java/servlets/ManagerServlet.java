/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Manufactor;
import entity.Boot;
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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import session.ManufactorFacade;
import session.BootFacade;
import session.UserRolesFacade;

/**
 *
 * @author Melnikov
 */
@WebServlet(name = "ManagerServlet", urlPatterns = {
    
    "/addBoot", 
    "/createBoot", 
    "/editBoot", 
    "/updateBoot", 
    "/addManufactor", 
    "/createManufactor",
    "/editManufactor",
    "/updateManufactor",
    
})
@MultipartConfig
public class ManagerServlet extends HttpServlet {
    
    @EJB private BootFacade bootFacade;
    @EJB private ManufactorFacade manufactorFacade;
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
            request.setAttribute("info", "?? ?????? ?????? ????????. ?????????????? ?? ?????????????? ??????????????????");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        User authUser = (User) session.getAttribute("authUser");
        if(authUser == null){
            request.setAttribute("info", "?? ?????? ?????? ????????. ?????????????? ?? ?????????????? ??????????????????");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        
        if(!userRolesFacade.isRole("MANAGER", authUser)){
            request.setAttribute("info", "?? ?????? ?????? ????????. ?????????????? ?? ?????????????? ??????????????????");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        String path = request.getServletPath();
        switch (path) {
            
            case "/addBoot":
                List<Manufactor> manufactors = manufactorFacade.findAll();
                request.setAttribute("manufactor", manufactors);
                request.getRequestDispatcher("/addBoot.jsp").forward(request, response);
                break;
            case "/createBoot":
               // String cover = request.getParameter("cover");
                String pathToDir = "C:\\UploadDir\\JKTV20WebLibrary";
                Part part = request.getPart("cover");
                String filename = getFileName(part);
                String pathToFile = pathToDir+File.separator+filename;
                File file = new File(pathToFile);
                file.mkdirs();
                try(InputStream fileContent = part.getInputStream()){
                    Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                String caption = request.getParameter("caption");
                String[] bootManufactor = request.getParameterValues("manufactors");
                String releaseyear = request.getParameter("releaseyear");
                String price = request.getParameter("price");
                String quantity = request.getParameter("quantity");
                if(
                     "".equals(caption) || caption == null   
                     || bootManufactor == null || bootManufactor.length == 0  
                     || "".equals(releaseyear) || releaseyear == null
                     || "".equals(quantity) || quantity == null   
                     || "".equals(price) || price == null
                        ){
                    request.setAttribute("info", "?????????????????? ?????? ???????? (???????????????? ????????????????????????????)");
                    request.getRequestDispatcher("/addBoot.jsp").forward(request, response);
                    break;
                }
                Boot boot = new Boot();
                boot.setCaption(caption);
                List<Manufactor> listBookAuthors= new ArrayList<>();
                for (int i = 0; i < bootManufactor.length; i++) {
                    String authorId = bootManufactor[i];
                    listBookAuthors.add(manufactorFacade.find(Long.parseLong(authorId)));
                }
                boot.setAuthors(listBookAuthors);
                boot.setReleaseyear(Integer.parseInt(releaseyear));
                boot.setQuantity(Integer.parseInt(quantity));
                boot.setPrice(price);
                boot.setCover(pathToFile);
                bootFacade.create(boot);
                request.setAttribute("info", "?????????????? ?????????????? ????????????????");
                request.getRequestDispatcher("/addBoot").forward(request, response);
                break;
            case "/editBoot":
                String bootId = request.getParameter("bootId");
                boot = bootFacade.find(Long.parseLong(bootId));
                request.setAttribute("boot", boot);
                Map<Manufactor, String> authorsMap= new HashMap<>();
                List<Manufactor> listAuthors =  manufactorFacade.findAll();
                for(Manufactor author : listAuthors){
                    if(boot.getAuthors().contains(author)){
                        authorsMap.put(author,"selected");
                    }else{
                        authorsMap.put(author,"");
                    }
                }
                request.setAttribute("authorsMap",authorsMap);
                request.getRequestDispatcher("/editBoot.jsp").forward(request, response);
                break;
            case "/updateBoot":
                String newBootId = request.getParameter("bootId");
                String newCaption = request.getParameter("caption");
                String[] newAuthors = request.getParameterValues("listAuthors");
                releaseyear = request.getParameter("releaseyear");
                quantity = request.getParameter("quantity");
                price = request.getParameter("price");
                if("".equals(newBootId) || newBootId == null
                     || "".equals(newCaption) || newCaption == null   
                     || newAuthors == null || newAuthors.length == 0  
                     || "".equals(releaseyear) || releaseyear == null 
                     || "".equals(price) || price == null
                     || "".equals(quantity) || quantity == null
                        ){
                    request.setAttribute("info", "?????????????????? ?????? ???????? (???????????????? ????????????????????????????)");
                    request.getRequestDispatcher("/editBoot.jsp").forward(request, response);
                    break;
                }
                Boot editBook = bootFacade.find(Long.parseLong(newBootId));
                editBook.setCaption(newCaption);
                List<Manufactor> newListAuthors = new ArrayList<>();
                for(String authorId : newAuthors){
                    newListAuthors.add(manufactorFacade.find(Long.parseLong(authorId)));
                }
                editBook.setAuthors(newListAuthors);
                editBook.setReleaseyear(Integer.parseInt(releaseyear));
                editBook.setPrice(price);
                editBook.setQuantity(Integer.parseInt(quantity));
                bootFacade.edit(editBook);
                request.setAttribute("info", "???????????? ?????????????? ????????????????");
                request.getRequestDispatcher("/editBoot").forward(request, response);
                break;
            case "/addManufactor":
                manufactors = manufactorFacade.findAll();
                request.setAttribute("manufactor", manufactors);
                request.getRequestDispatcher("/addManufactor.jsp").forward(request, response);
                break;
            case "/createManufactor":
                String name = request.getParameter("name");
                String country = request.getParameter("country");
                String city = request.getParameter("city");
                String address = request.getParameter("address");
                
                if("".equals(name) || "".equals(country)
                        || "".equals(city) || "".equals(address)){
                    request.setAttribute("name", name);
                    request.setAttribute("country", country);
                    request.setAttribute("city", city);
                    request.setAttribute("address", address);
                    
                    request.setAttribute("info", "?????????????????? ?????? ????????");
                    request.getRequestDispatcher("/addManufactor").forward(request, response);
                    break;
                }
                Manufactor newManufactor = new Manufactor();
                newManufactor.setName(name);
                newManufactor.setCountry(country);
                newManufactor.setCity(city);
                newManufactor.setAddress(address);
                
                manufactorFacade.create(newManufactor);
                request.setAttribute("info", "?????????? ?????????????????????????? ????????????");
                request.getRequestDispatcher("/addManufactor").forward(request, response);
                break;
            case "/editManufactor":
                String authorId = request.getParameter("manufactorId");
                Manufactor editManufactor = manufactorFacade.find(Long.parseLong(authorId));
                request.setAttribute("manufactor", editManufactor);
                
                request.getRequestDispatcher("/editManufactor.jsp").forward(request, response);
                break;
            case "/updateManufactor":
                authorId = request.getParameter("manufactorId");
                Manufactor updateManufactor = manufactorFacade.find(Long.parseLong(authorId));
                name = request.getParameter("name");
                country = request.getParameter("country");
                city = request.getParameter("city");
                address = request.getParameter("address");
                
                if("".equals(name) || "".equals(country)
                        || "".equals(city) || "".equals(address)){
                    request.setAttribute("info", "?????????????????? ?????? ????????");
                    request.setAttribute("author", updateManufactor);
                    request.getRequestDispatcher("/editManufactor").forward(request, response);
                    break;
                }
                updateManufactor.setName(name);
                updateManufactor.setCountry(country);
                updateManufactor.setCity(city);
                updateManufactor.setAddress(address);
                
                manufactorFacade.edit(updateManufactor);
                request.setAttribute("info", "?????????????????????????? ????????????????");
                request.getRequestDispatcher("/editManufactor").forward(request, response);
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
