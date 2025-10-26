import java.sql.*
import org.mariadb.jdbc.internal.util.constant.Version

object jdbc_sample {
    @JvmStatic fun main(args:Array<String>) {
        var conn: Connection? = null
        var stmt: Statement? = null
        var resultSet: ResultSet
        try {
            Class.forName("org.mariadb.jdbc.Driver")
            println("MariaDB Connector/J: " + Version.version + "\n")
            print("Connecting to DB...")
            conn = DriverManager.getConnection(
                "jdbc:mariadb://192.168.2.104/mysql",
                "db_user", "db_passwd")
            println("done.")
            stmt = conn.createStatement()
            resultSet = stmt.executeQuery("SELECT user, host FROM mysql.user")
            println("\nList of MariaDB users:")
            while (resultSet.next()) {
                var user = resultSet.getString(1)
                var host = resultSet.getString(2)
                println(user + "@'" + host + "'")
            }
        }
        catch (ex: SQLException) {
            ex.printStackTrace()
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
        finally {
            try {
                if (stmt != null) {
                    conn?.close()
                }
            }
            catch (ex:SQLException) {}
            try {
                if (conn != null) {
                    conn.close()
                }
            }
            catch (ex: SQLException) {
                ex.printStackTrace()
            }
        }
    }
}