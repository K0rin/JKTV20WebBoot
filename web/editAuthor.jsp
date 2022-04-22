<%-- 
    Document   : index
    Created on : Jan 20, 2022, 2:22:05 PM
    Author     : Melnikov
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
        <div class="card border-light my-5" style="width: 30rem;">
            <div class="card-body">
                <form action="updateAuthor" method="POST">
                    <fieldset>
                      <legend>Редактирование автора</legend>
                      <div class="form-group mb-3">
                        <label for="name">Имя</label>
                        <input type="hidden" name="authorId" value="${author.id}">
                        <input type="text" class="form-control" id="name" name="name" aria-describedby="name" placeholder="" value="${author.name}">
                        <small id="caption" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                        
                      <div class="form-group mt-3">
                        <label for="lastname">Фамилия</label>
                        <input type="text" class="form-control" id="lastname" name="lastname" aria-describedby="lastname" placeholder="" value="${author.lastname}">
                        <small id="publishedYear" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                      <div class="form-group  mt-3">
                        <label for="year">Год рождения</label>
                        <input type="text" class="form-control" id="year" name="year" aria-describedby="year" placeholder="" value="${author.year}">
                        <small id="quantity" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                      <div class="form-group  mt-3">
                        <label for="day">День рождения</label>
                        <input type="text" class="form-control" id="day" name="day" aria-describedby="day" placeholder="" value="${author.day}">
                        <small id="quantity" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                      <div class="form-group  mt-3">
                        <label for="month">Месяц рождения</label>
                        <input type="text" class="form-control" id="month" name="month" aria-describedby="month" placeholder="" value="${author.month}">
                        <small id="quantity" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                        <button type="submit" class="btn btn-primary mt-4">Изменить автора</button>
                    </fieldset>
               </form>
            </div>
        </div>
        