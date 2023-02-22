/**
 * @author Hexanome H4124
 */

module fr.deliverapp {
    requires com.sothawo.mapjfx;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.slf4j;
    requires java.xml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens fr.deliverapp.controller to javafx.fxml;
    opens fr.deliverapp to javafx.fxml, javafx.graphics;

    exports fr.deliverapp;
    exports fr.deliverapp.view;
    exports fr.deliverapp.controller.command;
    exports fr.deliverapp.controller;
    exports fr.deliverapp.model.data;
    exports fr.deliverapp.model.objects;
    exports fr.deliverapp.model.package_diagram;

    opens fr.deliverapp.view to javafx.fxml, javafx.graphics;
}