import services.impl.DataBaseImpl;

public class DataBaseConnectionTest {

	public static void main(String[] args) {
		
		DataBaseImpl dataBaseImpl = new DataBaseImpl();
		dataBaseImpl.connect();

	}

}
