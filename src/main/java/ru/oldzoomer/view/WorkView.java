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
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.oldzoomer.dto.WorkDTO;
import ru.oldzoomer.service.WorkService;
import ru.oldzoomer.view.util.DialogUtil;

@Route(value = "works", layout = MainView.class)
@PageTitle("Работы")
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
@Component
@Scope("prototype")
@Log4j2
public class WorkView extends VerticalLayout {

    private final Grid<WorkDTO> grid;
    private final WorkService workService;

    public WorkView(WorkService workService) {
        this.workService = workService;
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
        dialog.getFooter().add(buttonLayout);
        dialog.open();
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        TextField name = new TextField();
        TextField normalHours = new TextField();
        TextField pricePerHour = new TextField();

        // Create local binder instance
        BeanValidationBinder<WorkDTO> binder = new BeanValidationBinder<>(WorkDTO.class);

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

        // Use FormLayout instead of responsive layout
        FormLayout formLayout = new FormLayout();
        formLayout.addFormItem(name, "Название");
        formLayout.addFormItem(normalHours, "Нормо-часов");
        formLayout.addFormItem(pricePerHour, "Цена за час");

        DialogUtil.openAddDialog(dialog, formLayout, binder, save, cancel, new WorkDTO());
    }

    private void openEditDialog(WorkDTO work) {
        Dialog dialog = new Dialog();
        TextField name = new TextField();
        TextField normalHours = new TextField();
        TextField pricePerHour = new TextField();

        name.setPlaceholder(work.getName());
        normalHours.setPlaceholder(work.getNormalHours().toString());
        pricePerHour.setPlaceholder(work.getPricePerHour().toString());

        // Create local binder instance
        BeanValidationBinder<WorkDTO> binder = new BeanValidationBinder<>(WorkDTO.class);

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

        // Use FormLayout instead of responsive layout
        FormLayout formLayout = new FormLayout();
        formLayout.addFormItem(name, "Название");
        formLayout.addFormItem(normalHours, "Нормо-часов");
        formLayout.addFormItem(pricePerHour, "Цена за час");

        DialogUtil.openEditDialog(dialog, formLayout, binder, save, cancel, work);
    }

    private void refreshGrid() {
        grid.setItems(workService.getAllWorks());
    }
}
