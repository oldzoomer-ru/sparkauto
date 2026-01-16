package ru.oldzoomer.view;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import ru.oldzoomer.model.Client;
import ru.oldzoomer.service.ClientService;

@Route(value = "clients", layout = MainView.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Component
@Validated
public class ClientView extends VerticalLayout {

    private final Grid<Client> grid;
    private final ClientService clientService;
    private final BeanValidationBinder<Client> binder;

    public ClientView(ClientService clientService) {
        this.clientService = clientService;
        this.binder = new BeanValidationBinder<>(Client.class);
        this.grid = new Grid<>(Client.class, false);
        grid.addColumn(Client::getId).setHeader("ID").setVisible(false);
        grid.addColumn(c -> c.getName() + " " + c.getSurname()).setHeader("ФИО");
        grid.addColumn(Client::getMiddleName).setHeader("Отчество");
        grid.addColumn(Client::getVinNumber).setHeader("VIN");
        grid.addColumn(Client::getPhone).setHeader("Телефон");
        grid.addColumn(Client::getEmail).setHeader("Эл. почта");
        grid.setItems(clientService.getAllClients());

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

        // Bind fields to binder
        binder.forField(name)
                .asRequired("Имя не может быть пустым")
                .bind(Client::getName, Client::setName);
        binder.forField(surname)
                .asRequired("Фамилия не может быть пустой")
                .bind(Client::getSurname, Client::setSurname);
        binder.forField(middleName)
                .bind(Client::getMiddleName, Client::setMiddleName);
        binder.forField(vin)
                .withValidator(value -> value == null || value.isEmpty() || value.matches("^[A-HJ-NP-TV-Z0-9]{17}$"),
                        "VIN номер должен содержать 17 символов")
                .bind(Client::getVinNumber, Client::setVinNumber);
        binder.forField(phone)
                .withValidator(value -> value == null || value.isEmpty() || value.matches("^\\+?\\d{10,15}$"),
                        "Номер телефона должен содержать от 10 до 15 цифр")
                .bind(Client::getPhone, Client::setPhone);
        binder.forField(email)
                .asRequired("Email не может быть пустым")
                .withValidator(value -> value == null || value.isEmpty() || value.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"),
                        "Некорректный формат email")
                .bind(Client::getEmail, Client::setEmail);

        Button save = new Button("Сохранить", ev -> {
            Client client = new Client();
            try {
                binder.writeBean(client);
                clientService.saveClient(client);
                refreshGrid();
                dialog.close();
            } catch (Exception e) {
                // Validation errors are automatically displayed by the binder
            }
        });
        Button cancel = new Button("Отмена", ev -> dialog.close());
        dialog.add(name, surname, middleName, vin, phone, email, new HorizontalLayout(save, cancel));
        dialog.open();
    }

    private void refreshGrid() {
        grid.setItems(clientService.getAllClients());
    }
}
