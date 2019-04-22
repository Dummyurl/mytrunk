package com.car.portal.XmlHandlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class DistrictParserHandler extends DefaultHandler {

    /**
     * 存储所有的解析对象
     */
    private List<DistrictModel> citiesModelList;

    public DistrictParserHandler() {

    }


    public List<DistrictModel> getDataList() {
        return citiesModelList;
    }

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一个开始标签的时候，会触发这个方法
        citiesModelList = new ArrayList<>();


    }

    private DistrictModel citiesModel;
    private String qName;

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (qName.equals("ROW")) {
            citiesModel = new DistrictModel();
        }
        this.qName = qName;
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if (qName.equals("ROW")) {

            citiesModelList.add(citiesModel);
        }


    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length);

        switch (qName) {
            case "parentCode":
                if (!value.trim().isEmpty())
                    citiesModel.setParentCode(Integer.parseInt(value));
                break;
            case "city":
                if (!value.trim().isEmpty())
                    citiesModel.setName(value);
                break;
            case "code":
                if (!value.trim().isEmpty())
                    citiesModel.setCode(Integer.parseInt(value));
                break;
            case "id":
                if (!value.trim().isEmpty())
                    citiesModel.setId(Integer.parseInt(value));
                break;
            case "cid":
                if (!value.trim().isEmpty())
                    citiesModel.setCid(Integer.parseInt(value));
                break;
        }


    }

}
