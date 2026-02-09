package ru.oldzoomer.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.oldzoomer.dto.ClientDTO;
import ru.oldzoomer.service.ClientService;
import ru.oldzoomer.view.util.DialogUtil;

@Route(value = "", layout = MainView.class)
@RouteAlias(value = "clients", layout = MainView.class)
@PageTitle("Клиенты")
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Component
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
            try {
                clientService.deleteClient(client.getId());
                refreshGrid();
                dialog.close();
                Notification.show("Клиент успешно удален");
            } catch (Exception e) {
                log.error("Error deleting client: {}", client.getId(), e);
                dialog.close();
                Notification.show("Ошибка при удалении клиента", 3000, Notification.Position.MIDDLE);
            }
        });
        Button cancel = new Button("Отмена", _ -> dialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(confirm, cancel);
        buttonLayout.setSpacing(true);
        dialog.getFooter().add(buttonLayout);
        dialog.open();
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        TextField name = new TextField();
        TextField surname = new TextField();
        TextField middleName = new TextField();
        TextField vin = new TextField();
        TextField phone = new TextField();
        TextField email = new TextField();

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
                Notification.show("Клиент успешно добавлен");
            } catch (Exception e) {
                log.error("Error saving client", e);
                Notification.show("Ошибка при сохранении клиента", 3000, Notification.Position.MIDDLE);
            }
        });
        Button cancel = new Button("Отмена", _ -> dialog.close());

        // Use FormLayout instead of responsive layout
        FormLayout formLayout = new FormLayout();
        formLayout.addFormItem(name, "Имя");
        formLayout.addFormItem(surname, "Фамилия");
        formLayout.addFormItem(middleName, "Отчество");
        formLayout.addFormItem(vin, "VIN");
        formLayout.addFormItem(phone, "Телефон");
        formLayout.addFormItem(email, "Эл. почта");

        DialogUtil.openAddDialog(dialog, formLayout, binder, save, cancel, new ClientDTO());
    }

    private void refreshGrid() {
        grid.setItems(clientService.getAllClients());
    }
}
