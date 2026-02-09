package ru.oldzoomer.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainView extends AppLayout {

    public MainView() {
        // Header with drawer toggle
        H1 title = new H1("Спаркавто CRM");
        DrawerToggle toggle = new DrawerToggle();
        
        HorizontalLayout header = new HorizontalLayout(toggle, title);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassName("header");

        addToNavbar(header);

        // Drawer with navigation links
        RouterLink clientsLink = new RouterLink("Клиенты", ClientView.class);
        RouterLink ordersLink = new RouterLink("Заказы", OrderView.class);
        RouterLink worksLink = new RouterLink("Рaботы", WorkView.class);
        VerticalLayout drawerContent = new VerticalLayout(clientsLink, ordersLink, worksLink);
        drawerContent.setSizeFull();
        addToDrawer(drawerContent);
    }
}
