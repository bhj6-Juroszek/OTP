package com.example.gui.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;

@org.springframework.stereotype.Component
@Title( "Training Platform" )
@SpringUI(path = "/")
@Theme("mytheme")
public class DashboardUI extends UI {
 private Navigator navigator;
    private String password;
    private Panel loginPanel=new Panel();
    private GoogleMap googleMap=new GoogleMap(null, null, null);

    @Autowired
    private SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout root = new VerticalLayout();
        setContent(root);

        Panel panel = new Panel();
        root.addComponent(panel);
        this.navigator = new Navigator(this, root);
        navigator.addProvider(viewProvider);
        navigator.navigateTo("mainView");

    }
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DashboardUI.class, widgetset = "com.vaadin.tapio.googlemaps.demo.DemoWidgetset")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    public void detach() {
        super.detach();
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Panel getLoginPanel() {
        return loginPanel;
    }

    public void setLoginPanel(Panel loginPanel) {
        this.loginPanel = loginPanel;
    }



    public GoogleMap getGoogleMap(double lat, double lng, String name, String info)
    {

        VerticalLayout mapContent = new VerticalLayout();
        GoogleMapMarker kakolaMarker = new GoogleMapMarker(
                name, new LatLon(lat, lng),
                false, null);
        GoogleMapInfoWindow kakolaInfoWindow = new GoogleMapInfoWindow(
                info, kakolaMarker);
        mapContent.setSizeFull();
        googleMap = new GoogleMap("AIzaSyCOySy9jH746ssXG4h6BIqaeZP9Rz7EIq0", null, null);
        googleMap.setCenter(new LatLon(lat, lng));
        googleMap.setZoom(14);
        googleMap.setSizeFull();
        googleMap.setWidth("500px");
        googleMap.setHeight("500px");
        kakolaMarker.setAnimationEnabled(false);
        googleMap.addMarker(kakolaMarker);
        kakolaInfoWindow.setWidth("400px");
        kakolaInfoWindow.setHeight("500px");

        mapContent.addComponent(googleMap);
        mapContent.setExpandRatio(googleMap, 1.0f);
        return googleMap;
    }

}
