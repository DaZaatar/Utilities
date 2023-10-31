package com.dazaatar.tablegenerator;

import com.dazaatar.UtilityMethods;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTMLTableGenerator<T> {
    // ---------------------------------------------
    // Constant fields
    //----------------------------------------------
    private static final String TABLE_ID_FORMAT = "%s_Table_%d";

    // ---------------------------------------------
    // Private final fields
    //----------------------------------------------
    private final Class<T> typeClass;
    private final List<Field> fields;

    // ---------------------------------------------
    // Private fields
    //----------------------------------------------
    private List<T> data;
    private Document doc;
    private String tableId;
    private String defaultTableStyle;
    private String defaultHeadStyle;
    private String defaultRowStyle;

    public HTMLTableGenerator(Class<T> typeClass){
        this.typeClass = typeClass;
        this.fields = Arrays.stream(typeClass.getDeclaredFields()).toList();
        this.tableId = String.format(TABLE_ID_FORMAT, typeClass.getSimpleName(), UtilityMethods.generateRandomNumber());
    }

    public String build() throws IllegalAccessException, ParserConfigurationException, TransformerException {
        getDocument();
        Element table = createTable();

        Element headerRow = createHeadRowElement();
        table.appendChild(headerRow);

        for(T d: data){
            Element dataRow = createDataRowElement(d);
            table.appendChild(dataRow);
        }

        return transformDocumentToString();
    }

    private void getDocument() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        this.doc = documentBuilder.newDocument();
    }

    private Element createTable(){
        Element rootElement = doc.createElement("table");
        rootElement.setAttribute("id", tableId);
        doc.appendChild(rootElement);
        return rootElement;
    }

    private Element createRow(List<Object> data, String cellType){
        Element rowElement = doc.createElement("tr");
        for(Object d: data){
            Element cellElement = createCell(d, cellType);
            rowElement.appendChild(cellElement);
        }
        return rowElement;
    }

    private Element createCell(Object data, String cellType){
        Element cellElement = doc.createElement(cellType);
        Text textNode = doc.createTextNode(data.toString());
        cellElement.appendChild(textNode);
        return cellElement;
    }

    private Element createHeadRowElement(){
        List<Object> fieldNames = new ArrayList<>();

        for(Field field: fields){
            field.setAccessible(true);
            String fieldName = UtilityMethods.getFieldNameWithAnnotation(field, Header.class);
            fieldNames.add(fieldName);
        }

        return createRow(fieldNames, "th");
    }

    private Element createDataRowElement(T data) throws IllegalAccessException {
        List<Object> fieldValues = new ArrayList<>();

        for(Field field: fields){
            Object value = field.get(data);
            fieldValues.add(value);
        }

        return createRow(fieldValues, "td");
    }

    private String transformDocumentToString() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new StringWriter());
        transformer.transform(source, result);
        return result.getWriter().toString();
    }

    public void setData(List<T> data){
        this.data = data;
    }

    public void setDefaultTableStyle(String defaultTableStyle) {
        this.defaultTableStyle = defaultTableStyle;
    }

    public void setDefaultHeadStyle(String defaultHeadStyle) {
        this.defaultHeadStyle = defaultHeadStyle;
    }

    public void setDefaultRowStyle(String defaultRowStyle) {
        this.defaultRowStyle = defaultRowStyle;
    }
}
