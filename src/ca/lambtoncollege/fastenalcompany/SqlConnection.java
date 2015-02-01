/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.lambtoncollege.fastenalcompany;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JIAJUN XUE <nicoxue0324@gmail.com>
 */
public class SqlConnection {

    public void connect() {
        DBManager db = new DBManager();
        Connection conn = null;
        try {
            conn = db.getMysqlConnection();
            String query = "select * from inventory";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.println(rs.getString(i));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FastenalCompany.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FastenalCompany.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
