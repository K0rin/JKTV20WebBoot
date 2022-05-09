<%-- 
    Document   : index
    Created on : Jan 20, 2022, 2:22:05 PM
    Author     : Melnikov
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
      <c:forEach var="book" items="${books}">
        <div class="card border-light mb-3" style="max-width: 20rem;">
            
            <div class="card-header">${book.caption}</div>
            <img src="insertFile/${book.cover}" style="max-height: 25rem;" class="card-img-top" alt="...">
            <div class="card-body">
              <h4 class="card-title">
                <c:forEach var="author" items="${book.authors}">
                    ${author.name} ${author.lastname}. ${author.year}. 
                </c:forEach>
              </h4>
                <p class="card-text">${book.publishedYear}</p>
                <a class="card-body" href="buyBook?bookId=${book.id}">Купить</a>
                <c:if test="${role eq 'MANAGER' or role eq 'ADMINISTRATOR'}">
                    <a class="card-body" href="editBook?bookId=${book.id}">Редактировать</a>
                </c:if>
              <p class="card-text"></p>
            </div>
        </div>
      </c:forEach>

        
    
