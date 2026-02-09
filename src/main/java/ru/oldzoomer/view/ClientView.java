package ru.oldzoomer.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.oldzoomer.dto.ClientDTO;
import ru.oldzoomer.service.ClientService;

@Route(value = "clients", layout = MainView.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Component
@Validated
@Scope("prototype")
@Log4j2
public class ClientView extends VerticalLayout {

    private final Grid<ClientDTO> grid;
    private final ClientService clientService;
    private final BeanValidationBinder<ClientDTO> binder;

    public ClientView(ClientService clientService) {
        this.clientService = clientService;
        this.binder = new BeanValidationBinder<>(ClientDTO.class);
        this.grid = new Grid<>(ClientDTO.class, false);
        grid.addColumn(ClientDTO::getId).setHeader("ID").setVisible(false);
        grid.addColumn(c -> c.getName() + " " + c.getSurname()).setHeader("ФИО");
        grid.addColumn(ClientDTO::getMiddleName).setHeader("Отчество");
        grid.addColumn(ClientDTO::getVinNumber).setHeader("VIN");
        grid.addColumn(ClientDTO::getPhone).setHeader("Телефон");
        grid.addColumn(ClientDTO::getEmail).setHeader("Эл. почта");
        // Add delete button column
        grid.addComponentColumn(client -> new Button("Удалить", _ -> deleteClient(client))).setHeader("Удаление");
        grid.setItems(clientService.getAllClients());
        
        // Make grid responsive
        grid.setMinWidth("300px");
        grid.setWidth("100%");

        Button addBtn = new Button("Добавить клиента", _ -> openAddDialog());
        addBtn.setWidthFull();
        
        add(addBtn, grid);
        setPadding(true);
        setSpacing(true);
    }

    private void deleteClient(ClientDTO client) {
        Dialog dialog = new Dialog();
        dialog.add("Вы уверены, что хотите удалить клиента " + client.getName() + " " + client.getSurname() + "?");

        Button confirm = new Button("Удалить", _ -> {
            clientService.deleteClient(client.getId());
            refreshGrid();
            dialog.close();
        });
        Button cancel = new Button("Отмена", _ -> dialog.close());
        
        HorizontalLayout buttonLayout = new HorizontalLayout(confirm, cancel);
        buttonLayout.setSpacing(true);
        dialog.add(buttonLayout);
        dialog.open();
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
                .bind("name");
        binder.forField(surname)
                .bind("surname");
        binder.forField(middleName)
                .bind("middleName");
        binder.forField(vin)
                .bind("vinNumber");
        binder.forField(phone)
                .bind("phone");
        binder.forField(email)
                .bind("email");

        Button save = new Button("Сохранить", _ -> {
            ClientDTO client = new ClientDTO();
            try {
                binder.writeBean(client);
                clientService.saveClient(client);
                refreshGrid();
                dialog.close();
            } catch (Exception e) {
                log.error(e);
            }
        });
        Button cancel = new Button("Отмена", _ -> dialog.close());
        
        // Make form responsive for mobile
        VerticalLayout formLayout = new VerticalLayout(name, surname, middleName, vin, phone, email);
        formLayout.setSpacing(false);
        formLayout.setPadding(false);
        HorizontalLayout buttonLayout = new HorizontalLayout(save, cancel);
        buttonLayout.setSpacing(true);
        dialog.add(formLayout, buttonLayout);
        dialog.setWidth("90vw");
        dialog.setHeight("90vh");
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(true);
        dialog.open();
    }

    private void refreshGrid() {
        grid.setItems(clientService.getAllClients());
    }
}
