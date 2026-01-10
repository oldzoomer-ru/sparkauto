package ru.oldzoomer.view;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import ru.oldzoomer.model.Client;
import ru.oldzoomer.repository.ClientRepository;

@Route(value = "clients", layout = MainView.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Component
public class ClientView extends VerticalLayout {

    private final Grid<Client> grid;
    private final ClientRepository repository;

    public ClientView(ClientRepository repository) {
        this.repository = repository;
        this.grid = new Grid<>(Client.class, false);
        grid.addColumn(Client::getId).setHeader("ID").setVisible(false);
        grid.addColumn(c -> c.getName() + " " + c.getSurname()).setHeader("ФИО");
        grid.addColumn(Client::getMiddleName).setHeader("Отчество");
        grid.addColumn(Client::getVinNumber).setHeader("VIN");
        grid.addColumn(Client::getPhone).setHeader("Телефон");
        grid.addColumn(Client::getEmail).setHeader("Эл. почта");
        grid.setItems(repository.findAll());

        Button addBtn = new Button("Добавить клиента", e -> openAddDialog());
        add(addBtn, grid);
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        TextField name = new TextField("Имя");
        TextField surname = new TextField("Фамилия");
        TextField middleName = new TextField("Отчество");
        TextField vin = new TextField("VIN");
        TextField phone = new TextField("Телефон");
        TextField email = new TextField("Эл. почта");

        Button save = new Button("Сохранить", ev -> {
            Client client = new Client();
            client.setName(name.getValue());
            client.setSurname(surname.getValue());
            client.setMiddleName(middleName.getValue());
            client.setVinNumber(vin.getValue());
            client.setPhone(phone.getValue());
            client.setEmail(email.getValue());
            repository.save(client);
            refreshGrid();
            dialog.close();
        });
        Button cancel = new Button("Отмена", ev -> dialog.close());
        dialog.add(name, surname, middleName, vin, phone, email, new HorizontalLayout(save, cancel));
        dialog.open();
    }

    private void refreshGrid() {
        grid.setItems(repository.findAll());
    }
}


