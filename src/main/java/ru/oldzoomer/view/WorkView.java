package ru.oldzoomer.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
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
import ru.oldzoomer.dto.WorkDTO;
import ru.oldzoomer.service.WorkService;

@Route(value = "works", layout = MainView.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Component
@Validated
@Scope("prototype")
@Log4j2
public class WorkView extends VerticalLayout {

    private final Grid<WorkDTO> grid;
    private final WorkService workService;
    private final BeanValidationBinder<WorkDTO> binder;

    public WorkView(WorkService workService) {
        this.workService = workService;
        this.binder = new BeanValidationBinder<>(WorkDTO.class);
        this.grid = new Grid<>(WorkDTO.class, false);
        grid.addColumn(WorkDTO::getId).setHeader("ID").setVisible(false);
        grid.addColumn(WorkDTO::getName).setHeader("Название");
        grid.addColumn(WorkDTO::getNormalHours).setHeader("Нормо-часов");
        grid.addColumn(WorkDTO::getPricePerHour).setHeader("Цена за час");
        // Add delete and edit button columns
        grid.addComponentColumn(work -> new Button("Редактировать", _ -> openEditDialog(work))).setHeader("Редактирование");
        grid.addComponentColumn(work -> new Button("Удалить", _ -> deleteWork(work))).setHeader("Удаление");
        refreshGrid();

        Button addBtn = new Button("Добавить работу", _ -> openAddDialog());
        addBtn.setWidthFull();
        
        add(addBtn, grid);
        setPadding(true);
        setSpacing(true);
    }

    private void deleteWork(WorkDTO work) {
        Dialog dialog = new Dialog();
        dialog.add("Вы уверены, что хотите удалить работу '" + work.getName() + "'?");

        Button confirm = new Button("Удалить", _ -> {
            try {
                workService.deleteWork(work.getId());
                refreshGrid();
                dialog.close();
                Notification.show("Работа успешно удалена");
            } catch (Exception e) {
                log.error("Error deleting work: {}", work.getId(), e);
                dialog.close();
                Notification.show("Ошибка при удалении работы", 3000, Notification.Position.MIDDLE);
            }
        });
        Button cancel = new Button("Отмена", _ -> dialog.close());
        
        HorizontalLayout buttonLayout = new HorizontalLayout(confirm, cancel);
        buttonLayout.setSpacing(true);
        dialog.add(buttonLayout);
        dialog.open();
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

        Button save = new Button("Сохранить", _ -> {
            WorkDTO work = new WorkDTO();
            try {
                binder.writeBean(work);
                workService.saveWork(work);
                refreshGrid();
                dialog.close();
                Notification.show("Работа успешно добавлена");
            } catch (Exception e) {
                log.error("Error saving work", e);
                Notification.show("Ошибка при сохранении работы", 3000, Notification.Position.MIDDLE);
            }
        });
        Button cancel = new Button("Отмена", _ -> dialog.close());
        
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

    private void openEditDialog(WorkDTO work) {
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
        binder.setBean(work);

        Button save = new Button("Сохранить", _ -> {
            try {
                binder.writeBean(work);
                workService.saveWork(work);
                refreshGrid();
                dialog.close();
                Notification.show("Работа успешно обновлена");
            } catch (Exception e) {
                log.error("Error updating work: {}", work.getId(), e);
                Notification.show("Ошибка при сохранении работы", 3000, Notification.Position.MIDDLE);
            }
        });
        Button cancel = new Button("Отмена", _ -> dialog.close());
        
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
