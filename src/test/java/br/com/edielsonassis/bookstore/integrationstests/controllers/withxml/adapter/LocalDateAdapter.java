package br.com.edielsonassis.bookstore.integrationstests.controllers.withxml.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return (v != null) ? LocalDate.parse(v, formatter) : null;
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return (v != null) ? v.format(formatter) : null;
    }
}