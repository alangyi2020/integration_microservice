package com.dxc.hibernate;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class sqlQuery {
    private String rv;
    public String getQueryResults(String sqlQuery) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        SQLQuery query = session.createSQLQuery(sqlQuery);

        @SuppressWarnings("unchecked")
        List<Integer[]> rows =  query.list();
        rv = "";
        for (int i = 0; i < rows.size(); i++) {
            rv  = rv + String.valueOf(rows.get(i)) + "|";
            System.out.println("rv =" + rv );



        }
        rv = rv.substring(0,rv.length() - 1);
        //session.close();
        System.out.println("return value = " + rv);
        return rv;
    }
}
