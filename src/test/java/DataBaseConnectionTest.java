
import java.sql.SQLException;

import services.IDatabase;
import services.impl.DatabaseImpl;

public class DataBaseConnectionTest {

	public static void main(String[] args) {

		IDatabase database = new DatabaseImpl();
		try {
			database.connect();
			database.closeConnection();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
