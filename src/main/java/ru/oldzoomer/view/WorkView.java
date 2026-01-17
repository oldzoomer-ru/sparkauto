package ru.oldzoomer.view;

import org.springframework.context.annotation.Scope;
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
import ru.oldzoomer.model.Work;
import ru.oldzoomer.service.WorkService;

@Route(value = "works", layout = MainView.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Component
@Validated
@Scope("prototype")
public class WorkView extends VerticalLayout {

    private final Grid<Work> grid;
    private final WorkService workService;
    private final BeanValidationBinder<Work> binder;

    public WorkView(WorkService workService) {
        this.workService = workService;
        this.binder = new BeanValidationBinder<>(Work.class);
        this.grid = new Grid<>(Work.class, false);
        grid.addColumn(Work::getId).setHeader("ID").setVisible(false);
        grid.addColumn(Work::getName).setHeader("Название");
        grid.addColumn(Work::getNormalHours).setHeader("Нормо-часов");
        grid.addColumn(Work::getPricePerHour).setHeader("Цена за час");
        // Optionally add actions column
        grid.addComponentColumn(work -> new Button("Редактировать", e -> openEditDialog(work))).setHeader("Действия");
        refreshGrid();

        Button addBtn = new Button("Добавить работу", e -> openAddDialog());
        addBtn.setWidthFull();
        
        add(addBtn, grid);
        setPadding(true);
        setSpacing(true);
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        TextField name = new TextField("Название");
        TextField normalHours = new TextField("Нормо-часов");
        TextField pricePerHour = new TextField("Цена за час");

        // Bind fields to binder
        binder.forField(name)
                .bind("name");
        binder.forField(normalHours)
                .withConverter(value -> value == null || value.isEmpty() ? null : Double.valueOf(value),
                        value -> value == null ? "" : value.toString())
                .bind("normalHours");
        binder.forField(pricePerHour)
                .withConverter(value -> value == null || value.isEmpty() ? null : Double.valueOf(value),
                        value -> value == null ? "" : value.toString())
                .bind("pricePerHour");

        Button save = new Button("Сохранить", ev -> {
            Work work = new Work();
            try {
                binder.writeBean(work);
                workService.saveWork(work);
                refreshGrid();
                dialog.close();
            } catch (Exception e) {
                // Validation errors are automatically displayed by the binder
            }
        });
        Button cancel = new Button("Отмена", ev -> dialog.close());
        
        // Make form responsive for mobile
        VerticalLayout formLayout = new VerticalLayout(name, normalHours, pricePerHour);
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

    private void openEditDialog(Work work) {
        Dialog dialog = new Dialog();
        TextField name = new TextField("Название", work.getName());
        TextField normalHours = new TextField("Нормо-часов", String.valueOf(work.getNormalHours()));
        TextField pricePerHour = new TextField("Цена за час", String.valueOf(work.getPricePerHour()));

        // Bind fields to binder
        binder.forField(name)
                .bind("name");
        binder.forField(normalHours)
                .withConverter(value -> value == null || value.isEmpty() ? null : Double.valueOf(value),
                        value -> value == null ? "" : value.toString())
                .bind("normalHours");
        binder.forField(pricePerHour)
                .withConverter(value -> value == null || value.isEmpty() ? null : Double.valueOf(value),
                        value -> value == null ? "" : value.toString())
                .bind("pricePerHour");

        Button save = new Button("Сохранить", ev -> {
            try {
                binder.writeBean(work);
                workService.saveWork(work);
                refreshGrid();
                dialog.close();
            } catch (Exception e) {
                // Validation errors are automatically displayed by the binder
            }
        });
        Button cancel = new Button("Отмена", ev -> dialog.close());
        
        // Make form responsive for mobile
        VerticalLayout formLayout = new VerticalLayout(name, normalHours, pricePerHour);
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
        grid.setItems(workService.getAllWorks());
    }
}
