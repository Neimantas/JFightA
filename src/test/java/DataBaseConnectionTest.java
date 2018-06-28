
import services.IDatabase;
import services.impl.DatabaseImpl;

public class DataBaseConnectionTest {

	public static void main(String[] args) {
		
		IDatabase database = new DatabaseImpl();
		database.connect();
		database.closeConnection();

	}

}
