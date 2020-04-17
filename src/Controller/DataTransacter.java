package Controller;

import Model.*;
import Util.HibernateUtil;
import java.sql.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class DataTransacter {

    public List select(String txt) {
        List re = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(txt);
            re = query.list();
            session.close();
        } catch (Exception e) {
            System.out.println("Lỗi select data \n" + e);
        }
        return re;
    }

    public boolean insert(Object obj) {
        boolean re=false;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(obj);
            session.getTransaction().commit();
            session.close();
            re=true;
        } catch (Exception e) {
            System.out.println("Lỗi insert data \n" + e);
        }
        return re;
    }

    public boolean delete(Object obj) {
        boolean re=false;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(obj);
            session.getTransaction().commit();
            session.close();
            re=true;
        } catch (Exception e) {
            System.out.println("Lỗi delete dataTransacter \n" + e);
        }
        return re;
    }

    public boolean update(Object obj) {
        boolean re = false;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(obj);
            session.getTransaction().commit();
            session.close();
            re=true;
        } catch (Exception e) {
            System.out.println("Lỗi update data \n" + e);
        }
        return re;
    }
}
