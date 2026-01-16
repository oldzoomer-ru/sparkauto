package ru.oldzoomer.view;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import ru.oldzoomer.model.Work;
import ru.oldzoomer.service.WorkService;

@Route(value = "works", layout = MainView.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Component
@Validated
public class WorkView extends VerticalLayout {

    private final Grid<Work> grid;
    private final WorkService workService;

    public WorkView(WorkService workService) {
        this.workService = workService;
        this.grid = new Grid<>(Work.class, false);
        grid.addColumn(Work::getId).setHeader("ID").setVisible(false);
        grid.addColumn(Work::getName).setHeader("Название");
        grid.addColumn(Work::getNormalHours).setHeader("Нормо-часов");
        grid.addColumn(Work::getPricePerHour).setHeader("Цена за час");
        // Optionally add actions column
        grid.addComponentColumn(work -> new Button("Редактировать", e -> openEditDialog(work))).setHeader("Действия");
        refreshGrid();

        Button addBtn = new Button("Добавить работу", e -> openAddDialog());
        add(addBtn, grid);
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        TextField name = new TextField("Название");
        TextField normalHours = new TextField("Нормо-часов");
        TextField pricePerHour = new TextField("Цена за час");

        Button save = new Button("Сохранить", ev -> {
            Work work = new Work();
            work.setName(name.getValue());
            try {
                work.setNormalHours(Double.valueOf(normalHours.getValue()));
            } catch (NumberFormatException ex) {
                // ignore or set null
            }
            try {
                work.setPricePerHour(Double.valueOf(pricePerHour.getValue()));
            } catch (NumberFormatException ex) {
                // ignore or set null
            }
            workService.saveWork(work);
            refreshGrid();
            dialog.close();
        });
        Button cancel = new Button("Отмена", ev -> dialog.close());
        dialog.add(name, normalHours, pricePerHour, new HorizontalLayout(save, cancel));
        dialog.open();
    }

    private void openEditDialog(Work work) {
        Dialog dialog = new Dialog();
        TextField name = new TextField("Название", work.getName());
        TextField normalHours = new TextField("Нормо-часов", String.valueOf(work.getNormalHours()));
        TextField pricePerHour = new TextField("Цена за час", String.valueOf(work.getPricePerHour()));

        Button save = new Button("Сохранить", ev -> {
            work.setName(name.getValue());
            try {
                work.setNormalHours(Double.valueOf(normalHours.getValue()));
            } catch (NumberFormatException ignored) {
                // Ignored
            }
            try {
                work.setPricePerHour(Double.valueOf(pricePerHour.getValue()));
            } catch (NumberFormatException ignored) {
                // Ignored
            }
            workService.saveWork(work);
            refreshGrid();
            dialog.close();
        });
        Button cancel = new Button("Отмена", ev -> dialog.close());
        dialog.add(name, normalHours, pricePerHour, new HorizontalLayout(save, cancel));
        dialog.open();
    }

    private void refreshGrid() {
        grid.setItems(workService.getAllWorks());
    }
}
