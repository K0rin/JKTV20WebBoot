<%-- 
    Document   : index
    Created on : Jan 20, 2022, 2:22:05 PM
    Author     : Melnikov
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
      <c:forEach var="boot" items="${boots}">
        <div class="card border-light mb-3" style="max-width: 20rem;">
            
            <div class="card-header">${boot.caption}</div>
            <img src="insertFile/${boot.cover}" style="max-height: 25rem;" class="card-img-top" alt="...">
            <div class="card-body">
              <h4 class="card-title">
                <c:forEach var="author" items="${boot.authors}">
                    ${author.name} ${author.country}. 
                </c:forEach>
              </h4>
                <p class="card-text">Год выпуска: ${boot.releaseyear}</p>
                <p class="card-text">Цена: ${boot.price}</p>
                <c:if test="${boot.quantity > 0}">
                    <a class="card-body" href="buyBoot?bootId=${boot.id}">Купить</a>
                </c:if>
                <c:if test="${boot.quantity == 0}">
                    <p class="card-text" style="color: red;">Нет в наличии</p>
                </c:if>    
                
                <c:if test="${role eq 'MANAGER' or role eq 'ADMINISTRATOR'}">
                    <a class="card-body" href="editBoot?bootId=${boot.id}">Редактировать</a>
                </c:if>
              <p class="card-text"></p>
            </div>
        </div>
      </c:forEach>

        
    
