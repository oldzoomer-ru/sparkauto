package ru.oldzoomer.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.oldzoomer.model.Client;
import ru.oldzoomer.model.Order;
import ru.oldzoomer.model.Work;
import ru.oldzoomer.service.ClientService;
import ru.oldzoomer.service.OrderService;
import ru.oldzoomer.service.WorkService;

import java.util.ArrayList;
import java.util.HashSet;

@Route(value = "orders", layout = MainView.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Component
@Validated
@Scope("prototype")
@Log4j2
public class OrderView extends VerticalLayout {

    private final Grid<Order> grid;
    private final OrderService orderService;
    private final WorkService workService;
    private final ClientService clientService;
    private final BeanValidationBinder<Order> binder;

    public OrderView(OrderService orderService, WorkService workService, ClientService clientService) {
        this.orderService = orderService;
        this.workService = workService;
        this.clientService = clientService;
        this.binder = new BeanValidationBinder<>(Order.class);
        this.grid = new Grid<>(Order.class, false);
        grid.addColumn(Order::getId).setHeader("ID").setVisible(false);
        grid.addColumn(o -> o.getClient().getName() + " " + o.getClient().getSurname()).setHeader("Клиент");
        grid.addColumn(Order::getTotalHours).setHeader("Нормо-часов");
        grid.addColumn(Order::getTotalPrice).setHeader("Стоимость");
        // Add delete and edit button columns
        grid.addComponentColumn(order -> new Button("Редактировать",
                _ -> openEditDialog(order))).setHeader("Редактирование");
        grid.addComponentColumn(order -> new Button("Удалить", _ -> deleteOrder(order))).setHeader("Удаление");
        refreshGrid();

        Button addBtn = new Button("Добавить заказ", _ -> openAddDialog());
        addBtn.setWidthFull();
        
        add(addBtn, grid);
        setPadding(true);
        setSpacing(true);
    }

    private void deleteOrder(Order order) {
        Dialog dialog = new Dialog();
        dialog.add("Вы уверены, что хотите удалить заказ клиента " + order.getClient().getName() + " " + order.getClient().getSurname() + "?");

        Button confirm = new Button("Удалить", _ -> {
            orderService.deleteOrder(order.getId());
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
        ComboBox<Client> clientSelect = new ComboBox<>("Клиент");
        clientSelect.setItems(clientService.getAllClients());
        clientSelect.setItemLabelGenerator(c -> c.getName() + " " + c.getSurname());

        // List of works with multi‑select
        MultiSelectListBox<Work> workList = new MultiSelectListBox<>();
        workList.setItems(workService.getAllWorks());
        workList.setItemLabelGenerator(Work::getName);

        // Bind fields to binder
        binder.forField(clientSelect)
                .bind("client");

        Button save = new Button("Сохранить", _ -> {
            Order order = new Order();
            try {
                binder.writeBean(order);
                // Set works manually since they're not directly bound
                order.setWorks(new ArrayList<>(workList.getSelectedItems()));
                orderService.saveOrder(order);
                refreshGrid();
                dialog.close();
            } catch (Exception e) {
                log.error(e);
            }
        });
        Button cancel = new Button("Отмена", _ -> dialog.close());
        
        // Make form responsive for mobile
        VerticalLayout formLayout = new VerticalLayout(clientSelect, workList);
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

    private void openEditDialog(Order order) {
        Dialog dialog = new Dialog();
        ComboBox<Client> clientSelect = new ComboBox<>("Клиент");
        clientSelect.setItems(clientService.getAllClients());
        clientSelect.setItemLabelGenerator(c -> c.getName() + " " + c.getSurname());
        clientSelect.setValue(order.getClient());

        MultiSelectListBox<Work> workList = new MultiSelectListBox<>();
        workList.setItems(workService.getAllWorks());
        workList.setItemLabelGenerator(Work::getName);
        // preselect existing works
        if (order.getWorks() != null) {
            workList.setValue(new HashSet<>(order.getWorks()));
        }

        // Bind fields to binder
        binder.forField(clientSelect)
                .bind("client");
        binder.setBean(order);

        Button save = new Button("Сохранить", _ -> {
            try {
                binder.writeBean(order);
                // Set works manually since they're not directly bound
                order.setWorks(new ArrayList<>(workList.getSelectedItems()));
                orderService.saveOrder(order);
                refreshGrid();
                dialog.close();
            } catch (Exception _) {
                // Validation errors are automatically displayed by the binder
            }
        });
        Button cancel = new Button("Отмена", _ -> dialog.close());
        
        // Make form responsive for mobile
        VerticalLayout formLayout = new VerticalLayout(clientSelect, workList);
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
        grid.setItems(orderService.getAllOrders());
    }
}
