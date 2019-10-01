/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.teste.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.teste.model.Itemtable;
import br.com.teste.util.HibernateUtil;

/**
 *
 * @author wesllen
 */
public class ItemtableDAO {
     private static ItemtableDAO instancia;
    private ItemtableDAO(){
    
    }
    
    public static synchronized ItemtableDAO getInstance(){
        if (instancia == null) instancia = new ItemtableDAO();
        return instancia;
    }
    
    private static Transaction transObj;
    private static Session sessionObj;
    
    public List<Itemtable> obterTodosOsItems(){
          sessionObj = HibernateUtil.getSessionFactory().openSession();
          List<Itemtable> lItemtable = null;
          try {
                Query query = sessionObj.createQuery ("from Itemtable");
                lItemtable = query.list();
                
               
            } catch (Exception e) {
                e.printStackTrace();
            }
              sessionObj.close();
              return lItemtable;
        }
    public Itemtable obterItemtablePorChave(Itemtable pItemtable){
        Itemtable lItemtable = null;
        sessionObj = HibernateUtil.getSessionFactory().openSession();
          try {
                Query query = sessionObj.createQuery ("from Itemtable where id = " + pItemtable.getId());
                lItemtable = (Itemtable)query.uniqueResult();
                
                
               
            } catch (Exception e) {
                e.printStackTrace();
            }
            
          sessionObj.close();
              return lItemtable;
        }
}
