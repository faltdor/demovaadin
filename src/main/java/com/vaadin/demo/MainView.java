package com.vaadin.demo;

import com.vaadin.demo.component.CustomerForm;
import com.vaadin.demo.model.Customer;
import com.vaadin.demo.service.CustomerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

/**
 * The main view contains a button and a template element.
 */
@HtmlImport("styles/shared-styles.html")
@Route("")
public class MainView extends VerticalLayout {

	private CustomerService service = CustomerService.getInstance();
    private Grid<Customer> grid = new Grid<>();
    private TextField filterText = new TextField();
    private CustomerForm form = new CustomerForm(this);
   
    public MainView() {
        grid.setSizeFull();
        grid.addColumn(Customer::getFirstName).setHeader("First Name");
        grid.addColumn(Customer::getLastName).setHeader("Last Name");
        grid.addColumn(Customer::getStatus).setHeader("Status");
        
        filterText.setPlaceholder("Filter by name...");
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());
        Button clearFilterTextBtn = new Button(
                new Icon(VaadinIcon.CLOSE_CIRCLE));
        clearFilterTextBtn.addClickListener(e -> filterText.clear());
        
        HorizontalLayout filtering = new HorizontalLayout(filterText, clearFilterTextBtn);
        
        Button addCustomerBtn = new Button("Add New Customer");
        addCustomerBtn.addClickListener(e -> {
        	grid.asSingleSelect().clear();
        	form.setCustomer(new Customer());
        });
        HorizontalLayout toolbar = new HorizontalLayout(filtering, addCustomerBtn);
		
		HorizontalLayout main = new HorizontalLayout(grid, form);
		main.setAlignItems(Alignment.START);
		main.setSizeFull();
		
        add(toolbar, main);
        setHeight("100vh");
        updateList();
        
        grid.asSingleSelect().addValueChangeListener(event -> {
        	form.setCustomer(event.getValue());
        });
        
    }
    
    public void updateList() {
        grid.setItems(service.findAll(filterText.getValue()));
    }
}
