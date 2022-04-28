<%-- 
    Document   : index
    Created on : Jan 20, 2022, 2:22:05 PM
    Author     : Melnikov
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

        <div class="card border-light my-5" style="width: 30rem;">
            <div class="card-body">
                <form action="updateBook" method="POST" multiple>
                    <fieldset>
                      <legend>Редактирование данных книги</legend>
                      <div class="form-group mb-3">
                        <label for="caption">Название книги</label>
                        <input type="hidden" name="bookId" value="${book.id}">
                        <input type="text" class="form-control" id="caption" name="caption" aria-describedby="caption" placeholder="" value="${book.caption}">
                        <small id="caption" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                        <label for="authors">Авторы</label>
                        <select multiple="multiple" class="form-select" id="authors" name="listAuthors">
                          <c:forEach var="entry" items="${authorsMap}">
                              <option ${entry.value} value="${entry.key.id}">${entry.key.name} ${entry.key.lastname}. ${entry.key.year}</option>
                          </c:forEach>
                        </select>
                      <div class="form-group mt-3">
                        <label for="publishedYear">Год издания</label>
                        <input type="text" class="form-control" id="publishedYear" name="publishedYear" aria-describedby="publishedYear" placeholder="" value="${book.publishedYear}">
                        <small id="publishedYear" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                      <div class="form-group  mt-3">
                        <label for="price">Цена</label>
                        <input type="text" class="form-control" id="price" name="price" aria-describedby="price" placeholder="" value="${book.price}">
                        <small id="price" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>


                        <button type="submit" class="btn btn-primary mt-4">Изменить данные</button>
                    </fieldset>
               </form>
            </div>
        </div>
     
