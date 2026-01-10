package ru.oldzoomer.view;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import ru.oldzoomer.model.Client;
import ru.oldzoomer.model.Order;
import ru.oldzoomer.model.Work;
import ru.oldzoomer.repository.ClientRepository;
import ru.oldzoomer.repository.OrderRepository;
import ru.oldzoomer.repository.WorkRepository;

@Route(value = "orders", layout = MainView.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Component
public class OrderView extends VerticalLayout {

    private final Grid<Order> grid;
    private final OrderRepository orderRepo;
    private final ClientRepository clientRepo;
    private final WorkRepository workRepo;

    public OrderView(OrderRepository orderRepo, ClientRepository clientRepo, WorkRepository workRepo) {
        this.orderRepo = orderRepo;
        this.clientRepo = clientRepo;
        this.workRepo = workRepo; // store repository
        this.grid = new Grid<>(Order.class, false);
        grid.addColumn(Order::getId).setHeader("ID").setVisible(false);
        grid.addColumn(o -> o.getClient().getName() + " " + o.getClient().getSurname()).setHeader("Клиент");
        grid.addColumn(Order::getTotalHours).setHeader("Нормо-часов");
        grid.addColumn(Order::getTotalPrice).setHeader("Стоимость");
        // Edit button column
        grid.addComponentColumn(order -> new Button("Редактировать",
                e -> openEditDialog(order))).setHeader("Действия");
        refreshGrid();

        Button addBtn = new Button("Добавить заказ", e -> openAddDialog());
        add(addBtn, grid);
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        ComboBox<Client> clientSelect = new ComboBox<>("Клиент");
        clientSelect.setItems(clientRepo.findAll());
        clientSelect.setItemLabelGenerator(c -> c.getName() + " " + c.getSurname());

        // List of works with multi‑select
        MultiSelectListBox<Work> workList = new MultiSelectListBox<>();
        workList.setItems(workRepo.findAll());
        workList.setItemLabelGenerator(Work::getName);

        Button save = new Button("Сохранить", ev -> {
            Client selected = clientSelect.getValue();
            if (selected != null) {
                Order order = new Order();
                order.setClient(selected);
                // set chosen works
                order.setWorks(new ArrayList<>(workList.getSelectedItems()));
                orderRepo.save(order);
                refreshGrid();
                dialog.close();
            }
        });
        Button cancel = new Button("Отмена", ev -> dialog.close());
        dialog.add(clientSelect, workList, new HorizontalLayout(save, cancel));
        dialog.open();
    }

    private void openEditDialog(Order order) {
        Dialog dialog = new Dialog();
        ComboBox<Client> clientSelect = new ComboBox<>("Клиент");
        clientSelect.setItems(clientRepo.findAll());
        clientSelect.setItemLabelGenerator(c -> c.getName() + " " + c.getSurname());
        clientSelect.setValue(order.getClient());

        MultiSelectListBox<Work> workList = new MultiSelectListBox<>();
        workList.setItems(workRepo.findAll());
        workList.setItemLabelGenerator(Work::getName);
        // preselect existing works
        if (order.getWorks() != null) {
            workList.setValue(new HashSet<>(order.getWorks()));
        }

        Button save = new Button("Сохранить", ev -> {
            Client selected = clientSelect.getValue();
            if (selected != null) {
                order.setClient(selected);
                order.setWorks(new java.util.ArrayList<>(workList.getSelectedItems()));
                orderRepo.save(order);
                refreshGrid();
                dialog.close();
            }
        });
        Button cancel = new Button("Отмена", ev -> dialog.close());
        dialog.add(clientSelect, workList, new HorizontalLayout(save, cancel));
        dialog.open();
    }

    private void refreshGrid() {
        grid.setItems(orderRepo.findAll());
    }
}
