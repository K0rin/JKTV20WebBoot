<%-- 
    Document   : index
    Created on : Jan 20, 2022, 2:22:05 PM
    Author     : Melnikov
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>

        <div class="card border-light my-5" style="width: 30rem;">
            <div class="card-body">
                <form action="createBook" method="POST">
                    <fieldset>
                      <legend>Добавление книги</legend>
                      <div class="form-group mb-3">
                        <label for="caption">Название книги</label>
                        <input type="text" class="form-control" id="caption" name="caption" aria-describedby="caption" placeholder="">
                        <small id="caption" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                        <label for="authors">Авторы</label>
                        <select multiple="" class="form-select" id="authors" name="authors">
                          <c:forEach var="author" items="${authors}">
                              <option value="${author.id}">${author.name} ${author.lastname}. ${author.year}</option>
                          </c:forEach>
                        </select>
                      <div class="form-group mt-3">
                        <label for="publishedYear">Год издания</label>
                        <input type="text" class="form-control" id="publishedYear" name="publishedYear" aria-describedby="publishedYear" placeholder="">
                        <small id="publishedYear" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                      <div class="form-group  mt-3">
                        <label for="price">Цена</label>
                        <input type="text" class="form-control" id="price" name="price" aria-describedby="price" placeholder="">
                        <small id="price" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                      <div class="form-group  mt-3">
                        <label for="cover">Обложка</label>
                        <input type="file" class="form-control" id="cover" name="cover" aria-describedby="cover" placeholder="">
                        <small id="cover" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>

                        <button type="submit" class="btn btn-primary mt-4">Добавить книгу</button>
                    </fieldset>
               </form>
            </div>
        </div>
     
