<%-- 
    Document   : index
    Created on : Jan 20, 2022, 2:22:05 PM
    Author     : Melnikov
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
        <div class="card border-light my-5" style="width: 30rem;">
            <div class="card-body">
                <form action="updateManufactor" method="POST">
                    <fieldset>
                      <legend>Редактирование автора</legend>
                      <div class="form-group mb-3">
                        <label for="name">Имя</label>
                        <input type="hidden" name="manufactorId" value="${manufactor.id}">
                        <input type="text" class="form-control" id="name" name="name" aria-describedby="name" placeholder="" value="${manufactor.name}">
                        <small id="caption" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                        
                      <div class="form-group mt-3">
                        <label for="country">Страна</label>
                        <input type="text" class="form-control" id="country" name="country" aria-describedby="country" placeholder="" value="${manufactor.country}">
                        <small id="country" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                      <div class="form-group  mt-3">
                        <label for="city">Город</label>
                        <input type="text" class="form-control" id="city" name="city" aria-describedby="city" placeholder="" value="${manufactor.city}">
                        <small id="city" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                      <div class="form-group  mt-3">
                        <label for="address">Адресс</label>
                        <input type="text" class="form-control" id="address" name="address" aria-describedby="address" placeholder="" value="${manufactor.address}">
                        <small id="address" class="form-text text-muted d-none">Это поле не должно быть пустым</small>
                      </div>
                      
                        <button type="submit" class="btn btn-primary mt-4">Изменить производителя</button>
                    </fieldset>
               </form>
            </div>
        </div>
        